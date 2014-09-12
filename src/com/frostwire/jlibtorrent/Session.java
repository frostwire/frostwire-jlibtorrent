package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import com.frostwire.jlibtorrent.swig.session.options_t;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Session {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private final session s;

    public Session() {
        this.s = new session();

        s.set_alert_mask(alert.category_t.all_categories.swigValue());

        int_int_pair pr = new int_int_pair(6881, 6889);
        error_code ec = new error_code();
        s.listen_on(pr, ec);
    }

    public session getSwig() {
        return s;
    }

    /**
     * You add torrents through the add_torrent() function where you give an object with all the parameters. The add_torrent() overloads will block until the torrent has been added (or failed to be added) and returns an error code and a torrent_handle. In order to add torrents more efficiently, consider using async_add_torrent() which returns immediately, without waiting for the torrent to add. Notification of the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on error and is not available when building without exception support. The torrent_handle returned by add_torrent() can be used to retrieve information about the torrent's progress, its peers etc. It is also used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is either queued for checking, being checked or downloading) add_torrent() will throw libtorrent_exception which derives from std::exception unless duplicate_is_error is set to false. In that case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrentFile
     * @param saveDir
     * @return
     */
    public TorrentHandle addTorrent(File torrentFile, File saveDir) {
        TorrentInfo ti = new TorrentInfo(torrentFile);

        add_torrent_params p = new add_torrent_params();
        p.setSave_path(saveDir.getAbsolutePath());
        p.setTi(ti.getSwig());
        torrent_handle th = s.add_torrent(p);
        th.auto_managed(false);

        return new TorrentHandle(th, torrentFile);
    }

    /**
     * This method will close all peer connections associated with the torrent and tell the
     * tracker that we've stopped participating in the swarm. This operation cannot fail.
     * When it completes, you will receive a torrent_removed_alert.
     * <p/>
     * The optional second argument options can be used to delete all the files downloaded
     * by this torrent. To do so, pass in the value session::delete_files. The removal of
     * the torrent is asyncronous, there is no guarantee that adding the same torrent immediately
     * after it was removed will not throw a libtorrent_exception exception. Once the torrent
     * is deleted, a torrent_deleted_alert is posted.
     *
     * @param th
     */
    public void removeTorrent(TorrentHandle th, Options options) {
        s.remove_torrent(th.getSwig(), Options.toSwig(options));
    }

    /**
     * This method will close all peer connections associated with the torrent and tell the
     * tracker that we've stopped participating in the swarm. This operation cannot fail.
     * When it completes, you will receive a torrent_removed_alert.
     *
     * @param th
     */
    public void removeTorrent(TorrentHandle th) {
        this.removeTorrent(th, Options.NONE);
    }

    /**
     * Blocks until an alert is available, or for no more than max_wait time. If wait_for_alert returns
     * because of the time-out, and no alerts are available, it returns 0. If at least one alert
     * was generated, a pointer to that alert is returned. The alert is not popped, any subsequent calls
     * to wait_for_alert will return the same pointer until the alert is popped by calling pop_alert.
     * This is useful for leaving any alert dispatching mechanism independent of this blocking call, the
     * dispatcher can be called and it can pop the alert independently.
     *
     * @param millis
     * @return
     */
    public List<alert> waitForAlerts(long millis) {
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

        // this needs an urgent refactor
        r = alert.cast_to_save_resume_data_alert(a);
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

        r = a;

        return r;
    }

    public enum Options {

        NONE,
        DELETE_FILES;

        public static Options fromSwig(options_t opts) {
            switch (opts) {
                case delete_files:
                    return DELETE_FILES;
                default:
                    throw new IllegalArgumentException("Enum value not supported");
            }
        }

        public static int toSwig(Options opts) {
            switch (opts) {
                case NONE:
                    return 0;
                case DELETE_FILES:
                    return options_t.delete_files.swigValue();
                default:
                    throw new IllegalArgumentException("Enum value not supported");
            }
        }
    }
}
