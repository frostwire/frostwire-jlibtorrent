package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.swig.state_update_alert;
import com.frostwire.jlibtorrent.swig.torrent_status_vector;

import java.util.ArrayList;
import java.util.List;

/**
 * This alert is only posted when requested by the user, by calling session::post_torrent_updates()
 * on the session. It contains the torrent status of all torrents that changed
 * since last time this message was posted. Its category is ``status_notification``, but
 * it's not subject to filtering, since it's only manually posted anyway.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StateUpdateAlert extends AbstractAlert<state_update_alert> {

    public StateUpdateAlert(state_update_alert alert) {
        super(alert);
    }

    /**
     * contains the torrent status of all torrents that changed since last time
     * this message was posted. Note that you can map a torrent status to a specific torrent
     * via its ``handle`` member. The receiving end is suggested to have all torrents sorted
     * by the torrent_handle or hashed by it, for efficient updates.
     *
     * @return
     */
    public List<TorrentStatus> getStatus() {
        torrent_status_vector v = alert.getStatus();
        int size = (int) v.size();

        List<TorrentStatus> l = new ArrayList<TorrentStatus>(size);
        for (int i = 0; i < size; i++) {
            l.add(new TorrentStatus(v.get(i)));
        }

        return l;
    }
}
