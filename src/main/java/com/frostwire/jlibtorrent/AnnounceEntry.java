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
     * Constructs a tracker announce entry with {@code u} as the URL.
     *
     * @param url
     */
    public AnnounceEntry(String url) {
        this(new announce_entry(url));
    }

    public announce_entry swig() {
        return e;
    }

    /**
     * Tracker URL as it appeared in the torrent file.
     *
     * @return
     */
    public String url() {
        return e.getUrl();
    }

    /**
     * The current ``&trackerid=`` argument passed to the tracker.
     * this is optional and is normally empty (in which case no
     * trackerid is sent).
     *
     * @return
     */
    public String trackerId() {
        return e.getTrackerid();
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return
     */
    public String message() {
        return e.getMessage();
    }

    /**
     * if this tracker failed the last time it was contacted
     * this error code specifies what error occurred.
     *
     * @return
     */
    public ErrorCode lastError() {
        return new ErrorCode(e.getLast_error());
    }

    /**
     * The tier this tracker belongs to.
     *
     * @return
     */
    public short tier() {
        return e.getTier();
    }
    
    public Status status() {
        if (e.getVerified() && e.is_working()) {
            return Status.WORKING;
        } else if ((e.getFails() == 0) && e.getUpdating()) {
            return Status.UPDATING;
        } else if (e.getFails() == 0) {
            return Status.NOT_CONTACTED;
        } else {
            return Status.NOT_WORKING;
        }
    }
    
    public enum Status {
        NOT_CONTACTED,
        WORKING,
        UPDATING,
        NOT_WORKING
    }
}
