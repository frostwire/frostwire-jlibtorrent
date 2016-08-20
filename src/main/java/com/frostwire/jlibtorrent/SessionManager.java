package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.*;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionManager {

    private static final Logger LOG = Logger.getLogger(SessionManager.class);

    private static final int[] METADATA_ALERT_TYPES = new int[]
            {AlertType.METADATA_RECEIVED.swig(), AlertType.METADATA_FAILED.swig()};

    private final String interfaces;
    private final int retries;
    private final boolean logging;

    private final ExecutorService alertsPool;
    private final AlertsLoop alertsLoop;
    private final AlertsCallback alertsCallback;

    private final AlertListener[] listeners;

    private final ReentrantLock sync;
    private final ReentrantLock syncMagnet;

    private session session;

    public SessionManager(String interfaces, int retries, boolean logging) {
        this.interfaces = interfaces;
        this.retries = retries;
        this.logging = logging;

        this.alertsPool = Executors.newSingleThreadExecutor(new AlertsThreadFactory());
        this.alertsLoop = new AlertsLoop();
        this.alertsCallback = new AlertsCallback();

        this.listeners = new AlertListener[libtorrent.num_alert_types + 1];

        this.sync = new ReentrantLock();
        this.syncMagnet = new ReentrantLock();
    }

    public SessionManager() {
        this("0.0.0.0:6881,[::]:6881", 10, false);
    }

    public void addListener(AlertListener listener) {
        modifyListeners(true, listener);
    }

    public void removeListener(AlertListener listener) {
        modifyListeners(false, listener);
    }

    public void start() {
        if (session != null) {
            return;
        }

        sync.lock();

        try {
            if (session != null) {
                return;
            }

            session = createSession(interfaces, retries, logging);
            session.set_alert_notify_callback(alertsCallback);

            // TODO: this will change once the new settings is merged in master
            for (Pair p : defaultDhtRouters()) {
                session.add_dht_router(p.to_string_int_pair());
            }

        } finally {
            sync.unlock();
        }
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

            session.abort().delete();
            session = null;

        } finally {
            sync.unlock();
        }
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

    /**
     * @param uri
     * @param timeout in seconds
     * @param maxSize in bytes
     * @return
     */
    public byte[] fetchMagnet(String uri, int timeout, final int maxSize) {
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
            if (add && th != null && th.is_valid()) {
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

    private void modifyListeners(boolean add, int type, AlertListener listener) {
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

    private static session createSession(String interfaces, int retries, boolean logging) {
        settings_pack sp = new settings_pack();

        sp.set_str(settings_pack.string_types.listen_interfaces.swigValue(), interfaces);
        sp.set_int(settings_pack.int_types.max_retry_port_bind.swigValue(), retries);
        sp.set_int(settings_pack.int_types.alert_mask.swigValue(), alertMask(logging));

        return new session(sp);
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

    private static LinkedList<Pair> defaultDhtRouters() {
        LinkedList<Pair> list = new LinkedList();

        list.add(new Pair("router.bittorrent.com", 6881));
        list.add(new Pair("dht.transmissionbt.com", 6881));

        // for DHT IPv6
        list.add(new Pair("outer.silotis.us", 6881));

        return list;
    }

    private final class AlertsLoop implements Runnable {

        private final alert_ptr_vector v;

        public AlertsLoop() {
            v = new alert_ptr_vector();
        }

        @Override
        public void run() {

            if (session == null) {
                return;
            }

            session.pop_alerts(v);
            int size = (int) v.size();
            for (int i = 0; i < size; i++) {
                alert a = v.get(i);
                int type = a.type();

                Alert<?> alert = null;

                if (listeners[type] != null) {
                    if (alert == null) {
                        alert = Alerts.cast(a);
                    }
                    fireAlert(alert, type);
                }

                if (listeners[libtorrent.num_alert_types] != null) {
                    if (alert == null) {
                        alert = Alerts.cast(a);
                    }
                    fireAlert(alert, libtorrent.num_alert_types);
                }
            }
            v.clear();
        }
    }

    private final class AlertsCallback extends alert_notify_callback {

        @Override
        public void on_alert() {
            alertsPool.execute(alertsLoop);
        }
    }

    private static final class AlertsThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("SessionManager-alerts");
            t.setDaemon(true);
            return t;
        }
    }
}
