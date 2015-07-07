package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.posix_time_duration;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PosixTimeDuration {

    private final posix_time_duration td;

    public PosixTimeDuration(posix_time_duration td) {
        this.td = td;
    }

    public posix_time_duration getSwig() {
        return td;
    }
}
