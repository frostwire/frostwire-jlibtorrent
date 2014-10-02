package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_alert;

/**
 * This is a base class used for alerts that are associated with a
 * specific tracker. It derives from torrent_alert since a tracker
 * is also associated with a specific torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public abstract class TrackerAlert<T extends tracker_alert> extends TorrentAlert<T> {

    public TrackerAlert(T alert) {
        super(alert);
    }

    public String getUrl() {
        return alert.getUrl();
    }
}
