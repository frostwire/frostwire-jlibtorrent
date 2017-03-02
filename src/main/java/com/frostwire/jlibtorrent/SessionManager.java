package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.net.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionManager {

    private static final Logger LOG = Logger.getLogger(SessionManager.class);

    private static final long REQUEST_STATS_RESOLUTION_MILLIS = 1000;
    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;

    private static final int[] METADATA_ALERT_TYPES = new int[]
            {AlertType.METADATA_RECEIVED.swig(), AlertType.METADATA_FAILED.swig()};
    private static final String FETCH_MAGNET_DOWNLOAD_KEY = "fetch_magnet___";

    private static final int[] DHT_IMMUTABLE_ITEM_TYPES = {AlertType.DHT_IMMUTABLE_ITEM.swig()};
    private static final int[] DHT_MUTABLE_ITEM_TYPES = {AlertType.DHT_MUTABLE_ITEM.swig()};
    private static final int[] DHT_GET_PEERS_REPLY_ALERT_TYPES = {AlertType.DHT_GET_PEERS_REPLY.swig()};

    private final boolean logging;

    private final AlertListener[] listeners;

    private final ReentrantLock sync;
    private final ReentrantLock syncMagnet;

    private session session;

    private final SessionStats stats;
    private long lastStatsRequestTime;
    private boolean firewalled;
    private final List<TcpEndpoint> listenEndpoints;
    private Address externalAddress;

    private Throwable lastAlertError;

    public SessionManager(boolean logging) {
        this.logging = logging;

        this.listeners = new AlertListener[libtorrent.num_alert_types + 1];

        this.sync = new ReentrantLock();
        this.syncMagnet = new ReentrantLock();

        this.stats = new SessionStats();
        this.listenEndpoints = new LinkedList<>();

        resetState();
    }

    public SessionManager() {
        this(false);
    }

    public session swig() {
        return session;
    }

    public void addListener(AlertListener listener) {
        modifyListeners(true, listener);
    }

    public void removeListener(AlertListener listener) {
        modifyListeners(false, listener);
    }

    public void start(SessionParams params) {
        if (session != null) {
            return;
        }

        sync.lock();

        try {
            if (session != null) {
                return;
            }

            onBeforeStart();

            resetState();

            params.settings().setInteger(settings_pack.int_types.alert_mask.swigValue(), alertMask(logging));
            session = new session(params.swig());
            alertsLoop();

            onAfterStart();

        } finally {
            sync.unlock();
        }
    }

    public void start() {
        settings_pack sp = new settings_pack();

        sp.set_str(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), dhtBootstrapNodes());

        start(new SessionParams(new session_params(sp)));
    }

    public void stop() {
        if (session == null) {
            return;
        }

        sync.lock();

        try {
            if (session == null) {
                return;
            }

            onBeforeStop();

            session s = session;
            session = null; // stop alerts loop and session methods

            resetState();

            s.abort().delete();

            onAfterStop();

        } finally {
            sync.unlock();
        }
    }

    /**
     * This method blocks for at least a second, don't call it from the UI thread.
     */
    public void restart() {
        sync.lock();

        try {

            stop();
            Thread.sleep(1000); // allow some time to release native resources
            start();

        } catch (InterruptedException e) {
            // ignore
        } finally {
            sync.unlock();
        }
    }

    public boolean isRunning() {
        return session != null;
    }

    public void pause() {
        if (session != null && !session.is_paused()) {
            session.pause();
        }
    }

    public void resume() {
        if (session != null) {
            session.resume();
        }
    }

    public boolean isPaused() {
        return session != null ? session.is_paused() : false;
    }

    public SessionStats stats() {
        return stats;
    }

    public long downloadRate() {
        return stats.downloadRate();
    }

    public long uploadRate() {
        return stats.uploadRate();
    }

    public long totalDownload() {
        return stats.totalDownload();
    }

    public long totalUpload() {
        return stats.totalUpload();
    }

    public long dhtNodes() {
        return stats.dhtNodes();
    }

    public boolean isFirewalled() {
        return firewalled;
    }

    public Address externalAddress() {
        return externalAddress;
    }

    public List<TcpEndpoint> listenEndpoints() {
        return new LinkedList<>(listenEndpoints);
    }

    //--------------------------------------------------
    // Settings methods
    //--------------------------------------------------

    /**
     * Returns a setting pack with all the settings
     * the current session is working with.
     * <p>
     * If the current internal session is null, returns
     * null.
     *
     * @return
     */
    public SettingsPack settings() {
        return session != null ? new SettingsPack(session.get_settings()) : null;
    }

    public void applySettings(SettingsPack sp) {
        if (session != null) {

            if (sp == null) {
                throw new IllegalArgumentException("settings pack can't be null");
            }

            session.apply_settings(sp.swig());
            onApplySettings(sp);
        }
    }

    public int downloadRateLimit() {
        if (session == null) {
            return 0;
        }
        return settings().downloadRateLimit();
    }

    public void downloadRateLimit(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().downloadRateLimit(limit));
    }

    public int uploadRateLimit() {
        if (session == null) {
            return 0;
        }
        return settings().uploadRateLimit();
    }

    public void uploadRateLimit(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().uploadRateLimit(limit));
    }

    public int maxActiveDownloads() {
        if (session == null) {
            return 0;
        }
        return settings().activeDownloads();
    }

    public void maxActiveDownloads(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().activeDownloads(limit));
    }

    public int maxActiveSeeds() {
        if (session == null) {
            return 0;
        }
        return settings().activeSeeds();
    }

    public void maxActiveSeeds(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().activeSeeds(limit));
    }

    public int maxConnections() {
        if (session == null) {
            return 0;
        }
        return settings().connectionsLimit();
    }

    public void maxConnections(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().connectionsLimit(limit));
    }

    public int maxPeers() {
        if (session == null) {
            return 0;
        }
        return settings().maxPeerlistSize();
    }

    public void maxPeers(int limit) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().maxPeerlistSize(limit));
    }

    public String listenInterfaces() {
        if (session == null) {
            return null;
        }
        return settings().listenInterfaces();
    }

    public void listenInterfaces(String value) {
        if (session == null) {
            return;
        }
        applySettings(new SettingsPack().listenInterfaces(value));
    }

    //--------------------------------------------------
    // more methods
    //--------------------------------------------------

    /**
     * This function will post a {@link SessionStatsAlert} object, containing a
     * snapshot of the performance counters from the internals of libtorrent.
     */
    public void postSessionStats() {
        if (session != null) {
            session.post_session_stats();
        }
    }

    /**
     * This will cause a {@link DhtStatsAlert} to be posted.
     */
    public void postDhtStats() {
        if (session != null) {
            session.post_dht_stats();
        }
    }

    public boolean isDhtRunning() {
        return session != null ? session.is_dht_running() : false;
    }

    public void startDht() {
        toggleDht(true);
    }

    public void stopDht() {
        toggleDht(false);
    }

    public TorrentHandle find(Sha1Hash sha1) {
        if (session == null) {
            return null;
        }

        torrent_handle th = session.find_torrent(sha1.swig());
        return th != null && th.is_valid() ? new TorrentHandle(th) : null;
    }

    /**
     * @param ti
     * @param saveDir
     * @param resumeFile
     * @param priorities
     */
    public void download(TorrentInfo ti, File saveDir, File resumeFile, Priority[] priorities, List<TcpEndpoint> peers) {
        if (session == null) {
            return;
        }

        if (!ti.isValid()) {
            throw new IllegalArgumentException("torrent info not valid");
        }

        torrent_handle th = session.find_torrent(ti.swig().info_hash());

        if (th != null && th.is_valid()) {
            // found a download with the same hash, just adjust the priorities if needed
            if (priorities != null) {
                if (ti.numFiles() != priorities.length) {
                    throw new IllegalArgumentException("priorities count should be equals to the number of files");
                }
                th.prioritize_files(Priority.array2int_vector(priorities));
            } else {
                // did they just add the entire torrent (therefore not selecting any priorities)
                priorities = Priority.array(Priority.NORMAL, ti.numFiles());
                th.prioritize_files(Priority.array2int_vector(priorities));
            }

            return;
        }

        add_torrent_params p = null;

        if (resumeFile != null) {
            try {
                byte[] data = Files.bytes(resumeFile);
                error_code ec = new error_code();
                p = add_torrent_params.read_resume_data(Vectors.bytes2byte_vector(data), ec);
                if (ec.value() != 0) {
                    throw new IllegalArgumentException("Unable to read the resume data: " + ec.message());
                }
            } catch (Throwable e) {
                LOG.warn("Unable to set resume data", e);
            }
        }

        if (p == null) {
            p = add_torrent_params.create_instance();
        }

        p.set_ti(ti.swig());
        if (saveDir != null) {
            p.setSave_path(saveDir.getAbsolutePath());
        }

        if (priorities != null) {
            if (ti.files().numFiles() != priorities.length) {
                throw new IllegalArgumentException("priorities count should be equals to the number of files");
            }
            byte_vector v = new byte_vector();
            for (int i = 0; i < priorities.length; i++) {
                v.push_back((byte) priorities[i].swig());
            }
            p.setFile_priorities(v);
        }

        if (peers != null && !peers.isEmpty()) {
            tcp_endpoint_vector v = new tcp_endpoint_vector();
            for (TcpEndpoint endp : peers) {
                v.push_back(endp.swig());
            }
            p.setPeers(v);
        }

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        p.setFlags(flags);

        session.async_add_torrent(p);
    }

    /**
     * @param ti
     * @param saveDir
     */
    public void download(TorrentInfo ti, File saveDir) {
        download(ti, saveDir, null, null, null);
    }

    public void remove(TorrentHandle th, SessionHandle.Options options) {
        if (session != null && th.isValid()) {
            session.remove_torrent(th.swig(), options.swig());
        }
    }

    public void remove(TorrentHandle th) {
        if (session != null && th.isValid()) {
            session.remove_torrent(th.swig());
        }
    }

    /**
     * @param uri     magnet uri
     * @param timeout in seconds
     * @param maxSize in bytes
     * @param extra   if extra data is included
     * @return the bencoded info or null
     */
    public byte[] fetchMagnet(String uri, int timeout, final boolean extra, final int maxSize) {
        if (session == null) {
            return null;
        }

        add_torrent_params p = add_torrent_params.create_instance_disabled_storage();
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, p, ec);

        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }

        final sha1_hash info_hash = p.getInfo_hash();
        final byte[][] data = {null};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener listener = new AlertListener() {
            @Override
            public int[] types() {
                return METADATA_ALERT_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                torrent_handle th = ((TorrentAlert<?>) alert).swig().getHandle();
                if (th == null || !th.is_valid() || th.info_hash().op_ne(info_hash)) {
                    return;
                }

                AlertType type = alert.type();

                if (type.equals(AlertType.METADATA_RECEIVED)) {
                    MetadataReceivedAlert a = ((MetadataReceivedAlert) alert);
                    int size = a.metadataSize();
                    if (0 < size && size <= maxSize) {
                        data[0] = a.torrentData(extra);
                    }
                }

                signal.countDown();
            }
        };

        addListener(listener);

        boolean add = false;
        torrent_handle th = null;

        try {

            syncMagnet.lock();

            try {
                th = session.find_torrent(info_hash);
                if (th != null && th.is_valid()) {
                    // we have a download with the same info-hash
                    add = false;

                    torrent_info ti = th.torrent_file_ptr();
                    if (ti != null && ti.is_valid()) {
                        create_torrent ct = new create_torrent(ti);
                        entry e = ct.generate();

                        int size = ti.metadata_size();
                        if (0 < size && size <= maxSize) {
                            data[0] = Vectors.byte_vector2bytes(e.bencode());
                        }
                        signal.countDown();
                    }
                } else {
                    add = true;
                }

                if (add) {
                    p.setName(FETCH_MAGNET_DOWNLOAD_KEY + uri);
                    p.setSave_path(FETCH_MAGNET_DOWNLOAD_KEY + uri);

                    long flags = p.getFlags();
                    flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
                    flags |= add_torrent_params.flags_t.flag_upload_mode.swigValue();
                    flags |= add_torrent_params.flags_t.flag_stop_when_ready.swigValue();
                    p.setFlags(flags);

                    ec.clear();
                    th = session.add_torrent(p, ec);
                    th.resume();
                }
            } finally {
                syncMagnet.unlock();
            }

            signal.await(timeout, TimeUnit.SECONDS);

        } catch (Throwable e) {
            LOG.error("Error fetching magnet", e);
        } finally {
            removeListener(listener);
            if (session != null && add && th != null && th.is_valid()) {
                session.remove_torrent(th);
            }
        }

        return data[0];
    }

    /**
     * Similar to call {@link #fetchMagnet(String, int, boolean, int)} with
     * a maximum size of 2MB.
     *
     * @param uri     magnet uri
     * @param timeout in seconds
     * @param extra   if extra data is included
     * @return the bencoded info or null
     */
    public byte[] fetchMagnet(String uri, int timeout, boolean extra) {
        return fetchMagnet(uri, timeout, extra, 2 * 1024 * 1024);
    }

    /**
     * Similar to call {@link #fetchMagnet(String, int, boolean)} with
     * a maximum {@code extra = false}.
     *
     * @param uri
     * @param timeout
     * @return the bencoded info or null
     */
    public byte[] fetchMagnet(String uri, int timeout) {
        return fetchMagnet(uri, timeout, false);
    }

    /**
     * @param sha1
     * @param timeout in seconds
     * @return the item
     */
    public Entry dhtGetItem(Sha1Hash sha1, int timeout) {
        if (session == null) {
            return null;
        }

        final sha1_hash target = sha1.swig();
        final Entry[] result = {null};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener listener = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_IMMUTABLE_ITEM_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                DhtImmutableItemAlert a = (DhtImmutableItemAlert) alert;
                if (target.op_eq(a.swig().getTarget())) {
                    result[0] = new Entry(new entry(a.swig().getItem()));
                    signal.countDown();
                }
            }
        };

        addListener(listener);

        try {

            session.dht_get_item(target);

            signal.await(timeout, TimeUnit.SECONDS);

        } catch (Throwable e) {
            LOG.error("Error getting immutable item", e);
        } finally {
            removeListener(listener);
        }

        return result[0];
    }

    /**
     * @param entry the data
     * @return the target key
     */
    public Sha1Hash dhtPutItem(Entry entry) {
        return session != null ? new SessionHandle(session).dhtPutItem(entry) : null;
    }

    public MutableItem dhtGetItem(final byte[] key, final byte[] salt, int timeout) {
        if (session == null) {
            return null;
        }

        final MutableItem[] result = {null};
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener listener = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_MUTABLE_ITEM_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                DhtMutableItemAlert a = (DhtMutableItemAlert) alert;
                boolean sameKey = Arrays.equals(key, a.key());
                boolean sameSalt = Arrays.equals(salt, a.salt());
                if (sameKey && sameSalt) {
                    Entry e = new Entry(new entry(a.swig().getItem()));
                    MutableItem item = new MutableItem(e, a.signature(), a.seq());
                    result[0] = item;
                    signal.countDown();
                }
            }
        };

        addListener(listener);

        try {

            new SessionHandle(session).dhtGetItem(key, salt);

            signal.await(timeout, TimeUnit.SECONDS);

        } catch (Throwable e) {
            LOG.error("Error getting mutable item", e);
        } finally {
            removeListener(listener);
        }

        return result[0];
    }

    public void dhtPutItem(byte[] publicKey, byte[] privateKey, Entry entry, byte[] salt) {
        if (session != null) {
            new SessionHandle(session).dhtPutItem(publicKey, privateKey, entry, salt);
        }
    }

    /**
     * @param sha1
     * @param timeout in seconds
     * @return the peer list or an empty list
     */
    public ArrayList<TcpEndpoint> dhtGetPeers(Sha1Hash sha1, int timeout) {
        final ArrayList<TcpEndpoint> result = new ArrayList<>();
        if (session == null) {
            return result;
        }

        final sha1_hash target = sha1.swig();
        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener listener = new AlertListener() {

            @Override
            public int[] types() {
                return DHT_GET_PEERS_REPLY_ALERT_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                DhtGetPeersReplyAlert a = (DhtGetPeersReplyAlert) alert;
                if (target.op_eq(a.swig().getInfo_hash())) {
                    result.addAll(a.peers());
                    signal.countDown();
                }
            }
        };

        addListener(listener);

        try {

            session.dht_get_peers(target);

            signal.await(timeout, TimeUnit.SECONDS);

        } catch (Throwable e) {
            LOG.error("Error getting peers from the dht", e);
        } finally {
            removeListener(listener);
        }

        return result;
    }

    public void dhtAnnounce(Sha1Hash sha1, int port, int flags) {
        if (session != null) {
            session.dht_announce(sha1.swig(), port, flags);
        }
    }

    public void dhtAnnounce(Sha1Hash sha1) {
        if (session != null) {
            session.dht_announce(sha1.swig());
        }
    }

    /**
     * @param dir
     */
    public void moveStorage(File dir) {
        if (session == null) {
            return;
        }

        try {
            torrent_handle_vector v = session.get_torrents();
            int size = (int) v.size();

            String path = dir.getAbsolutePath();
            for (int i = 0; i < size; i++) {
                torrent_handle th = v.get(i);
                torrent_status ts = th.status();
                boolean incomplete = !ts.getIs_seeding() && !ts.getIs_finished();
                if (th.is_valid() && incomplete) {
                    th.move_storage(path);
                }
            }
        } catch (Throwable e) {
            LOG.error("Error changing save path for session", e);
        }
    }

    public byte[] saveState() {
        return session != null ? new SessionHandle(session).saveState() : null;
    }

    public void loadState(byte[] data) {
        if (session != null) {
            new SessionHandle(session).loadState(data);
        }
    }

    public String magnetPeers() {
        if (session == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (TcpEndpoint endp : listenEndpoints) {
            try {
                Address address = endp.address();
                if (address.isLoopback() || address.isMulticast()) {
                    continue;
                }

                if (address.isUnspecified()) {
                    try {
                        addAllInterfaces(address.isV6(), endp.port(), sb);
                    } catch (Throwable e) {
                        LOG.error("Error adding all listen interfaces", e);
                    }
                } else {
                    addMagnetPeer(endp.address(), endp.port(), sb);
                }

                if (externalAddress != null) {
                    addMagnetPeer(externalAddress, endp.port(), sb);
                }
            } catch (Throwable e) {
                LOG.error("Error processing listen endpoint", e);
            }
        }

        return sb.toString();
    }

    /**
     * This methods return the last error recorded calling the alert
     * listeners.
     *
     * @return the last alert listener exception registered (or null)
     */
    public Throwable lastAlertError() {
        return lastAlertError;
    }

    protected void onBeforeStart() {
    }

    protected void onAfterStart() {
    }

    protected void onBeforeStop() {
    }

    protected void onAfterStop() {
    }

    protected void onApplySettings(SettingsPack sp) {
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    private void resetState() {
        stats.clear();
        firewalled = true;
        listenEndpoints.clear();
        externalAddress = null;
    }

    private void modifyListeners(boolean add, AlertListener listener) {
        if (listener == null) {
            return;
        }

        int[] types = listener.types();

        // all alert-type including listener
        if (types == null) {
            modifyListeners(add, libtorrent.num_alert_types, listener);
        } else {
            for (int i = 0; i < types.length; i++) {
                modifyListeners(add, types[i], listener);
            }
        }
    }

    private synchronized void modifyListeners(boolean add, int type, AlertListener listener) {
        if (add) {
            listeners[type] = AlertMulticaster.add(listeners[type], listener);
        } else {
            listeners[type] = AlertMulticaster.remove(listeners[type], listener);
        }
    }

    private void fireAlert(Alert<?> a, int type) {
        AlertListener listener = listeners[type];
        if (listener != null) {
            try {
                listener.alert(a);
            } catch (Throwable e) {
                LOG.warn("Error calling alert listener: " + e.getMessage());
                lastAlertError = e;
            }
        }
    }

    private void onListenSucceeded(ListenSucceededAlert alert) {
        try {
            if (alert.socketType() == SocketType.TCP) {
                String address = alert.address().toString(); // clone
                int port = alert.port();
                listenEndpoints.add(new TcpEndpoint(address, port));
            }
        } catch (Throwable e) {
            LOG.error("Error adding listen endpoint to internal list", e);
        }
    }

    private void toggleDht(boolean on) {
        if (session == null || isDhtRunning() == on) {
            return;
        }
        applySettings(new SettingsPack().enableDht(on));
    }

    private void onExternalIpAlert(ExternalIpAlert alert) {
        try {
            // libtorrent perform all kind of tests
            // to avoid non usable addresses
            String address = alert.externalAddress().toString(); // clone
            externalAddress = new Address(address);
        } catch (Throwable e) {
            LOG.error("Error saving reported external ip", e);
        }
    }

    private boolean isFetchMangetDownload(TorrentAddedAlert alert) {
        String name = alert.torrentName();
        return name != null && name.contains(FETCH_MAGNET_DOWNLOAD_KEY);
    }

    private static int alertMask(boolean logging) {
        int mask = alert.category_t.all_categories.swigValue();
        if (!logging) {
            int log_mask = alert.category_t.session_log_notification.swigValue() |
                    alert.category_t.torrent_log_notification.swigValue() |
                    alert.category_t.peer_log_notification.swigValue() |
                    alert.category_t.dht_log_notification.swigValue() |
                    alert.category_t.port_mapping_log_notification.swigValue() |
                    alert.category_t.picker_log_notification.swigValue();
            mask = mask & ~log_mask;
        }
        return mask;
    }

    private static String dhtBootstrapNodes() {
        StringBuilder sb = new StringBuilder();

        sb.append("dht.libtorrent.org:25401").append(",");
        sb.append("router.bittorrent.com:6881").append(",");
        sb.append("dht.transmissionbt.com:6881").append(",");
        // for DHT IPv6
        sb.append("outer.silotis.us:6881");

        return sb.toString();
    }

    private static void addAllInterfaces(boolean v6, int port, StringBuilder sb) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            if (iface.isLoopback()) {
                continue;
            }
            List<InterfaceAddress> interfaceAddresses = iface.getInterfaceAddresses();
            for (InterfaceAddress ifaceAddress : interfaceAddresses) {
                InetAddress inetAddress = ifaceAddress.getAddress();

                if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                    continue;
                }

                // same family?
                if (inetAddress instanceof Inet6Address != v6) {
                    continue;
                }

                String hostAddress = ifaceAddress.getAddress().getHostAddress();

                // IPv6 address should be expressed as [address]:port
                if (v6) {
                    // remove the %22 or whatever mask at the end.
                    if (hostAddress.contains("%")) {
                        hostAddress = hostAddress.substring(0, hostAddress.indexOf("%"));
                    }
                    hostAddress = "[" + hostAddress + "]";
                }
                sb.append("&x.pe=" + hostAddress + ":" + port);
            }
        }
    }

    private static void addMagnetPeer(Address address, int port, StringBuilder sb) {
        String hostAddress = address.toString();
        if (hostAddress.contains("invalid")) {
            return;
        }

        if (address.isV6()) {
            hostAddress = "[" + hostAddress + "]";
        }
        sb.append("&x.pe=" + hostAddress + ":" + port);
    }

    private void alertsLoop() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                alert_ptr_vector v = new alert_ptr_vector();

                while (session != null) {
                    alert ptr = session.wait_for_alert_ms(ALERTS_LOOP_WAIT_MILLIS);

                    if (session == null) {
                        return;
                    }

                    if (ptr != null) {
                        session.pop_alerts(v);
                        long size = v.size();
                        for (int i = 0; i < size; i++) {
                            alert a = v.get(i);
                            int type = a.type();

                            Alert<?> alert = null;

                            switch (AlertType.fromSwig(type)) {
                                case SESSION_STATS:
                                    alert = Alerts.cast(a);
                                    stats.update((SessionStatsAlert) alert);
                                    break;
                                case PORTMAP:
                                    firewalled = false;
                                    break;
                                case PORTMAP_ERROR:
                                    firewalled = true;
                                    break;
                                case LISTEN_SUCCEEDED:
                                    alert = Alerts.cast(a);
                                    onListenSucceeded((ListenSucceededAlert) alert);
                                    break;
                                case EXTERNAL_IP:
                                    alert = Alerts.cast(a);
                                    onExternalIpAlert((ExternalIpAlert) alert);
                                    break;
                                case TORRENT_ADDED:
                                    alert = Alerts.cast(a);
                                    if (isFetchMangetDownload((TorrentAddedAlert) alert)) {
                                        continue;
                                    }
                                    break;
                            }

                            if (listeners[type] != null) {
                                if (alert == null) {
                                    alert = Alerts.cast(a);
                                }
                                fireAlert(alert, type);
                            }

                            if (type != AlertType.SESSION_STATS.swig() &&
                                    listeners[libtorrent.num_alert_types] != null) {
                                if (alert == null) {
                                    alert = Alerts.cast(a);
                                }
                                fireAlert(alert, libtorrent.num_alert_types);
                            }
                        }
                        v.clear();
                    }

                    long now = System.currentTimeMillis();
                    if ((now - lastStatsRequestTime) >= REQUEST_STATS_RESOLUTION_MILLIS) {
                        lastStatsRequestTime = now;
                        postSessionStats();
                    }
                }
            }
        };

        Thread t = new Thread(r, "SessionManager-alertsLoop");
        t.setDaemon(true);
        t.start();
    }

    public static final class MutableItem {

        private MutableItem(Entry item, byte[] signature, long seq) {
            this.item = item;
            this.signature = signature;
            this.seq = seq;
        }

        public final Entry item;
        public final byte[] signature;
        public final long seq;
    }
}
