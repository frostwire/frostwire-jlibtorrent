package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_announce_alert;

/**
 * This alert is generated each time a tracker announce is sent (or attempted to be sent).
 * <p/>
 * There are no extra data members in this alert. The url can be found in the base class
 * however.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackerAnnounceAlert extends TrackerAlert<tracker_announce_alert> {

    public TrackerAnnounceAlert(tracker_announce_alert alert) {
        super(alert);
    }

    // specifies what event was sent to the tracker. It is defined as:
    //
    // 0. None
    // 1. Completed
    // 2. Started
    // 3. Stopped
    public int getEvent() {
        return alert.getEvent();
    }
}
