package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.ptime;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TimePoint {

    private final ptime tp;

    public TimePoint(ptime tp) {
        this.tp = tp;
    }

    public ptime getSwig() {
        return tp;
    }
}
