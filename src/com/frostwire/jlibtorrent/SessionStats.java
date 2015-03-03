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

    private final StatsSnapshot[] snapshots;

    SessionStats(StatsSnapshot[] snapshots) {
        this.snapshots = snapshots;
    }

    private Map<String, Long> nonZeroValues() {
        if (snapshots == null || snapshots[1] == null) {
            return Collections.emptyMap();
        }

        HashMap<String, Long> m = new HashMap<String, Long>();

        for (StatsMetric sm : STATS_METRICS) {
            long value = snapshots[1].values[sm.getValueIndex()];
            if (value != 0) {
                m.put(sm.getName(), value);
            }
        }

        return Collections.unmodifiableMap(m);
    }

    public long getDHTNodes() {
        return value(1, DHT_NODES_IDX);
    }

    public int getPayloadDownloadRate() {
        float seconds = seconds();

        if (seconds == 0) {
            return 0;
        }

        return (int) ((value(0, RECV_PAYLOAD_BYTES_IDX) - value(1, RECV_PAYLOAD_BYTES_IDX)) / seconds);
    }

    public int getPayloadUploadRate() {
        float seconds = seconds();

        if (seconds == 0) {
            return 0;
        }

        return (int) ((value(0, SENT_PAYLOAD_BYTES_IDX) - value(1, SENT_PAYLOAD_BYTES_IDX)) / seconds);
    }

    public long getTotalDownload() {
        return value(1, RECV_BYTES_IDX);
    }

    public long getTotalUpload() {
        return value(1, SENT_BYTES_IDX);
    }

    private float seconds() {
        if (snapshots == null || snapshots[0] == null || snapshots[1] == null) {
            return 0;
        }

        return (snapshots[0].timestamp - snapshots[1].timestamp) / 1000000.f;
    }

    private long value(int snapshotIdx, int valueIdx) {
        if (snapshots == null || snapshots[snapshotIdx] == null) {
            return 0;
        }

        return snapshots[snapshotIdx].values[valueIdx];
    }

    static final class StatsSnapshot {

        public StatsSnapshot(long timestamp, long[] values) {
            this.timestamp = timestamp;
            this.values = values;
        }

        public StatsSnapshot(SessionStatsAlert alert) {
            this(alert.getStatsTimestamp(), alert.getValues());
        }

        public final long timestamp;
        public final long[] values;
    }
}
