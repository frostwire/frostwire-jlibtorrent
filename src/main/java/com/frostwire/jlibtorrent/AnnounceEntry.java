package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.announce_endpoint_vector;
import com.frostwire.jlibtorrent.swig.announce_entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata for a single tracker in a torrent's announce list.
 * <p>
 * {@code AnnounceEntry} represents one tracker used for peer discovery and seedstats.
 * A torrent typically lists multiple trackers organized in "tiers" - the client tries
 * trackers in order of tier (lowest first), and within each tier tries all trackers
 * before moving to the next tier. This provides redundancy and load balancing.
 * <p>
 * <b>Tracker Tiers:</b>
 * <pre>
 * Tier 0 (Primary):    [tracker1.com, tracker2.com]
 * Tier 1 (Secondary):  [tracker3.com, tracker4.com]
 * Tier 2 (Fallback):   [tracker5.com]
 *
 * The client tries all tier-0 trackers first. If they all fail or are slow,
 * it tries tier-1, and so on.
 * </pre>
 * <p>
 * <b>Getting Trackers from a Torrent:</b>
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 *
 * // Get all trackers sorted by tier
 * for (AnnounceEntry tracker : ti.trackers()) {
 *     System.out.println("Tier " + tracker.tier() + ": " + tracker.url());
 * }
 *
 * // Example output:
 * // Tier 0: http://tracker1.example.com:80/announce
 * // Tier 0: udp://tracker2.example.com:6969/announce
 * // Tier 1: http://tracker3.example.com:80/announce
 * </pre>
 * <p>
 * <b>Tracker URL Schemes:</b>
 * <pre>
 * // HTTP tracker
 * "http://tracker.example.com:80/announce"
 *
 * // HTTPS tracker (secure)
 * "https://secure-tracker.example.com:443/announce"
 *
 * // UDP tracker (lightweight)
 * "udp://tracker.example.com:6969/announce"
 *
 * // UDP tracker with IPv6
 * "udp://[2001:db8::1]:6969/announce"
 * </pre>
 * <p>
 * <b>Modifying Tracker Lists:</b>
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 *
 * // Add a tracker to tier 0 (primary)
 * ti.addTracker("http://new-tracker.example.com:80/announce", 0);
 *
 * // Add a fallback tracker to tier 2
 * ti.addTracker("http://fallback.example.com:80/announce", 2);
 *
 * // Remove a tracker (create new list without it)
 * // Note: JlibtorrentAPI doesn't provide direct removal, rebuild the list if needed
 * </pre>
 * <p>
 * <b>Tracker Statistics (Per Session):</b>
 * <pre>
 * AnnounceEntry tracker = ...;
 *
 * // Get current state of this tracker
 * List&lt;AnnounceEndpoint&gt; endpoints = tracker.endpoints();
 * for (AnnounceEndpoint ep : endpoints) {
 *     System.out.println("Endpoint: " + ep.toString());
 * }
 *
 * // Optional tracker ID (for private trackers)
 * String trackerId = tracker.trackerId();
 * if (!trackerId.isEmpty()) {
 *     System.out.println("This is a private tracker, trackerId: " + trackerId);
 * }
 *
 * // Failure handling
 * int failLimit = tracker.failLimit();
 * System.out.println("Max failures before blacklisting: " + failLimit);
 *
 * // Verification status
 * boolean verified = tracker.isVerified();
 * System.out.println("Has received valid response: " + verified);
 * </pre>
 * <p>
 * <b>Tracker Tier Strategy:</b>
 * <pre>
 * // Check and prioritize working trackers
 * List&lt;AnnounceEntry&gt; allTrackers = ti.trackers();
 *
 * // Group by tier
 * Map&lt;Integer, List&lt;AnnounceEntry&gt;&gt; byTier = new TreeMap&lt;&gt;();
 * for (AnnounceEntry tracker : allTrackers) {
 *     byTier.computeIfAbsent(tracker.tier(), k -&gt; new ArrayList&lt;&gt;())
 *           .add(tracker);
 * }
 *
 * // Primary trackers (tier 0)
 * List&lt;AnnounceEntry&gt; primaryTrackers = byTier.get(0);
 * System.out.println("Primary trackers: " + primaryTrackers.size());
 *
 * // Backup trackers (tier > 0)
 * List&lt;AnnounceEntry&gt; backupTrackers = allTrackers.stream()
 *     .filter(t -&gt; t.tier() &gt; 0)
 *     .collect(Collectors.toList());
 * System.out.println("Backup trackers: " + backupTrackers.size());
 * </pre>
 * <p>
 * <b>Private vs Public Torrents:</b>
 * - Public torrents: Use DHT (no private bit set), multiple public trackers
 * - Private torrents: Require tracker (DHT disabled), use tracker-issued tracker IDs
 * <pre>
 * // Check if likely private torrent
 * List&lt;AnnounceEntry&gt; trackers = ti.trackers();
 * boolean hasPrivateTracker = trackers.stream()
 *     .anyMatch(t -&gt; !t.trackerId().isEmpty());
 * System.out.println("Likely private torrent: " + hasPrivateTracker);
 * </pre>
 *
 * @see TorrentInfo#trackers() - Get all trackers from a torrent
 * @see TorrentInfo#addTracker(String, int) - Add trackers
 * @see AnnounceEndpoint - Per-endpoint tracker state
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
