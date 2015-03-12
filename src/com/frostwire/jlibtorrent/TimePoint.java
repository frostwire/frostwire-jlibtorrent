package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.high_resolution_clock.time_point;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TimePoint {

    private final time_point tp;

    public TimePoint(time_point tp) {
        this.tp = tp;
    }

    public time_point getSwig() {
        return tp;
    }
}
