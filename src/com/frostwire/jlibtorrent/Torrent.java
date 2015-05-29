package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_torrent_params;

/**
 * a torrent is a class that holds information
 * // for a specific download. It updates itself against
 * // the tracker
 *
 * @author gubatron
 * @author aldenml
 */
public final class Torrent {

    private final add_torrent_params p;

    public Torrent(add_torrent_params p) {
        this.p = p;
    }

    public add_torrent_params getSwig() {
        return p;
    }
}
