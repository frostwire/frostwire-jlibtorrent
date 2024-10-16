package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_endpoint_vector;
import com.frostwire.jlibtorrent.swig.announce_entry;

import java.util.ArrayList;
import java.util.List;

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

    public List<AnnounceEndpoint> endpoints() {
        announce_endpoint_vector v = e.getEndpoints();
        int size = (int) v.size();
        ArrayList<AnnounceEndpoint> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new AnnounceEndpoint(v.get(i)));
        }

        return l;
    }

    /**
     * Tracker URL as it appeared in the torrent file.
     *
     * @return the tracker url
     */
    public String url() {
        return Vectors.byte_vector2ascii(e.get_url());
    }

    public void url(String value) {
        e.set_url(Vectors.ascii2byte_vector(value));
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

    public void trackerId(String value) {
        e.set_trackerid(Vectors.ascii2byte_vector(value));
    }

    /**
     * The tier this tracker belongs to.
     *
     * @return the tier number
     */
    public int tier() {
        return e.getTier();
    }

    public void tier(short value) {
        e.setTier(value);
    }

    /**
     * The max number of failures to announce to this tracker in
     * a row, before this tracker is not used anymore. 0 means unlimited.
     *
     * @return the max number of failures allowed
     */
    public int failLimit() {
        return e.getFail_limit();
    }

    public void failLimit(short value) {
        e.setFail_limit(value);
    }

    /**
     * A bitmask specifying which sources we got this tracker from.
     *
     * @return the source bitmask
     */
    public int source() {
        return e.getSource();
    }

    /**
     * Set to true the first time we receive a valid response
     * from this tracker.
     *
     * @return if the tracker has received a valid response
     */
    public boolean isVerified() {
        return e.getVerified();
    }
}
