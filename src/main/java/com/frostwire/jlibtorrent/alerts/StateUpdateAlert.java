package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.SessionHandle;
import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.swig.state_update_alert;
import com.frostwire.jlibtorrent.swig.torrent_status_vector;

import java.util.ArrayList;
import java.util.List;

/**
 * This alert is only posted when requested by the user, by calling
 * {@link SessionHandle#postTorrentUpdates()}. It contains the torrent status of
 * all torrents that changed since last time this message was posted. Its category
 * is {@code status_notification}, but it's not subject to filtering, since it's only
 * manually posted anyway.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StateUpdateAlert extends AbstractAlert<state_update_alert> {

    StateUpdateAlert(state_update_alert alert) {
        super(alert);
    }

    /**
     * Contains the torrent status of all torrents that changed since last time
     * this message was posted. Note that you can map a torrent status to a
     * specific torrent via its {@code handle} member.
     *
     * @return the list of torrent status
     */
    public List<TorrentStatus> status() {
        torrent_status_vector v = alert.getStatus();
        int size = (int) v.size();

        List<TorrentStatus> l = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            l.add(new TorrentStatus(v.get(i)));
        }

        return l;
    }
}
