package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_info;

/**
 * Statistics and information for a single connected peer.
 * <p>
 * {@code PeerInfo} provides a snapshot of statistics for a peer you're connected to
 * while downloading a torrent. It includes data transfer rates, protocol information,
 * peer identification, connection state, and download progress.
 * <p>
 * <b>Getting Peer Information:</b>
 * <pre>
 * TorrentHandle th = ...;  // Get from download
 *
 * // Get list of all peers we're connected to
 * List&lt;PeerInfo&gt; peers = th.peerInfo();
 *
 * System.out.println("Connected to " + peers.size() + " peers");
 *
 * for (PeerInfo peer : peers) {
 *     System.out.println("Peer: " + peer.ip());
 *     System.out.println("  Client: " + peer.client());
 *     System.out.println("  Progress: " + (peer.progress() * 100) + "%");
 *     System.out.println("  Download speed: " + (peer.downSpeed() / 1024) + " KB/s");
 *     System.out.println("  Upload speed: " + (peer.upSpeed() / 1024) + " KB/s");
 *     System.out.println("  Total downloaded: " + peer.totalDownload() + " bytes");
 *     System.out.println("  Total uploaded: " + peer.totalUpload() + " bytes");
 * }
 * </pre>
 * <p>
 * <b>Peer Statistics and Metrics:</b>
 * <pre>
 * for (PeerInfo peer : peers) {
 *     // Transfer statistics (payload only, no protocol overhead)
 *     long downloaded = peer.totalDownload();  // Bytes from this peer
 *     long uploaded = peer.totalUpload();      // Bytes to this peer
 *
 *     // Current speeds (updated ~1x per second)
 *     int downBps = peer.downSpeed();  // Bytes per second
 *     int upBps = peer.upSpeed();      // Bytes per second
 *
 *     // Progress: how much of the torrent does this peer have?
 *     float progress = peer.progress();  // 0.0 to 1.0
 *     float progressPpm = peer.progressPpm();  // Parts per million (more precise)
 *     System.out.println("Peer has " + (progress * 100) + "% of torrent");
 *
 *     // Peer identification
 *     String ip = peer.ip();           // IP address:port
 *     String client = peer.client();   // Client software version
 *     System.out.println(ip + " running " + client);
 * }
 * </pre>
 * <p>
 * <b>Peer Connection Information:</b>
 * <pre>
 * for (PeerInfo peer : peers) {
 *     // Connection state flags
 *     int flags = peer.flags();
 *     // Flags indicate: chocking state, optimistic unchoke, snubbed, etc.
 *
 *     // Where did we learn about this peer?
 *     byte source = peer.source();
 *     // Bit flags: DHT, PEX (peer exchange), tracker, etc.
 *
 *     // Connection type
 *     ConnectionType type = peer.connectionType();
 *     System.out.println("Connection: " + type);  // e.g., STANDARD_BEP
 * }
 * </pre>
 * <p>
 * <b>Analyzing Peer Performance:</b>
 * <pre>
 * // Find fastest peers
 * List&lt;PeerInfo&gt; peers = th.peerInfo();
 *
 * // Sort by download speed
 * List&lt;PeerInfo&gt; fastestPeers = peers.stream()
 *     .sorted((a, b) -&gt; Integer.compare(b.downSpeed(), a.downSpeed()))
 *     .limit(5)
 *     .collect(Collectors.toList());
 *
 * System.out.println("Top 5 fastest peers:");
 * for (PeerInfo peer : fastestPeers) {
 *     System.out.println("  " + peer.ip() + " @ " +
 *         (peer.downSpeed() / 1024 / 1024) + " MB/s");
 * }
 *
 * // Find peers with most progress
 * List&lt;PeerInfo&gt; completePeers = peers.stream()
 *     .filter(p -&gt; p.progress() &gt; 0.99)  // 99%+ complete
 *     .collect(Collectors.toList());
 *
 * System.out.println("Seeders (complete): " + completePeers.size());
 * </pre>
 * <p>
 * <b>Peer Quality Assessment:</b>
 * <pre>
 * for (PeerInfo peer : peers) {
 *     // Assess peer quality
 *     int downBps = peer.downSpeed();
 *     float progress = peer.progress();
 *     long totalDownloaded = peer.totalDownload();
 *
 *     // Peers that have sent us data are valuable
 *     boolean useful = totalDownloaded &gt; 0;
 *
 *     // Complete peers (seeders) are especially valuable
 *     boolean isSeeder = progress &gt; 0.99;
 *
 *     // Fast peers should be prioritized
 *     boolean isFast = downBps &gt; (1024 * 100);  // &gt; 100 KB/s
 *
 *     if (isSeeder &amp;&amp; isFast) {
 *         System.out.println("Premium seeder: " + peer.ip());
 *     }
 * }
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * - {@link TorrentHandle#peerInfo()} is a synchronous call (may block slightly)
 * - Call infrequently or cache results if performance is critical
 * - Updated approximately once per second by libtorrent
 * - For efficient batch updates, use {@link SessionManager#postTorrentUpdates()}
 *
 * @see TorrentHandle#peerInfo() - Get peer list from a torrent
 * @see TorrentStatus - For overall torrent statistics
 * @see ConnectionType - For peer connection type details
 *
 * @author gubatron
 * @author aldenml
 */
public class PeerInfo {

    protected String client;
    protected long totalDownload;
    protected long totalUpload;
    protected int flags;
    protected byte source;
    protected int upSpeed;
    protected int downSpeed;
    protected ConnectionType connectionType;
    protected float progress;
    protected int progressPpm;
    protected String ip;

    public PeerInfo(peer_info p) {
        init(p);
    }

    /**
     * This describes the software at the other end of the connection.
     * In some cases this information is not available, then it will contain
     * a string that may give away something about which software is running
     * in the other end. In the case of a web seed, the server type and
     * version will be a part of this string.
     *
     * @return the client string
     */
    public String client() {
        return client;
    }

    /**
     * The total number of bytes downloaded from this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes downloaded
     */
    public long totalDownload() {
        return totalDownload;
    }

    /**
     * The total number of bytes uploaded to this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes uploaded
     */
    public long totalUpload() {
        return totalUpload;
    }

    /**
     * Tells you in which state the peer is in. It is set to
     * any combination of the peer_flags_t flags.
     *
     * @return the flags as an integer
     */
    public int flags() {
        return flags;
    }

    /**
     * A combination of flags describing from which sources this peer
     * was received. A combination of the peer_source_flags_t flags.
     *
     * @return the flags as a byte
     */
    public byte source() {
        return source;
    }

    /**
     * The current upload speed we have to and from this peer
     * (including any protocol messages). Updated about once per second
     *
     * @return current upload speed we have to and from this peer
     */
    public int upSpeed() {
        return upSpeed;
    }

    /**
     * The current download speed we have to and from this peer
     * (including any protocol messages). Updated about once per second
     *
     * @return current download speed we have to and from this peer
     */
    public int downSpeed() {
        return downSpeed;
    }

    /**
     * The kind of connection this peer uses.
     *
     * @return the connection type
     */
    public ConnectionType connectionType() {
        return connectionType;
    }

    /**
     * The progress of the peer in the range [0, 1]. This is always 0 when
     * floating point operations are disabled, instead use ``progress_ppm``.
     *
     * @return the progress of the peer in the range [0, 1]
     */
    public float progress() {
        return progress;
    }

    /**
     * Indicates the download progress of the peer in the range [0, 1000000]
     * (parts per million).
     *
     * @return the download progress of the peer in the range [0, 1000000]
     */
    public int progressPpm() {
        return progressPpm;
    }

    /**
     * The IP-address to this peer.
     *
     * @return a string representing the endpoint.
     */
    public String ip() {
        return ip;
    }

    /**
     * NOTE: use this with care and only if necessary.
     *
     * @param p the native object
     */
    protected void init(peer_info p) {
        client = Vectors.byte_vector2utf8(p.get_client());
        totalDownload = p.getTotal_download();
        totalUpload = p.getTotal_upload();
        flags = p.get_flags();
        source = p.get_source();
        upSpeed = p.getUp_speed();
        downSpeed = p.getDown_speed();
        connectionType = ConnectionType.fromSwig(p.getConnection_type().to_int());
        progress = p.getProgress();
        progressPpm = p.getProgress_ppm();
        ip = new TcpEndpoint(p.getIp()).toString();
    }

    /**
     * The kind of connection this is. Used for the connectionType field.
     */
    public enum ConnectionType {

        /**
         * Regular bittorrent connection.
         */
        STANDARD_BITTORRENT(peer_info.standard_bittorrent.to_int()),

        /**
         * HTTP connection using the `BEP 19`_ protocol
         */
        WEB_SEED(peer_info.web_seed.to_int()),

        /**
         * HTTP connection using the `BEP 17`_ protocol.
         */
        HTTP_SEED(peer_info.http_seed.to_int()),

        /**
         *
         */
        UNKNOWN(-1);

        ConnectionType(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the native value
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue the swig value
         * @return the enum value
         */
        public static ConnectionType fromSwig(int swigValue) {
            ConnectionType[] enumValues = ConnectionType.class.getEnumConstants();
            for (ConnectionType ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
