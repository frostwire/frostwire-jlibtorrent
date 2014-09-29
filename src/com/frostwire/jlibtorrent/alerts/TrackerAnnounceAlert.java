package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TrackerAnnounceEvent;
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

    /**
     * Specifies what event was sent to the tracker.
     *
     * @return
     */
    public TrackerAnnounceEvent getEvent() {
        return TrackerAnnounceEvent.fromSwig(alert.getEvent());
    }
}
