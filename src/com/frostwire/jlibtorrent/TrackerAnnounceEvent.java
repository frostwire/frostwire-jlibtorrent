package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
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
