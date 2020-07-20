package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
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

    private volatile session session;

    private final SessionStats stats;
    private long lastStatsRequestTime;
    private boolean firewalled;
    private final Map<String, String> listenEndpoints;
    private String externalAddress;
    private int externalPort;
    private Thread alertsLoop;

    private Throwable lastAlertError;

    public SessionManager(boolean logging) {
        this.logging = logging;

        this.listeners = new AlertListener[Alerts.NUM_ALERT_TYPES + 1];

        this.sync = new ReentrantLock();
        this.syncMagnet = new ReentrantLock();

        this.stats = new SessionStats();
        this.listenEndpoints = new HashMap<>();

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

            params.settings().setInteger(settings_pack.int_types.alert_mask.swigValue(), alertMask(logging).to_int());
            session = new session(params.swig());
            alertsLoop();

            // block all connections to port < 1024, but
            // allows 80 and 443 for web seeds
            port_filter f = new port_filter();
            f.add_rule(0, 79, 1);
            f.add_rule(81, 442, 1);
            f.add_rule(444, 1023, 1);
            session.set_port_filter(f);

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

    /**
     * This method blocks during the destruction of the native session, it
     * could take some time, don't call this from the UI thread or other
     * sensitive multithreaded code.
     */
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

            // guarantee one more alert is post and detected
            s.post_session_stats();
            try {
                // 250 is to ensure that the sleep is bigger
                // than the wait in alerts loop
                Thread.sleep(ALERTS_LOOP_WAIT_MILLIS + 250);
            } catch (InterruptedException ignore) {
            }

            if (alertsLoop != null) {
                try {
                    alertsLoop.join();
                } catch (Throwable e) {
                    // ignore
                }
            }

            resetState();

            s.delete();

            onAfterStop();

        } finally {
            sync.unlock();
        }
    }

    /**
     * This method blocks for at least a second plus the time
     * needed to destroy the native session, don't call it from the UI thread.
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

    public String externalAddress() {
        return externalAddress;
    }

    public List<String> listenEndpoints() {
        return new ArrayList<>(listenEndpoints.values());
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

    /**
     * This functions instructs the session to post the
     * {@link com.frostwire.jlibtorrent.alerts.StateUpdateAlert},
     * containing the status of all torrents whose state changed since the
     * last time this function was called.
     * <p>
     * Only torrents who has the state subscription flag set will be
     * included.
     */
    public void postTorrentUpdates() {
        if (session != null) {
            session.post_torrent_updates();
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
     * @param ti         the torrent info to download
     * @param saveDir    the path to save the downloaded files
     * @param resumeFile the file with the resume file
     * @param priorities the initial file priorities
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
                th.prioritize_files2(Priority.array2vector(priorities));
            } else {
                // did they just add the entire torrent (therefore not selecting any priorities)
                priorities = Priority.array(Priority.NORMAL, ti.numFiles());
                th.prioritize_files2(Priority.array2vector(priorities));
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
            p.set_file_priorities2(v);
        }

        if (peers != null && !peers.isEmpty()) {
            tcp_endpoint_vector v = new tcp_endpoint_vector();
            for (TcpEndpoint endp : peers) {
                v.push_back(endp.swig());
            }
            p.set_peers(v);
        }

        torrent_flags_t flags = p.getFlags();

        flags = flags.and_(TorrentFlags.AUTO_MANAGED.inv());

        p.setFlags(flags);

        session.async_add_torrent(p);
    }

    /**
     * Downloads a magnet uri.
     *
     * @param magnetUri the magnet uri to download
     * @param saveDir   the path to save the downloaded files
     */
    public void download(String magnetUri, File saveDir) {
        if (session == null) {
            return;
        }

        error_code ec = new error_code();
        add_torrent_params p = add_torrent_params.parse_magnet_uri(magnetUri, ec);

        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }

        sha1_hash info_hash = p.getInfo_hash();

        torrent_handle th = session.find_torrent(info_hash);

        if (th != null && th.is_valid()) {
            // found a download with the same hash
            return;
        }

        if (saveDir != null) {
            p.setSave_path(saveDir.getAbsolutePath());
        }

        torrent_flags_t flags = p.getFlags();

        flags = flags.and_(TorrentFlags.AUTO_MANAGED.inv());

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

    public void remove(TorrentHandle th, remove_flags_t options) {
        if (session != null && th.isValid()) {
            session.remove_torrent(th.swig(), options);
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

        error_code ec = new error_code();
        add_torrent_params p = add_torrent_params.parse_magnet_uri(uri, ec);

        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }

        p.set_disabled_storage();

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

                    torrent_flags_t flags = p.getFlags();
                    flags = flags.and_(TorrentFlags.AUTO_MANAGED.inv());
                    flags = flags.or_(TorrentFlags.UPLOAD_MODE);
                    flags = flags.or_(TorrentFlags.STOP_WHEN_READY);
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

    /**
     * Instructs the session to reopen all listen and outgoing sockets.
     * <p>
     * It's useful in the case your platform doesn't support the built in
     * IP notifier mechanism, or if you have a better more reliable way to
     * detect changes in the IP routing table.
     */
    public void reopenNetworkSockets() {
        if (session != null) {
            session.reopen_network_sockets();
        }
    }

    public String magnetPeers() {
        if (session == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if (externalAddress != null && externalPort > 0) {
            sb.append("&x.pe=");
            sb.append(externalAddress).append(":").append(externalPort);
        }

        for (String endp : listenEndpoints.values()) {
            sb.append("&x.pe=").append(endp);
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
        alertsLoop = null;
    }

    private void modifyListeners(boolean add, AlertListener listener) {
        if (listener == null) {
            return;
        }

        int[] types = listener.types();

        // all alert-type including listener
        if (types == null) {
            modifyListeners(add, Alerts.NUM_ALERT_TYPES, listener);
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
            // only store TCP endpoints
            if (alert.socketType() == SocketType.TCP) {
                return;
            }

            Address addr = alert.address();

            if (addr.isV4()) {
                // consider just one IPv4 listen endpoint port
                // as the external port
                externalPort = alert.port();
            }

            // only consider valid addresses
            if (addr.isLoopback() || addr.isMulticast() || addr.isUnspecified()) {
                return;
            }

            String address = addr.toString();
            int port = alert.port();

            // avoid invalid addresses
            if (address.contains("invalid")) {
                return;
            }

            // avoid local-link addresses
            if (address.startsWith("127.") || address.startsWith("fe80::")) {
                return;
            }

            String endp = (addr.isV6() ? "[" + address + "]" : address) + ":" + port;
            listenEndpoints.put(address, endp);
        } catch (Throwable e) {
            LOG.error("Error adding listen endpoint to internal list", e);
        }
    }

    private void onListenFailed(ListenFailedAlert alert) {
        LOG.error("onListenFailed(): iface= " + alert.listenInterface() +
                ", address= " + alert.address() +
                ", port= " + alert.port() +
                ", socketType= " + alert.socketType() +
                ", errorCode= " + alert.error());
        LOG.error("onListenFailed(): error_message=" + alert.message());
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
            address addr = alert.swig().get_external_address();
            // filter out non IPv4 addresses
            if (!addr.is_v4()) {
                return;
            }
            String address = alert.externalAddress().toString();
            if (address.contains("invalid")) {
                return;
            }
            externalAddress = address;
        } catch (Throwable e) {
            LOG.error("Error saving reported external ip", e);
        }
    }

    private boolean isFetchMagnetDownload(AddTorrentAlert alert) {
        String name = alert.torrentName();
        return name != null && name.contains(FETCH_MAGNET_DOWNLOAD_KEY);
    }

    private static alert_category_t alertMask(boolean logging) {
        alert_category_t mask = alert.all_categories;
        if (!logging) {
            alert_category_t log_mask = alert.session_log_notification;
            log_mask = log_mask.or_(alert.torrent_log_notification);
            log_mask = log_mask.or_(alert.peer_log_notification);
            log_mask = log_mask.or_(alert.dht_log_notification);
            log_mask = log_mask.or_(alert.port_mapping_log_notification);
            log_mask = log_mask.or_(alert.picker_log_notification);

            mask = mask.and_(log_mask.inv());
        }
        return mask;
    }

    private static String dhtBootstrapNodes() {
        StringBuilder sb = new StringBuilder();

        sb.append("dht.libtorrent.org:25401").append(",");
        sb.append("router.bittorrent.com:6881").append(",");
        sb.append("router.utorrent.com:6881").append(",");
        sb.append("dht.transmissionbt.com:6881").append(",");
        // for DHT IPv6
        sb.append("router.silotis.us:6881");

        return sb.toString();
    }

    private static boolean isSpecialType(int type) {
        return type == AlertType.SESSION_STATS.swig() ||
                type == AlertType.STATE_UPDATE.swig() ||
                type == AlertType.SESSION_STATS_HEADER.swig();
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
                                case LISTEN_FAILED:
                                    alert = Alerts.cast(a);
                                    onListenFailed((ListenFailedAlert) alert);
                                    break;
                                case EXTERNAL_IP:
                                    alert = Alerts.cast(a);
                                    onExternalIpAlert((ExternalIpAlert) alert);
                                    break;
                                case ADD_TORRENT:
                                    alert = Alerts.cast(a);
                                    if (isFetchMagnetDownload((AddTorrentAlert) alert)) {
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

                            if (!isSpecialType(type) && listeners[Alerts.NUM_ALERT_TYPES] != null) {
                                if (alert == null) {
                                    alert = Alerts.cast(a);
                                }
                                fireAlert(alert, Alerts.NUM_ALERT_TYPES);
                            }
                        }
                        v.clear();
                    }

                    long now = System.currentTimeMillis();
                    if ((now - lastStatsRequestTime) >= REQUEST_STATS_RESOLUTION_MILLIS) {
                        lastStatsRequestTime = now;
                        postSessionStats();
                        postTorrentUpdates();
                    }
                }
            }
        };

        Thread t = new Thread(r, "SessionManager-alertsLoop");
        t.setDaemon(true);
        t.start();

        alertsLoop = t;
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
