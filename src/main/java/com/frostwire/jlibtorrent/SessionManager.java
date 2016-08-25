package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionManager {

    private static final Logger LOG = Logger.getLogger(SessionManager.class);

    private static final long REQUEST_STATS_RESOLUTION_MILLIS = 1000;
    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;

    private static final int[] METADATA_ALERT_TYPES = new int[]
            {AlertType.METADATA_RECEIVED.swig(), AlertType.METADATA_FAILED.swig()};

    private final boolean logging;

    private final AlertListener[] listeners;

    private final ReentrantLock sync;
    private final ReentrantLock syncMagnet;

    private session session;

    private final SessionStats stats;
    private long lastStatsRequestTime;

    public SessionManager(boolean logging) {
        this.logging = logging;

        this.listeners = new AlertListener[libtorrent.num_alert_types + 1];

        this.sync = new ReentrantLock();
        this.syncMagnet = new ReentrantLock();

        this.stats = new SessionStats();
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

    public void start(SettingsPack sp) {
        if (session != null) {
            return;
        }

        sync.lock();

        try {
            if (session != null) {
                return;
            }

            sp.setInteger(settings_pack.int_types.alert_mask.swigValue(), alertMask(logging));
            session = new session(sp.swig());
            alertsLoop();

        } finally {
            sync.unlock();
        }
    }

    public void start() {
        settings_pack sp = new settings_pack();

        sp.set_str(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), dhtBootstrapNodes());

        start(new SettingsPack(sp));
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

            session s = session;
            session = null; // stop alerts loop and session methods
            stats.clear();
            s.abort().delete();

        } finally {
            sync.unlock();
        }
    }

    public boolean running() {
        return session != null;
    }

    public SessionStats stats() {
        return stats;
    }

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
    public void postDHTStats() {
        if (session != null) {
            session.post_dht_stats();
        }
    }

    public void download(TorrentInfo ti, File saveDir, File resumeFile, Priority[] priorities) {
        if (session == null) {
            return;
        }

        if (!ti.isValid()) {
            throw new IllegalArgumentException("torrent info not valid");
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
                throw new IllegalArgumentException("priorities must be the same number of elements as files in torrent info");
            }
            byte_vector v = new byte_vector();
            for (int i = 0; i < priorities.length; i++) {
                v.push_back((byte) priorities[i].swig());
            }
            p.setFile_priorities(v);
        }

        long flags = p.get_flags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        p.set_flags(flags);

        session.async_add_torrent(p);
    }

    /**
     * @param uri
     * @param timeout in seconds
     * @param maxSize in bytes
     * @return
     */
    public byte[] fetchMagnet(String uri, int timeout, final int maxSize) {
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
        final byte[][] data = new byte[1][];
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
                    if (size <= maxSize) {
                        data[0] = a.torrentData();
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
                    // we have a download with the same info-hash, let's wait
                    add = false;
                } else {
                    add = true;
                }

                if (add) {
                    p.setName("fetch_magnet" + uri);
                    p.setSave_path("fetch_magnet" + uri);

                    long flags = p.get_flags();
                    flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
                    p.set_flags(flags);

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
     * Similar to call {@link #fetchMagnet(String, int, int)} with
     * a maximum size of 2MB.
     *
     * @param uri
     * @param timeout in seconds
     * @return
     */
    public byte[] fetchMagnet(String uri, int timeout) {
        return fetchMagnet(uri, timeout, 2 * 1024 * 1024);
    }

    public byte[] saveState() {
        return new SessionHandle(session).saveState();
    }

    public void loadState(byte[] data) {
        new SessionHandle(session).loadState(data);
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
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
                LOG.warn("Error calling alert listener", e);
            }
        }
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

                            if (type == AlertType.SESSION_STATS.swig()) {
                                alert = Alerts.cast(a);
                                stats.update((SessionStatsAlert) alert);
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
}
