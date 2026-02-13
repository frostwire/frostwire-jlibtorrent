package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.metric_type_t;
import com.frostwire.jlibtorrent.swig.stats_metric;

/**
 * Metadata describing a single session statistic metric.
 * <p>
 * {@code StatsMetric} identifies a specific statistic available from the session
 * and provides its index for accessing values in SessionStatsAlert. Each metric has
 * a name, index, and type (counter or gauge). Useful for discovering available
 * metrics and accessing statistics programmatically.
 * <p>
 * <b>Metric Types:</b>
 * <ul>
 *   <li><b>Counter:</b> Cumulative total that only increases (NET_SENT_BYTES, etc)</li>
 *   <li><b>Gauge:</b> Current instantaneous value (DHT_NODES, etc)</li>
 * </ul>
 * <p>
 * <b>Common Metrics:</b>
 * <pre>
 * Network Sent (Counter):
 *   NET_SENT_PAYLOAD_BYTES:      Torrent data uploaded
 *   NET_SENT_BYTES:              Total upload including protocol
 *   NET_SENT_IP_OVERHEAD_BYTES:  TCP/IP headers in uploads
 *
 * Network Received (Counter):
 *   NET_RECV_PAYLOAD_BYTES:      Torrent data downloaded
 *   NET_RECV_BYTES:              Total download including protocol
 *   NET_RECV_IP_OVERHEAD_BYTES:  TCP/IP headers in downloads
 *
 * DHT Status (Gauge):
 *   DHT_NODES_GAUGE:             Number of nodes in DHT routing table
 *
 * File Pool (Counter):
 *   FILE_POOL_HITS:              File handle reused from cache
 *   FILE_POOL_MISSES:            New file handle had to be opened
 *   FILE_POOL_THREAD_STALL:      Thread blocked waiting for file handle
 *   FILE_POOL_RACE:              Race conditions in file pool access
 *
 * File Pool (Gauge):
 *   FILE_POOL_SIZE:              Current number of open file handles
 * </pre>
 * <p>
 * <b>Accessing Metric Values:</b>
 * <pre>
 * // From SessionStatsAlert
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.SESSION_STATS.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         SessionStatsAlert a = (SessionStatsAlert) alert;
 *
 *         // Access using predefined indices
 *         long sentPayload = a.value(
 *             StatsMetric.NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX);
 *         long recvPayload = a.value(
 *             StatsMetric.NET_RECV_PAYLOAD_BYTES_COUNTER_INDEX);
 *         long dhtNodes = a.value(
 *             StatsMetric.DHT_NODES_GAUGE_INDEX);
 *
 *         System.out.println(\"Sent: \" + sentPayload + \" bytes\");
 *         System.out.println(\"Recv: \" + recvPayload + \" bytes\");
 *         System.out.println(\"DHT nodes: \" + dhtNodes);
 *     }
 * });
 * </pre>
 * <p>
 * <b>Discovering All Metrics:</b>
 * <pre>
 * // Get list of all available metrics
 * java.util.List&lt;StatsMetric&gt; metrics = LibTorrent.metrics();
 *
 * for (StatsMetric metric : metrics) {
 *     System.out.println(\"Metric: \" + metric.name);
 *     System.out.println(\"  Index: \" + metric.valueIndex);
 *     System.out.println(\"  Type: \" +
 *         (metric.type == StatsMetric.TYPE_COUNTER ? \"counter\" : \"gauge\"));
 * }
 * </pre>
 * <p>
 * <b>Metric Index Usage:</b>
 * <pre>
 * // Metric index is used to access values in SessionStatsAlert
 * int idx = StatsMetric.NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX;
 * long value = sessionStatsAlert.value(idx);
 *
 * // For counters, value is cumulative total
 * // For gauges, value is current snapshot
 * </pre>
 * <p>
 * <b>Performance Analysis with Metrics:</b>
 * <pre>
 * // Calculate actual useful bandwidth vs overhead
 * long sentPayload = statsAlert.value(
 *     StatsMetric.NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX);
 * long sentTotal = statsAlert.value(
 *     StatsMetric.NET_SENT_BYTES_COUNTER_INDEX);
 * long sentIP = statsAlert.value(
 *     StatsMetric.NET_SENT_IP_OVERHEAD_BYTES_COUNTER_INDEX);
 *
 * double efficiency = (double) sentPayload / sentTotal * 100;
 * System.out.println(\"Upload efficiency: \" +
 *     String.format(\"%.1f%%\", efficiency));
 *
 * // sentTotal = sentPayload + protocol + IP overhead
 * long protocol = sentTotal - sentPayload - sentIP;
 * </pre>
 * <p>
 * <b>Standard Metric Prefixes:</b>
 * <ul>
 *   <li><b>net.*:</b> Network statistics (sent/received bytes)</li>
 *   <li><b>dht.*:</b> DHT statistics (nodes, lookups)</li>
 *   <li><b>disk.*:</b> Disk I/O statistics (file pool, read/write times)</li>
 * </ul>
 *
 * @see StatsMetric#TYPE_COUNTER - For cumulative counters
 * @see StatsMetric#TYPE_GAUGE - For point-in-time values
 * @see SessionStatsAlert#value(int) - Get metric value by index
 * @see LibTorrent#metrics() - Discover all available metrics
 *
 * @author gubatron
 * @author aldenml
 */
public final class StatsMetric {

    public static final String NET_SENT_PAYLOAD_BYTES_COUNTER_NAME = "net.sent_payload_bytes";
    public static final String NET_SENT_BYTES_COUNTER_NAME = "net.sent_bytes";
    public static final String NET_SENT_IP_OVERHEAD_BYTES_COUNTER_NAME = "net.sent_ip_overhead_bytes";
    public static final String NET_RECV_PAYLOAD_BYTES_COUNTER_NAME = "net.recv_payload_bytes";
    public static final String NET_RECV_BYTES_COUNTER_NAME = "net.recv_bytes";
    public static final String NET_RECV_IP_OVERHEAD_BYTES_COUNTER_NAME = "net.recv_ip_overhead_bytes";

    public static final int NET_SENT_PAYLOAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_PAYLOAD_BYTES_COUNTER_NAME);
    public static final int NET_SENT_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_BYTES_COUNTER_NAME);
    public static final int NET_SENT_IP_OVERHEAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_SENT_IP_OVERHEAD_BYTES_COUNTER_NAME);
    public static final int NET_RECV_PAYLOAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_PAYLOAD_BYTES_COUNTER_NAME);
    public static final int NET_RECV_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_BYTES_COUNTER_NAME);
    public static final int NET_RECV_IP_OVERHEAD_BYTES_COUNTER_INDEX = LibTorrent.findMetricIdx(NET_RECV_IP_OVERHEAD_BYTES_COUNTER_NAME);

    public static final String DHT_NODES_GAUGE_NAME = "dht.dht_nodes";
    public static final int DHT_NODES_GAUGE_INDEX = LibTorrent.findMetricIdx(DHT_NODES_GAUGE_NAME);

    // -----------------------------------------------------------------------
    // File Pool Metrics (Counters)
    // -----------------------------------------------------------------------

    /**
     * Metric name for the file pool hits counter.
     * <p>
     * Counts the number of times an open file handle was successfully reused
     * from the file pool cache, avoiding the cost of opening a new file descriptor.
     * A high hit rate indicates the file pool size is well-tuned for the workload.
     * <p>
     * <b>Type:</b> Counter (cumulative, monotonically increasing)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * // Monitor file pool efficiency in a SessionStatsAlert handler
     * long hits = statsAlert.value(StatsMetric.FILE_POOL_HITS_COUNTER_INDEX);
     * long misses = statsAlert.value(StatsMetric.FILE_POOL_MISSES_COUNTER_INDEX);
     * long total = hits + misses;
     * if (total > 0) {
     *     double hitRate = (double) hits / total * 100;
     *     System.out.println("File pool hit rate: " +
     *         String.format("%.1f%%", hitRate));
     * }
     * }</pre>
     *
     * @see #FILE_POOL_HITS_COUNTER_INDEX
     * @see #FILE_POOL_MISSES_COUNTER_NAME
     * @see #FILE_POOL_SIZE_GAUGE_NAME
     */
    public static final String FILE_POOL_HITS_COUNTER_NAME = "disk.file_pool_hits";

    /**
     * Index for accessing the file pool hits counter value from a
     * {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert}.
     *
     * @see #FILE_POOL_HITS_COUNTER_NAME
     */
    public static final int FILE_POOL_HITS_COUNTER_INDEX = LibTorrent.findMetricIdx(FILE_POOL_HITS_COUNTER_NAME);

    /**
     * Metric name for the file pool misses counter.
     * <p>
     * Counts the number of times a file handle was not found in the pool cache
     * and a new file descriptor had to be opened. A high miss rate may indicate
     * that the file pool size (configured via
     * {@code settings_pack.int_types.file_pool_size}) is too small for the
     * number of active torrents or files.
     * <p>
     * <b>Type:</b> Counter (cumulative, monotonically increasing)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * long misses = statsAlert.value(StatsMetric.FILE_POOL_MISSES_COUNTER_INDEX);
     * System.out.println("File pool misses: " + misses);
     *
     * // If misses are high, consider increasing the file pool size:
     * // settingsPack.setInteger(
     * //     settings_pack.int_types.file_pool_size.swigValue(), 200);
     * }</pre>
     *
     * @see #FILE_POOL_MISSES_COUNTER_INDEX
     * @see #FILE_POOL_HITS_COUNTER_NAME
     */
    public static final String FILE_POOL_MISSES_COUNTER_NAME = "disk.file_pool_misses";

    /**
     * Index for accessing the file pool misses counter value from a
     * {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert}.
     *
     * @see #FILE_POOL_MISSES_COUNTER_NAME
     */
    public static final int FILE_POOL_MISSES_COUNTER_INDEX = LibTorrent.findMetricIdx(FILE_POOL_MISSES_COUNTER_NAME);

    /**
     * Metric name for the file pool thread stall counter.
     * <p>
     * Counts the number of times a disk I/O thread had to stall (wait/block)
     * because the file handle it needed was in use by another thread. Frequent
     * stalls may indicate contention on popular files and could signal the need
     * for a larger file pool or fewer concurrent disk threads.
     * <p>
     * <b>Type:</b> Counter (cumulative, monotonically increasing)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * long stalls = statsAlert.value(
     *     StatsMetric.FILE_POOL_THREAD_STALL_COUNTER_INDEX);
     * if (stalls > 1000) {
     *     System.out.println("Warning: " + stalls +
     *         " file pool thread stalls detected, " +
     *         "consider tuning file_pool_size or disk threads");
     * }
     * }</pre>
     *
     * @see #FILE_POOL_THREAD_STALL_COUNTER_INDEX
     */
    public static final String FILE_POOL_THREAD_STALL_COUNTER_NAME = "disk.file_pool_thread_stall";

    /**
     * Index for accessing the file pool thread stall counter value from a
     * {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert}.
     *
     * @see #FILE_POOL_THREAD_STALL_COUNTER_NAME
     */
    public static final int FILE_POOL_THREAD_STALL_COUNTER_INDEX = LibTorrent.findMetricIdx(FILE_POOL_THREAD_STALL_COUNTER_NAME);

    /**
     * Metric name for the file pool race condition counter.
     * <p>
     * Counts the number of race conditions encountered in file pool access.
     * These occur when multiple threads attempt to open or close the same file
     * concurrently. While the file pool handles these races safely, a high count
     * may indicate excessive contention on the same files.
     * <p>
     * <b>Type:</b> Counter (cumulative, monotonically increasing)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * long races = statsAlert.value(
     *     StatsMetric.FILE_POOL_RACE_COUNTER_INDEX);
     * System.out.println("File pool races: " + races);
     * }</pre>
     *
     * @see #FILE_POOL_RACE_COUNTER_INDEX
     */
    public static final String FILE_POOL_RACE_COUNTER_NAME = "disk.file_pool_race";

    /**
     * Index for accessing the file pool race counter value from a
     * {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert}.
     *
     * @see #FILE_POOL_RACE_COUNTER_NAME
     */
    public static final int FILE_POOL_RACE_COUNTER_INDEX = LibTorrent.findMetricIdx(FILE_POOL_RACE_COUNTER_NAME);

    // -----------------------------------------------------------------------
    // File Pool Metrics (Gauges)
    // -----------------------------------------------------------------------

    /**
     * Metric name for the file pool size gauge.
     * <p>
     * Reports the current number of open file handles held in the file pool
     * cache. This is a point-in-time snapshot, unlike the counter metrics which
     * are cumulative. The value is bounded by the {@code file_pool_size} setting
     * in {@link com.frostwire.jlibtorrent.swig.settings_pack}.
     * <p>
     * <b>Type:</b> Gauge (instantaneous snapshot)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * // Comprehensive file pool diagnostics
     * sm.addListener(new AlertListener() {
     *     public int[] types() {
     *         return new int[] {AlertType.SESSION_STATS.swig()};
     *     }
     *
     *     public void alert(Alert<?> alert) {
     *         SessionStatsAlert a = (SessionStatsAlert) alert;
     *
     *         long poolSize = a.value(StatsMetric.FILE_POOL_SIZE_GAUGE_INDEX);
     *         long hits = a.value(StatsMetric.FILE_POOL_HITS_COUNTER_INDEX);
     *         long misses = a.value(StatsMetric.FILE_POOL_MISSES_COUNTER_INDEX);
     *         long stalls = a.value(
     *             StatsMetric.FILE_POOL_THREAD_STALL_COUNTER_INDEX);
     *         long races = a.value(StatsMetric.FILE_POOL_RACE_COUNTER_INDEX);
     *
     *         System.out.println("File Pool Status:");
     *         System.out.println("  Open handles: " + poolSize);
     *         System.out.println("  Hits: " + hits);
     *         System.out.println("  Misses: " + misses);
     *         System.out.println("  Thread stalls: " + stalls);
     *         System.out.println("  Races: " + races);
     *
     *         long total = hits + misses;
     *         if (total > 0) {
     *             System.out.printf("  Hit rate: %.1f%%%n",
     *                 (double) hits / total * 100);
     *         }
     *     }
     * });
     *
     * // Trigger session stats collection
     * sm.postSessionStats();
     * }</pre>
     *
     * @see #FILE_POOL_SIZE_GAUGE_INDEX
     * @see #FILE_POOL_HITS_COUNTER_NAME
     * @see #FILE_POOL_MISSES_COUNTER_NAME
     */
    public static final String FILE_POOL_SIZE_GAUGE_NAME = "disk.file_pool_size";

    /**
     * Index for accessing the file pool size gauge value from a
     * {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert}.
     *
     * @see #FILE_POOL_SIZE_GAUGE_NAME
     */
    public static final int FILE_POOL_SIZE_GAUGE_INDEX = LibTorrent.findMetricIdx(FILE_POOL_SIZE_GAUGE_NAME);

    public static final int TYPE_COUNTER = metric_type_t.counter.swigValue();
    public static final int TYPE_GAUGE = metric_type_t.gauge.swigValue();

    StatsMetric(stats_metric sm) {
        this.name = sm.get_name();
        this.valueIndex = sm.getValue_index();
        this.type = sm.getType().swigValue();
    }

    public final String name;

    public final int valueIndex;

    public final int type;

    @Override
    public String toString() {
        return name + ":" + valueIndex + ":" + typeStr();
    }

    private String typeStr() {
        String str = "unknown";

        if (type == TYPE_COUNTER) {
            str = "counter";
        } else if (type == TYPE_GAUGE) {
            str = "gauge";
        }

        return str;
    }
}
