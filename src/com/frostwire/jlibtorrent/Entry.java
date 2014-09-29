package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;

/**
 * The ``entry`` class represents one node in a bencoded hierarchy. It works as a
 * variant type, it can be either a list, a dictionary (``std::map``), an integer
 * or a string.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Entry {

    private final entry e;

    public Entry(entry e) {
        this.e = e;
    }

    public entry getSwig() {
        return e;
    }
}
