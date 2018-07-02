package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_endpoint;

/**
 * Announces are sent to each tracker using every listen socket, this class
 * holds information about one listen socket for one tracker.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AnnounceEndpoint {

    private final announce_endpoint e;

    /**
     * @param e the native object
     */
    public AnnounceEndpoint(announce_endpoint e) {
        this.e = e;
    }

    /**
     * @return the native object
     */
    public announce_endpoint swig() {
        return e;
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return the error or warning message
     */
    public String message() {
        return Vectors.byte_vector2ascii(e.get_message());
    }

    /**
     * If this tracker failed the last time it was contacted
     * this error code specifies what error occurred.
     *
     * @return the last error code
     */
    public ErrorCode lastError() {
        return new ErrorCode(e.getLast_error());
    }

    /**
     * The local endpoint of the listen interface associated with this endpoint.
     *
     * @return the local endpoint
     */
    public TcpEndpoint localEndpoint() {
        return new TcpEndpoint(e.getLocal_endpoint());
    }

    /**
     * The time of next tracker announce in milliseconds.
     *
     * @return the time of next tracker announce in milliseconds
     */
    public long nextAnnounce() {
        return e.get_next_announce();
    }

    /**
     * No announces before this time.
     */
    public long minAnnounce() {
        return e.get_min_announce();
    }

    /**
     * This is either -1 or the scrape information this tracker last
     * responded with. Incomplete is the current number of downloaders in
     * the swarm.
     * <p>
     * If this tracker has returned scrape data, these fields are filled in
     * with valid numbers. Otherwise they are set to -1.
     *
     * @return the number of current downloaders
     */
    public int scrapeIncomplete() {
        return e.getScrape_incomplete();
    }

    /**
     * This is either -1 or the scrape information this tracker last
     * responded with. Complete is the current number of seeds in the swarm.
     * <p>
     * If this tracker has returned scrape data, these fields are filled in
     * with valid numbers. Otherwise they are set to -1.
     *
     * @return the current number of seeds
     */
    public int scrapeComplete() {
        return e.getScrape_complete();
    }

    /**
     * This is either -1 or the scrape information this tracker last
     * responded with. Downloaded is the cumulative number of completed
     * downloads of this torrent, since the beginning of time
     * (from this tracker's point of view).
     * <p>
     * If this tracker has returned scrape data, these fields are filled in
     * with valid numbers. Otherwise they are set to -1.
     *
     * @return the cumulative number of completed downloads
     */
    public int scrapeDownloaded() {
        return e.getScrape_downloaded();
    }

    /**
     * The number of times in a row we have failed to announce to this tracker.
     *
     * @return the number of fails
     */
    public int fails() {
        return e.getFails();
    }

    /**
     * True while we're waiting for a response from the tracker.
     */
    public boolean updating() {
        return e.getUpdating();
    }

    /**
     * Set to true when we get a valid response from an announce
     * with event=started. If it is set, we won't send start in the subsequent
     * announces.
     */
    public boolean startSent() {
        return e.getStart_sent();
    }

    /**
     * Set to true when we send a event=completed.
     */
    public boolean completeSent() {
        return e.getComplete_sent();
    }

    /**
     * Returns true if the last time we tried to announce to this
     * tracker succeeded, or if we haven't tried yet.
     */
    public boolean isWorking() {
        return e.is_working();
    }
}
