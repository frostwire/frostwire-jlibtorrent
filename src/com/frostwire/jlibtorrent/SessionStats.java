package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.SessionStatsAlert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionStats {

    private static final StatsMetric[] STATS_METRICS = LibTorrent.sessionStatsMetrics();

    private static final int DHT_NODES_IDX = LibTorrent.findMetricIdx("dht.dht_nodes");
    private static final int RECV_PAYLOAD_BYTES_IDX = LibTorrent.findMetricIdx("net.recv_payload_bytes");
    private static final int SENT_PAYLOAD_BYTES_IDX = LibTorrent.findMetricIdx("net.sent_payload_bytes");
    private static final int RECV_BYTES_IDX = LibTorrent.findMetricIdx("net.recv_bytes");
    private static final int SENT_BYTES_IDX = LibTorrent.findMetricIdx("net.sent_bytes");

    SessionStats() {
        this.values = null;
    }

    SessionStats(SessionStatsAlert alert) {
        if (alert == null) {
            throw new IllegalArgumentException("alert can't be null");

        }
        this.values = new long[STATS_METRICS.length];

        for (int i = 0; i < STATS_METRICS.length; i++) {
            int index = STATS_METRICS[i].valueIndex;
            if (index < values.length) {
                values[index] = alert.value(index);
            } // TODO: Review this if
        }
    }

    public final long[] values;

    public Map<String, Long> nonZeroValues() {
        if (values == null) {
            return Collections.emptyMap();
        }

        HashMap<String, Long> m = new HashMap<String, Long>();

        for (StatsMetric sm : STATS_METRICS) {
            long value = values[sm.valueIndex];
            if (value != 0) {
                m.put(sm.name, value);
            }
        }

        return Collections.unmodifiableMap(m);
    }

    /**
     * NOTE: This method returns always zero due due to the way it's
     * implemented in libtorrent.
     *
     * @return
     */
    public long getDHTNodes() {
        return value(DHT_NODES_IDX);
    }

    public long getPayloadDownload() {
        return value(RECV_PAYLOAD_BYTES_IDX);
    }

    public long getPayloadUpload() {
        return value(SENT_PAYLOAD_BYTES_IDX);
    }

    public long getTotalDownload() {
        return value(RECV_BYTES_IDX);
    }

    public long getTotalUpload() {
        return value(SENT_BYTES_IDX);
    }

    private long value(int valueIdx) {
        if (values == null) {
            return 0;
        }

        return values[valueIdx];
    }
}
