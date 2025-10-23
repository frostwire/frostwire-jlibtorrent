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
