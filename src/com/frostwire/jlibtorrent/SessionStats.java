package com.frostwire.jlibtorrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionStats {

    private static final StatsMetric[] statsMetrics = LibTorrent.sessionStatsMetrics();

    private final long[] statsValues;
    private final long dhtNodes;

    SessionStats(long[] statsValues, long dhtNodes) {
        this.statsValues = statsValues;
        this.dhtNodes = dhtNodes;
    }

    private Map<String, Long> getNonZeroValues() {
        if (statsValues == null) {
            return Collections.emptyMap();
        }

        HashMap<String, Long> m = new HashMap<String, Long>();

        for (StatsMetric sm : statsMetrics) {
            long value = statsValues[sm.getValueIndex()];
            if (value != 0) {
                m.put(sm.getName(), value);
            }
        }

        return Collections.unmodifiableMap(m);
    }

    public long getDHTNodes() {
        return dhtNodes;
    }
}
