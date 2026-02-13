package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.reannounce_flags_t;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link TorrentHandle} including the new
 * {@link TorrentHandle#HIGH_PRIORITY} reannounce flag.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentHandleTest {

    /**
     * Verify that the HIGH_PRIORITY flag is available and non-null.
     */
    @Test
    public void testHighPriorityFlagExists() {
        assertNotNull("HIGH_PRIORITY flag should not be null",
                TorrentHandle.HIGH_PRIORITY);
    }

    /**
     * Verify that the IGNORE_MIN_INTERVAL flag is available and non-null.
     */
    @Test
    public void testIgnoreMinIntervalFlagExists() {
        assertNotNull("IGNORE_MIN_INTERVAL flag should not be null",
                TorrentHandle.IGNORE_MIN_INTERVAL);
    }

    /**
     * Verify that HIGH_PRIORITY and IGNORE_MIN_INTERVAL are distinct flags
     * (not the same object reference).
     */
    @Test
    public void testHighPriorityAndIgnoreMinIntervalAreDistinct() {
        assertNotSame(
                "HIGH_PRIORITY and IGNORE_MIN_INTERVAL should be different flag objects",
                TorrentHandle.HIGH_PRIORITY, TorrentHandle.IGNORE_MIN_INTERVAL);
    }

    /**
     * Verify that reannounce flags can be combined using or_().
     * This tests the pattern users would use to combine HIGH_PRIORITY
     * with IGNORE_MIN_INTERVAL for urgent reannouncements.
     */
    @Test
    public void testCombineReannounceFlags() {
        reannounce_flags_t combined = TorrentHandle.IGNORE_MIN_INTERVAL
                .or_(TorrentHandle.HIGH_PRIORITY);
        assertNotNull("Combined reannounce flags should not be null", combined);

        // The combined flags should be different from either individual flag
        // (though we can't easily compare flag values without more swig support,
        // we can at least verify the or_ operation succeeds without error)
    }

    /**
     * Verify that all resume data flags exposed by TorrentHandle are non-null.
     */
    @Test
    public void testResumeDataFlagsExist() {
        assertNotNull("FLUSH_DISK_CACHE should not be null",
                TorrentHandle.FLUSH_DISK_CACHE);
        assertNotNull("SAVE_INFO_DICT should not be null",
                TorrentHandle.SAVE_INFO_DICT);
        assertNotNull("ONLY_IF_MODIFIED should not be null",
                TorrentHandle.ONLY_IF_MODIFIED);
    }

    /**
     * Verify that all status query flags exposed by TorrentHandle are non-null.
     */
    @Test
    public void testStatusQueryFlagsExist() {
        assertNotNull("QUERY_DISTRIBUTED_COPIES should not be null",
                TorrentHandle.QUERY_DISTRIBUTED_COPIES);
        assertNotNull("QUERY_ACCURATE_DOWNLOAD_COUNTERS should not be null",
                TorrentHandle.QUERY_ACCURATE_DOWNLOAD_COUNTERS);
        assertNotNull("QUERY_LAST_SEEN_COMPLETE should not be null",
                TorrentHandle.QUERY_LAST_SEEN_COMPLETE);
        assertNotNull("QUERY_PIECES should not be null",
                TorrentHandle.QUERY_PIECES);
        assertNotNull("QUERY_VERIFIED_PIECES should not be null",
                TorrentHandle.QUERY_VERIFIED_PIECES);
        assertNotNull("QUERY_TORRENT_FILE should not be null",
                TorrentHandle.QUERY_TORRENT_FILE);
        assertNotNull("QUERY_NAME should not be null",
                TorrentHandle.QUERY_NAME);
        assertNotNull("QUERY_SAVE_PATH should not be null",
                TorrentHandle.QUERY_SAVE_PATH);
    }
}
