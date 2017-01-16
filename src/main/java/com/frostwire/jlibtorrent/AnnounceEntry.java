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

    /**
     * @param e the native object
     */
    public AnnounceEntry(announce_entry e) {
        this.e = e;
    }

    /**
     * Constructs a tracker announce entry with {@code u} as the URL.
     *
     * @param url the tracker url
     */
    public AnnounceEntry(String url) {
        this(new announce_entry(url));
    }

    /**
     * @return the native object
     */
    public announce_entry swig() {
        return e;
    }

    /**
     * Tracker URL as it appeared in the torrent file.
     *
     * @return the tracker url
     */
    public String url() {
        return e.getUrl();
    }

    /**
     * The current {@code &trackerid=} argument passed to the tracker.
     * this is optional and is normally empty (in which case no
     * trackerid is sent).
     *
     * @return the trackerid url argument
     */
    public String trackerId() {
        return e.getTrackerid();
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return the error message
     */
    public String message() {
        return e.getMessage();
    }

    /**
     * If this tracker failed the last time it was contacted
     * this error code specifies what error occurred.
     *
     * @return the error
     */
    public ErrorCode lastError() {
        return new ErrorCode(e.getLast_error());
    }

    /**
     * The tier this tracker belongs to.
     *
     * @return the tier number
     */
    public int tier() {
        return e.getTier();
    }
}
