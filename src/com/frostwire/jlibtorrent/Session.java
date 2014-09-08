package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.List;

public class Session {

    // public only to test
    public final session s;

    public Session() {
        this.s = new session();

        init();
    }

    public List<alert> waitForAlerts(int millis) {
        SWIGTYPE_p_boost__posix_time__time_duration max_wait = libtorrent.milliseconds(millis);
        alert ptr = s.wait_for_alert(max_wait);

        alert_ptr_deque deque = new alert_ptr_deque();
        if (ptr != null) {
            s.pop_alerts(deque);
        }

        List<alert> alerts = new ArrayList<alert>((int) deque.size());

        for (int i = 0; i < deque.size(); i++) {
            alert a = deque.getitem(i);
            a = cast_alert(a);
            if (a != null) {
                alerts.add(a);
            } else {
                System.out.println("Alert type not supported: " + a.what() + " - " + a.category() + " - " + a.message());
            }
        }

        return alerts;
    }

    private void init() {
        s.set_alert_mask(alert.category_t.all_categories.swigValue());

        int_int_pair pr = new int_int_pair(6881, 6889);
        error_code ec = new error_code();
        s.listen_on(pr, ec);
    }

    private alert cast_alert(alert a) {
        alert r = null;

        r = alert.cast_to_dht_announce_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_put_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_external_ip_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_listen_failed_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_state_update_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_portmap_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_torrent_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_rss_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_bootstrap_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_get_peers_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_incoming_connection_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_i2p_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_mutable_item_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_immutable_item_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_udp_error_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_portmap_error_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_portmap_log_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_rss_item_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_listen_succeeded_alert(a);
        if (r != null) {
            return r;
        }

        r = alert.cast_to_dht_error_alert(a);
        if (r != null) {
            return r;
        }

        return r;
    }
}
