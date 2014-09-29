package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.time_duration;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TimeDuration {

    private final time_duration td;

    public TimeDuration(time_duration td) {
        this.td = td;
    }

    public time_duration getSwig() {
        return td;
    }
}
