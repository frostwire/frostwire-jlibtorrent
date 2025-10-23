package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_infohash;

/**
 * Announcement status for a specific info-hash at a tracker endpoint.
 * <p>
 * {@code AnnounceInfohash} tracks the status of an individual info-hash (torrent) being
 * announced to a specific tracker. It records the tracker's response, error messages,
 * failure count, and whether an announcement is currently in progress for that info-hash.
 * <p>
 * <b>Understanding Announce Status:</b>
 * <br/>
 * BitTorrent trackers require periodic announcements with torrent metadata. Multiple
 * info-hashes may be announced to the same tracker through different endpoints:
 * <ul>
 *   <li><b>Message:</b> Last error or warning message from tracker</li>
 *   <li><b>Fails:</b> Count of consecutive failed announcements</li>
 *   <li><b>Updating:</b> Whether announcement is currently in progress</li>
 *   <li><b>Working:</b> Whether the tracker is responding successfully</li>
 * </ul>
 * <p>
 * <b>Monitoring Announce Status:</b>
 * <pre>
 * // Get tracker status for a torrent
 * TorrentHandle th = sm.find(infoHash);
 * TorrentStatus status = th.status();
 *
 * // Access announce info-hashes for trackers
 * java.util.List&lt;AnnounceEndpoint&gt; endpoints = status.announceEndpoints();
 *
 * for (AnnounceEndpoint endpoint : endpoints) {
 *     // Each endpoint has info-hashes being announced
 *     java.util.List&lt;AnnounceInfohash&gt; infoHashes = endpoint.infoHashes();
 *
 *     for (AnnounceInfohash ih : infoHashes) {
 *         System.out.println(\"Announce Status:\");
 *         System.out.println(\"  Updating: \" + ih.updating());
 *         System.out.println(\"  Fails: \" + ih.fails());
 *         System.out.println(\"  Working: \" + ih.isWorking());
 *         if (!ih.message().isEmpty()) {
 *             System.out.println(\"  Message: \" + ih.message());
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * <b>Interpreting Announcement States:</b>
 * <pre>
 * AnnounceInfohash ih = ...;
 *
 * // Is an announcement currently being sent?
 * if (ih.updating()) {
 *     System.out.println(\"Announcement in progress...\");
 * }
 *
 * // Has it succeeded?
 * if (ih.isWorking()) {
 *     System.out.println(\"Tracker is responding correctly\");
 *     System.out.println(\"Consecutive failures: 0\");
 * } else {
 *     // Count indicates how many times announcement failed
 *     System.out.println(\"Tracker failed \" + ih.fails() + \" times in a row\");
 *
 *     // Check for error message
 *     String msg = ih.message();
 *     if (!msg.isEmpty()) {
 *         System.out.println(\"Error: \" + msg);
 *     }
 * }
 * </pre>
 * <p>
 * <b>Failure and Recovery:</b>
 * <pre>
 * // Track consecutive failures
 * AnnounceInfohash ih = ...;
 *
 * short failCount = ih.fails();\n * if (failCount == 0) {
 *     // Tracker working normally
 *     System.out.println(\"OK\");
 * } else if (failCount &lt; 5) {
 *     // Transient failures, may recover
 *     System.out.println(\"Tracker unstable: \" + failCount + \" failures\");
 * } else {
 *     // Many failures, likely connectivity or tracker issue
 *     System.out.println(\"Tracker appears down: \" + failCount + \" failures\");
 *     System.out.println(\"Last message: \" + ih.message());
 * }
 * </pre>
 * <p>
 * <b>Tracker Endpoint Structure:</b>
 * <p>
 * Trackers are organized hierarchically:
 * <ul>
 *   <li><b>Tracker URL</b> → <b>Endpoints</b> (IP/port combinations)</li>
 *   <li><b>Endpoint</b> → <b>Info-hashes</b> (torrents being announced)</li>
 *   <li><b>AnnounceInfohash</b> → Status of single info-hash at endpoint</li>
 * </ul>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Failure counter resets on successful announcement</li>
 *   <li>Multiple info-hashes may share same tracker endpoint connection</li>
 *   <li>Messages indicate tracker-returned warnings or errors</li>
 *   <li>Status is updated continuously by libtorrent's announce thread</li>
 * </ul>
 *
 * @see AnnounceEndpoint - Container for announce info-hashes
 * @see TorrentStatus#announceEndpoints() - Get announce endpoints
 * @see TorrentHandle#status() - Query torrent status
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceInfohash {
    private final announce_infohash swig;

    public AnnounceInfohash(announce_infohash infohash) {
        swig = infohash;
    }

    /**
     * If this tracker has returned an error or warning message
     * that message is stored here.
     *
     * @return the error or warning message
     */
    public String message() {
        return swig.getMessage();
    }

    /**
     * The number of times in a row we have failed to announce to this
     * tracker.
     *
     * @return number of announce fails
     */
    public short fails() {
        return swig.getFails();
    }

    /**
     * Returns true while we're waiting for a response from the tracker.
     *
     * @return true if waiting
     */
    public boolean updating() {
        return swig.getUpdating();
    }

    /**
     * Returns true if the last time we tried to announce to this
     * tracker succeeded, or if we haven't tried yet.
     */
    public boolean isWorking() {
        return swig.getFails() == 0;
    }
}
