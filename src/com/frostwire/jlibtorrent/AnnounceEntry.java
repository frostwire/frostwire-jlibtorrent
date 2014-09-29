package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_entry;

/**
 * This class holds information about one bittorrent tracker, as it
 * relates to a specific torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AnnounceEntry {

    private final announce_entry e;

    public AnnounceEntry(announce_entry e) {
        this.e = e;
    }

    /**
     * Constructs a tracker announce entry with ``u`` as the URL.
     *
     * @param url
     */
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

    /**
     * The current ``&trackerid=`` argument passed to the tracker.
     * this is optional and is normally empty (in which case no
     * trackerid is sent).
     *
     * @return
     */
    public String getTrackerId() {
        return e.getTrackerid();
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return
     */
    public String getMessage() {
        return e.getMessage();
    }
}
