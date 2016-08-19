package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.Alerts;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionManager {

    private static final long ALERTS_LOOP_WAIT_MILLIS = 1000;

    private final String interfaces;
    private final int retries;
    private final boolean logging;

    private final ReentrantLock sync;

    private session session;
    private Thread alertsLoop;

    public SessionManager(String interfaces, int retries, boolean logging) {
        this.interfaces = interfaces;
        this.retries = retries;
        this.logging = logging;

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

            startAlertsLoop();

            for (Pair p : defaultDhtRouters()) {
                session.add_dht_router(p.to_string_int_pair());
            }

        } finally {
            sync.unlock();
        }
    }

    public static TorrentHandle download(Session s, TorrentInfo ti, File saveDir) {
        if (saveDir == null) {
            throw new IllegalArgumentException("saveDir can't be null");
        }

        add_torrent_params p = add_torrent_params.create_instance();

        p.set_ti(ti.swig());
        p.setSave_path(saveDir.getAbsolutePath());

        long flags = p.get_flags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        p.set_flags(flags);

        error_code ec = new error_code();
        torrent_handle th = s.swig().add_torrent(p, ec);
        if (ec.value() != 0) {
            throw new RuntimeException(ec.message());
        }
        return new TorrentHandle(th);
    }

    private void startAlertsLoop() {
        alertsLoop = new Thread(new AlertsLoop(), "SessionManager-alertsLoop");
        alertsLoop.setDaemon(true);
        alertsLoop.start();
    }

    private session createSession(String interfaces, int retries, boolean logging) {
        settings_pack sp = new settings_pack();

        sp.set_str(settings_pack.string_types.listen_interfaces.swigValue(), interfaces);
        sp.set_int(settings_pack.int_types.max_retry_port_bind.swigValue(), retries);
        sp.set_int(settings_pack.int_types.alert_mask.swigValue(), alertMask(logging));

        session s = new session(sp);

        return s;
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
            alert_ptr_vector v = new alert_ptr_vector();

            while (session != null) {
                alert ptr = session.wait_for_alert_ms(ALERTS_LOOP_WAIT_MILLIS);

                if (ptr != null && session != null) {
                    session.pop_alerts(v);
                    int size = (int) v.size();
                    for (int i = 0; i < size; i++) {
                        alert a = v.get(i);

                        Alert alert = Alerts.cast(a);
                        System.out.println(alert);
                    }
                }
            }
        }
    }
}
