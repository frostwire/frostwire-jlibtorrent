package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.Alerts;
import com.frostwire.jlibtorrent.swig.*;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionManager {

    private final String interfaces;
    private final int retries;
    private final boolean logging;

    private final ExecutorService alertsPool;
    private final AlertsCallback alertsCallback;
    private final alert_ptr_vector alerts;

    private final ReentrantLock sync;

    private session session;

    public SessionManager(String interfaces, int retries, boolean logging) {
        this.interfaces = interfaces;
        this.retries = retries;
        this.logging = logging;

        this.alertsPool = Executors.newSingleThreadExecutor(new AlertsThreadFactory());
        this.alertsCallback = new AlertsCallback();
        this.alerts = new alert_ptr_vector();

        this.sync = new ReentrantLock();
    }

    public SessionManager() {
        this("0.0.0.0:6881,[::]:6881", 10, false);
    }

    public void start() {
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

    private session createSession(String interfaces, int retries, boolean logging) {
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

        @Override
        public void run() {

            if (session == null) {
                return;
            }

            session.pop_alerts(alerts);
            int size = (int) alerts.size();
            for (int i = 0; i < size; i++) {
                alert a = alerts.get(i);

                Alert alert = Alerts.cast(a);
                System.out.println(alert);
            }
            alerts.clear();
        }
    }

    private final class AlertsCallback extends alert_notify_callback {

        @Override
        public void on_alert() {
            alertsPool.execute(new AlertsLoop());
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
