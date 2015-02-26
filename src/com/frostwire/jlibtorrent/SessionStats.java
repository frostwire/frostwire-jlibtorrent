package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionStats {

    private static final int dhtNodesIdx = LibTorrent.findMetricIdx("dht.dht_nodes");

    private final long[] statsValues;

    SessionStats(long[] statsValues) {
        this.statsValues = statsValues;
    }

    public long getDHTNodes() {
        return statsValues != null ? statsValues[dhtNodesIdx] : 0;
    }
}
