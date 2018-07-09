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

    private final String message;
    private final ErrorCode lastError;
    private final String localEndpoint;
    private final long nextAnnounce;
    private final long minAnnounce;
    private final int scrapeIncomplete;
    private final int scrapeComplete;
    private final int scrapeDownloaded;
    private final int fails;
    private final boolean updating;
    private final boolean isWorking;

    /**
     * @param e the native object
     */
    AnnounceEndpoint(announce_endpoint e) {
        this.message = Vectors.byte_vector2ascii(e.get_message());
        this.lastError = new ErrorCode(e.getLast_error());
        this.localEndpoint = new TcpEndpoint(e.getLocal_endpoint()).toString();
        this.nextAnnounce = e.get_next_announce();
        this.minAnnounce = e.get_min_announce();
        this.scrapeIncomplete = e.getScrape_incomplete();
        this.scrapeComplete = e.getScrape_complete();
        this.scrapeDownloaded = e.getScrape_downloaded();
        this.fails = e.getFails();
        this.updating = e.getUpdating();
        this.isWorking = e.is_working();
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return the error or warning message
     */
    public String message() {
        return message;
    }

    /**
     * If this tracker failed the last time it was contacted
     * this error code specifies what error occurred.
     *
     * @return the last error code
     */
    public ErrorCode lastError() {
        return lastError;
    }

    /**
     * The local endpoint of the listen interface associated with this endpoint.
     *
     * @return the local endpoint
     */
    public String localEndpoint() {
        return localEndpoint;
    }

    /**
     * The time of next tracker announce in milliseconds.
     *
     * @return the time of next tracker announce in milliseconds
     */
    public long nextAnnounce() {
        return nextAnnounce;
    }

    /**
     * No announces before this time.
     */
    public long minAnnounce() {
        return minAnnounce;
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
        return scrapeIncomplete;
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
        return scrapeComplete;
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
        return scrapeDownloaded;
    }

    /**
     * The number of times in a row we have failed to announce to this tracker.
     *
     * @return the number of fails
     */
    public int fails() {
        return fails;
    }

    /**
     * True while we're waiting for a response from the tracker.
     */
    public boolean updating() {
        return updating;
    }

    /**
     * Returns true if the last time we tried to announce to this
     * tracker succeeded, or if we haven't tried yet.
     */
    public boolean isWorking() {
        return isWorking;
    }
}
