package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_reply_alert;

/**
 * This alert is only for informational purpose. It is generated when a tracker announce
 * succeeds. It is generated regardless what kind of tracker was used, be it UDP, HTTP or
 * the DHT.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackerReplyAlert extends TrackerAlert<tracker_reply_alert> {

    public TrackerReplyAlert(tracker_reply_alert alert) {
        super(alert);
    }

    /**
     * tells how many peers the tracker returned in this response. This is
     * not expected to be more thant the ``num_want`` settings. These are not necessarily
     * all new peers, some of them may already be connected.
     *
     * @return
     */
    public int getNumPeers() {
        return alert.getNum_peers();
    }
}
