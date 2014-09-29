package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.ptime;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PTime {

    private final ptime pt;

    public PTime(ptime pt) {
        this.pt = pt;
    }

    public ptime getSwig() {
        return pt;
    }
}
