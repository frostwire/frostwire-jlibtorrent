package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_entry;

/**
 * This class holds information about one bittorrent tracker, as it
 * relates to a specific torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceEntry {

    private final announce_entry e;

    public AnnounceEntry(announce_entry e) {
        this.e = e;
    }

    public AnnounceEntry(String url) {
        this(new announce_entry(url));
    }

    public announce_entry getSwig() {
        return e;
    }

    /**
     * Tracker URL as it appeared in the torrent file.
     *
     * @return
     */
    public String getUrl() {
        return e.getUrl();
    }
}
