package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class StatsMetricTest {

    @Test
    public void testListStatsMetric() {
        List<StatsMetric> metrics = LibTorrent.sessionStatsMetrics();

        for (StatsMetric m : metrics) {
            assertEquals(m.valueIndex, LibTorrent.findMetricIdx(m.name));
        }

        assertEquals(-1, LibTorrent.findMetricIdx("anything"));
    }
}
