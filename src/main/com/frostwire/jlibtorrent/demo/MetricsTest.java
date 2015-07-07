package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.StatsMetric;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MetricsTest {

    public static void main(String[] args) throws Throwable {
        StatsMetric[] statsMetrics = LibTorrent.sessionStatsMetrics();

        for (StatsMetric sm : statsMetrics) {
            System.out.println(sm);
        }

        System.out.println(LibTorrent.findMetricIdx("dht.dht_nodes"));
    }
}
