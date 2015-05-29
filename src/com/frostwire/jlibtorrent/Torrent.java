package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent;

/**
 * a torrent is a class that holds information
 * // for a specific download. It updates itself against
 * // the tracker
 *
 * @author gubatron
 * @author aldenml
 */
public final class Torrent {

    private final torrent t;

    public Torrent(torrent t) {
        this.t = t;
    }

    public torrent getSwig() {
        return t;
    }

    boolean isAborted() {
        return t.is_aborted();
    }

    public int queuePosition() {
        return t.queue_position();
    }
}
