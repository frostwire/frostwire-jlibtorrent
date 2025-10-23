package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.SessionStatsAlert;

/**
 * Aggregated session-wide statistics with sliding average tracking.
 * <p>
 * {@code SessionStats} maintains real-time and historical statistics about data
 * transfer rates in the session. It tracks payload bytes (actual torrent data),
 * protocol overhead (BitTorrent protocol messages), and IP-level overhead
 * separately for uploaded and downloaded traffic. Useful for monitoring bandwidth
 * usage and performance tracking over time.
 * <p>
 * <b>Understanding Network Traffic Layers:</b>
 * <pre>
 * Total Downloaded = Payload + Protocol Overhead + IP Overhead
 *
 * Payload:      Actual file data from torrents (what matters to users)
 * Protocol:     BitTorrent protocol messages (request, have, bitfield, etc)
 * IP Overhead:  TCP/IP headers and retransmissions (network-level)
 *
 * Example for 1MB download:
 *   Payload:    900 KB (actual file data)
 *   Protocol:   80 KB (peer messages)
 *   IP Overhead: 20 KB (TCP/IP headers)
 *   Total:      1000 KB
 * </pre>
 * <p>
 * <b>Accessing Session Statistics:</b>
 * <pre>
 * // From SessionManager after listening to SessionStatsAlert
 * SessionStats stats = sm.getSessionStats();
 *
 * // Overall totals (lifetime)
 * long totalDown = stats.totalDownload();  // All bytes ever downloaded
 * long totalUp = stats.totalUpload();      // All bytes ever uploaded
 *
 * // Current rates (bytes per second, 5-second average)
 * long downloadRate = stats.downloadRate();  // KB/s (approx)
 * long uploadRate = stats.uploadRate();      // KB/s (approx)
 *
 * // DHT network status
 * long dhtNodes = stats.dhtNodes();  // Number of DHT nodes in routing table
 * </pre>
 * <p>
 * <b>Typical Usage Pattern:</b>
 * <pre>
 * // Enable session stats monitoring
 * sm.postSessionStats();  // Request stats alert
 *
 * sm.addListener(new AlertListener() {
 *     private SessionStats stats = new SessionStats();
 *
 *     public int[] types() {
 *         return new int[] {AlertType.SESSION_STATS.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         SessionStatsAlert a = (SessionStatsAlert) alert;
 *         stats.update(a);  // Update with new metrics
 *
 *         // Display statistics
 *         System.out.println(\"Download: \" +
 *             (stats.downloadRate() / 1024) + \" KB/s\");
 *         System.out.println(\"Upload: \" +
 *             (stats.uploadRate() / 1024) + \" KB/s\");
 *         System.out.println(\"DHT nodes: \" + stats.dhtNodes());
 *     }
 * });
 * </pre>
 * <p>
 * <b>Bandwidth Breakdown Analysis:</b>
 * <pre>
 * SessionStats stats = ...;
 *
 * long totalDown = stats.totalDownload();
 * long payloadDown = stats.stat[DOWNLOAD_PAYLOAD].total();
 * long protocolDown = stats.stat[DOWNLOAD_PROTOCOL].total();
 * long ipDown = stats.stat[DOWNLOAD_IP_PROTOCOL].total();
 *
 * double payloadPercent = (double) payloadDown / totalDown * 100;
 * double protocolPercent = (double) protocolDown / totalDown * 100;
 * double ipPercent = (double) ipDown / totalDown * 100;
 *
 * System.out.println(\"Payload: \" + String.format(\"%.1f%%\", payloadPercent));
 * System.out.println(\"Protocol: \" + String.format(\"%.1f%%\", protocolPercent));
 * System.out.println(\"IP overhead: \" + String.format(\"%.1f%%\", ipPercent));
 * </pre>
 * <p>
 * <b>Rate Calculation (5-Second Average):</b>
 * <ul>
 *   <li>Rates are smoothed over 5 seconds to avoid jitter</li>
 *   <li>Formula: current_avg = (current_avg * 4/5) + (new_sample / 5)</li>
 *   <li>Gives recent activity 20% weight, history 80% weight</li>
 *   <li>More stable than raw byte counts between updates</li>
 * </ul>
 * <p>
 * <b>Performance Monitoring:</b>
 * <pre>
 * // Track bandwidth efficiency
 * long dl = stats.downloadRate();
 * long payload = dl * 0.95;  // Assume 95% is payload
 * long overhead = dl * 0.05; // Assume 5% is overhead
 *
 * if (overhead &gt; dl * 0.2) {
 *     System.out.println(\"High overhead ratio detected\");
 *     // May indicate: slow connections, many peers, packet loss
 * }
 *
 * // Monitor DHT bootstrap
 * long dhtNodes = stats.dhtNodes();
 * if (dhtNodes &lt; 10) {
 *     System.out.println(\"DHT not fully bootstrapped yet\");
 * } else if (dhtNodes &gt; 1000) {
 *     System.out.println(\"DHT fully bootstrapped\");
 * }
 * </pre>
 *
 * @see StatsMetric - Metric names and indices
 * @see SessionStatsAlert - Provides updated statistics
 * @see SessionManager#postSessionStats() - Request stats update
 *
 * @author gubatron
 * @author aldenml
 */
public final class SessionStats {

    // these are the channels we keep stats for
    private static final int UPLOAD_PAYLOAD = 0;
    private static final int UPLOAD_PROTOCOL = 1;
    private static final int UPLOAD_IP_PROTOCOL = 2;
    private static final int DOWNLOAD_PAYLOAD = 3;
    private static final int DOWNLOAD_PROTOCOL = 4;
    private static final int DOWNLOAD_IP_PROTOCOL = 5;
    private static final int NUM_AVERAGES = 6;

    private final Average[] stat;

    private long lastTickTime;
    private long dhtNodes;

    SessionStats() {
        this.stat = new Average[NUM_AVERAGES];
        for (int i = 0; i < this.stat.length; i++) {
            this.stat[i] = new Average();
        }
    }

    public long totalDownload() {
        return stat[DOWNLOAD_PAYLOAD].total() +
                stat[DOWNLOAD_PROTOCOL].total() +
                stat[DOWNLOAD_IP_PROTOCOL].total();
    }

    public long totalUpload() {
        return stat[UPLOAD_PAYLOAD].total() +
                stat[UPLOAD_PROTOCOL].total() +
                stat[UPLOAD_IP_PROTOCOL].total();
    }

    public long downloadRate() {
        return stat[DOWNLOAD_PAYLOAD].rate() +
                stat[DOWNLOAD_PROTOCOL].rate() +
                stat[DOWNLOAD_IP_PROTOCOL].rate();
    }

    public long uploadRate() {
        return stat[UPLOAD_PAYLOAD].rate() +
                stat[UPLOAD_PROTOCOL].rate() +
                stat[UPLOAD_IP_PROTOCOL].rate();
    }

    public long dhtNodes() {
        return dhtNodes;
    }

    void update(SessionStatsAlert alert) {
        long now = System.currentTimeMillis();
        long tickIntervalMs = now - lastTickTime;
        lastTickTime = now;

        long received = alert.value(StatsMetric.NET_RECV_BYTES_COUNTER_INDEX);
        long payload = alert.value(StatsMetric.NET_RECV_PAYLOAD_BYTES_COUNTER_INDEX);
        long protocol = received - payload;
        long ip = alert.value(StatsMetric.NET_RECV_IP_OVERHEAD_BYTES_COUNTER_INDEX);

        payload -= stat[DOWNLOAD_PAYLOAD].total();
        protocol -= stat[DOWNLOAD_PROTOCOL].total();
        ip -= stat[DOWNLOAD_IP_PROTOCOL].total();
        stat[DOWNLOAD_PAYLOAD].add(payload);
        stat[DOWNLOAD_PROTOCOL].add(protocol);
        stat[DOWNLOAD_IP_PROTOCOL].add(ip);

        long sent = alert.value(StatsMetric.NET_SENT_BYTES_COUNTER_INDEX);
        payload = alert.value(StatsMetric.NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX);
        protocol = sent - payload;
        ip = alert.value(StatsMetric.NET_SENT_IP_OVERHEAD_BYTES_COUNTER_INDEX);

        payload -= stat[UPLOAD_PAYLOAD].total();
        protocol -= stat[UPLOAD_PROTOCOL].total();
        ip -= stat[UPLOAD_IP_PROTOCOL].total();
        stat[UPLOAD_PAYLOAD].add(payload);
        stat[UPLOAD_PROTOCOL].add(protocol);
        stat[UPLOAD_IP_PROTOCOL].add(ip);

        tick(tickIntervalMs);
        dhtNodes = alert.value(StatsMetric.DHT_NODES_GAUGE_INDEX);
    }

    void clear() {
        for (int i = 0; i < NUM_AVERAGES; ++i) {
            stat[i].clear();
        }
        dhtNodes = 0;
    }

    // should be called once every second
    private void tick(long tickIntervalMs) {
        for (int i = 0; i < NUM_AVERAGES; ++i) {
            stat[i].tick(tickIntervalMs);
        }
    }

    private static final class Average {

        // total counters
        private long totalCounter;

        // the accumulator for this second.
        private long counter;

        // sliding average
        private long averageSec5;

        public Average() {
        }

        public void add(long count) {
            counter += count;
            totalCounter += count;
        }

        // should be called once every second
        public void tick(long tickIntervalMs) {
            if (tickIntervalMs >= 1) {
                long sample = (counter * 1000) / tickIntervalMs;
                averageSec5 = (averageSec5 * 4) / 5 + sample / 5;
                counter = 0;
            }
        }

        public long rate() {
            return averageSec5;
        }

        public long total() {
            return totalCounter;
        }

        public void clear() {
            counter = 0;
            averageSec5 = 0;
            totalCounter = 0;
        }
    }
}
