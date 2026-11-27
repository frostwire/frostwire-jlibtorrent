package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * {@link TorrentStats} fed with the status of a real torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentStatsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void aSeedsStatusIsCopiedAndSeriesGrow() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "stat", "a.bin", 40000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));

        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, saveDir);
            LoopbackSession.get(finished, "the check");
            TorrentHandle th = s.sm.find(ti.infoHashV1());

            TorrentStats stats = new TorrentStats(ti.infoHashV1(), 3);
            assertEquals(3, stats.maxSamples());
            assertEquals(0, stats.series(TorrentStats.SeriesMetric.TIME).size());

            // a status of some other torrent is ignored
            TorrentStats other = new TorrentStats(Sha1Hash.max(), 3);
            other.update(th.status(true));
            assertEquals(0, other.series(TorrentStats.SeriesMetric.TIME).size());

            for (int i = 0; i < 5; i++) {
                stats.update(th.status(true));
            }
            assertEquals(3, stats.series(TorrentStats.SeriesMetric.TIME).size());
            assertEquals(3, stats.series(TorrentStats.SeriesMetric.DOWNLOAD_RATE).size());
            assertEquals(3, stats.series(TorrentStats.SeriesMetric.UPLOAD_RATE).size());
            assertTrue(stats.last(TorrentStats.SeriesMetric.TIME) > 0 || stats.series(TorrentStats.SeriesMetric.TIME).get(2) > 0);
            assertEquals(0, stats.last(TorrentStats.SeriesMetric.DOWNLOAD_RATE));

            assertEquals(TorrentStatus.State.SEEDING, stats.state());
            assertTrue(stats.isSeeding());
            assertTrue(stats.isFinished());
            assertFalse(stats.isPaused());
            assertFalse(stats.isSequentialDownload());
            assertEquals(40000, stats.totalDone());
            assertEquals(stats.totalWanted(), stats.totalWantedDone());
            assertEquals(1.0f, stats.progress(), 0.0001f);
            assertEquals(1_000_000, stats.progressPpm());
            assertEquals(ti.numPieces(), stats.numPieces());
            assertEquals(0, stats.totalDownload());
            assertEquals(0, stats.totalUpload());
            assertEquals(0, stats.totalPayloadDownload());
            assertEquals(0, stats.totalPayloadUpload());
            assertEquals(0, stats.allTimeDownload());
            assertEquals(0, stats.allTimeUpload());
            assertEquals(0, stats.downloadRate());
            assertEquals(0, stats.uploadRate());
            assertEquals(0, stats.downloadPayloadRate());
            assertEquals(0, stats.uploadPayloadRate());
            assertEquals(0, stats.numSeeds());
            assertEquals(0, stats.numPeers());
            assertEquals(0, stats.listSeeds());
            assertEquals(0, stats.listPeers());
            assertEquals(0, stats.numConnections());
            assertNotNull(stats.state());
            stats.needSaveResume();

            th.pause();
            LoopbackSession.awaitCondition("pause visible in the flags", () -> {
                stats.update(th.status(true));
                return stats.isPaused();
            });
            th.setFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
            LoopbackSession.awaitCondition("sequential flag", () -> {
                stats.update(th.status(true));
                return stats.isSequentialDownload();
            });
        }
    }
}
