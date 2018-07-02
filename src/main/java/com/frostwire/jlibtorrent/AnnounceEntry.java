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
        this(new announce_entry(Vectors.ascii2byte_vector(url)));
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
        return Vectors.byte_vector2ascii(e.get_url());
    }

    public void url(String s) {
        e.set_url(Vectors.ascii2byte_vector(s));
    }

    /**
     * The current {@code &trackerid=} argument passed to the tracker.
     * this is optional and is normally empty (in which case no
     * trackerid is sent).
     *
     * @return the trackerid url argument
     */
    public String trackerId() {
        return Vectors.byte_vector2ascii(e.get_trackerid());
    }

    public void trackerId(String s) {
        e.set_trackerid(Vectors.ascii2byte_vector(s));
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
