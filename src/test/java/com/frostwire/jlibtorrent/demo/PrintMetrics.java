package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.StatsMetric;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PrintMetrics {

    public static void main(String[] args) throws Throwable {

        for (StatsMetric m : LibTorrent.sessionStatsMetrics()) {
            System.out.println(m);
        }
    }
}
