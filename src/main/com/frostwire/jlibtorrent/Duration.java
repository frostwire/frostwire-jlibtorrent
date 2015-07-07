package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.high_resolution_clock.duration;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Duration {

    private final duration d;

    public Duration(duration d) {
        this.d = d;
    }

    public duration getSwig() {
        return d;
    }
}
