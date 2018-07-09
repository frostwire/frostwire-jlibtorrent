package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_endpoint;

/**
 * Announces are sent to each tracker using every listen socket, this class
 * holds information about one listen socket for one tracker.
 * <p>
 * This class is a lightweight version of the native {@link announce_endpoint},
 * and only carries a subset of all the information. However, it's completely
 * open for custom use or optimization to accommodate client necessities.
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceEndpoint {

    protected String message;
    protected ErrorCode lastError;
    protected String localEndpoint;
    protected long nextAnnounce;
    protected long minAnnounce;
    protected int scrapeIncomplete;
    protected int scrapeComplete;
    protected int scrapeDownloaded;
    protected int fails;
    protected boolean updating;
    protected boolean isWorking;

    /**
     * @param e the native object
     */
    public AnnounceEndpoint(announce_endpoint e) {
        init(e);
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

    /**
     * NOTE: use this with care and only if necessary.
     *
     * @param e the native object
     */
    protected void init(announce_endpoint e) {
        message = Vectors.byte_vector2ascii(e.get_message());
        lastError = new ErrorCode(e.getLast_error());
        localEndpoint = new TcpEndpoint(e.getLocal_endpoint()).toString();
        nextAnnounce = e.get_next_announce();
        minAnnounce = e.get_min_announce();
        scrapeIncomplete = e.getScrape_incomplete();
        scrapeComplete = e.getScrape_complete();
        scrapeDownloaded = e.getScrape_downloaded();
        fails = e.getFails();
        updating = e.getUpdating();
        isWorking = e.is_working();
    }
}
