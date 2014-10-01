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

    /**
     * Specifies what event was sent to the tracker.
     *
     * @return
     */
    public TrackerAnnounceEvent getEvent() {
        return TrackerAnnounceEvent.fromSwig(alert.getEvent());
    }

    public enum TrackerAnnounceEvent {

        NONE(0),

        COMPLETED(1),

        STARTED(2),

        STOPPED(3),

        UNKNOWN(-1);

        private TrackerAnnounceEvent(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static TrackerAnnounceEvent fromSwig(int swigValue) {
            TrackerAnnounceEvent[] enumValues = TrackerAnnounceEvent.class.getEnumConstants();
            for (TrackerAnnounceEvent ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
