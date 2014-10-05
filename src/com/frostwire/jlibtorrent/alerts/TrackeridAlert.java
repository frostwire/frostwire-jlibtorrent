package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.trackerid_alert;

/**
 * This alert is posted whenever a tracker responds with a ``trackerid``.
 * The tracker ID is like a cookie. The libtorrent will store the tracker ID
 * for this tracker and repeat it in subsequent announces.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackeridAlert extends TorrentAlert<trackerid_alert> {

    public TrackeridAlert(trackerid_alert alert) {
        super(alert);
    }

    public String getTrackerId() {
        return alert.getTrackerid();
    }
}
