package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link StatsMetric} including metric discovery and file pool metrics.
 *
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

    /**
     * Verify that all file pool counter metric names resolve to valid indices.
     * A valid index is >= 0; -1 means the metric name was not found.
     */
    @Test
    public void testFilePoolCounterMetricIndices() {
        assertNotEquals("FILE_POOL_HITS_COUNTER_INDEX should be a valid index (>= 0)",
                -1, StatsMetric.FILE_POOL_HITS_COUNTER_INDEX);
        assertNotEquals("FILE_POOL_MISSES_COUNTER_INDEX should be a valid index (>= 0)",
                -1, StatsMetric.FILE_POOL_MISSES_COUNTER_INDEX);
        assertNotEquals("FILE_POOL_THREAD_STALL_COUNTER_INDEX should be a valid index (>= 0)",
                -1, StatsMetric.FILE_POOL_THREAD_STALL_COUNTER_INDEX);
        assertNotEquals("FILE_POOL_RACE_COUNTER_INDEX should be a valid index (>= 0)",
                -1, StatsMetric.FILE_POOL_RACE_COUNTER_INDEX);
    }

    /**
     * Verify that the file pool size gauge metric name resolves to a valid index.
     */
    @Test
    public void testFilePoolSizeGaugeMetricIndex() {
        assertNotEquals("FILE_POOL_SIZE_GAUGE_INDEX should be a valid index (>= 0)",
                -1, StatsMetric.FILE_POOL_SIZE_GAUGE_INDEX);
    }

    /**
     * Verify that each file pool metric index is unique (no two metrics share
     * the same index).
     */
    @Test
    public void testFilePoolMetricIndicesAreUnique() {
        int[] indices = {
                StatsMetric.FILE_POOL_HITS_COUNTER_INDEX,
                StatsMetric.FILE_POOL_MISSES_COUNTER_INDEX,
                StatsMetric.FILE_POOL_THREAD_STALL_COUNTER_INDEX,
                StatsMetric.FILE_POOL_RACE_COUNTER_INDEX,
                StatsMetric.FILE_POOL_SIZE_GAUGE_INDEX
        };

        for (int i = 0; i < indices.length; i++) {
            for (int j = i + 1; j < indices.length; j++) {
                assertNotEquals(
                        "File pool metric indices should be unique (index " + i + " vs " + j + ")",
                        indices[i], indices[j]);
            }
        }
    }

    /**
     * Verify that file pool metric names follow the "disk." prefix convention.
     */
    @Test
    public void testFilePoolMetricNamePrefixes() {
        assertTrue("FILE_POOL_HITS should have 'disk.' prefix",
                StatsMetric.FILE_POOL_HITS_COUNTER_NAME.startsWith("disk."));
        assertTrue("FILE_POOL_MISSES should have 'disk.' prefix",
                StatsMetric.FILE_POOL_MISSES_COUNTER_NAME.startsWith("disk."));
        assertTrue("FILE_POOL_THREAD_STALL should have 'disk.' prefix",
                StatsMetric.FILE_POOL_THREAD_STALL_COUNTER_NAME.startsWith("disk."));
        assertTrue("FILE_POOL_RACE should have 'disk.' prefix",
                StatsMetric.FILE_POOL_RACE_COUNTER_NAME.startsWith("disk."));
        assertTrue("FILE_POOL_SIZE should have 'disk.' prefix",
                StatsMetric.FILE_POOL_SIZE_GAUGE_NAME.startsWith("disk."));
    }
}
