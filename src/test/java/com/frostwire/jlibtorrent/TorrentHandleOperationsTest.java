package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.CacheFlushedAlert;
import com.frostwire.jlibtorrent.alerts.FilePrioritiesAlert;
import com.frostwire.jlibtorrent.alerts.FileRenamedAlert;
import com.frostwire.jlibtorrent.alerts.FileStatusAlert;
import com.frostwire.jlibtorrent.alerts.ReadPieceAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataAlert;
import com.frostwire.jlibtorrent.alerts.TorrentCheckedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentPausedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentResumedAlert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Operations on a {@link TorrentHandle} of a real seeding torrent in a loopback session.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentHandleOperationsTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private static LoopbackSession session;
    private static File saveDir;
    private static File root;
    private static TorrentInfo ti;
    private static TorrentHandle th;

    @BeforeClass
    public static void startSeeding() throws Exception {
        saveDir = folder.newFolder("save");
        root = TestTorrents.content(saveDir, "payload", "one.bin", 70000, "two.bin", 30000);
        ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        session = LoopbackSession.start();
        CompletableFuture<Boolean> finished = session.expect(TorrentFinishedAlert.class);
        session.sm.download(ti, saveDir);
        LoopbackSession.get(finished, "the torrent to be checked");
        th = session.sm.find(ti.infoHashV1());
        assertNotNull(th);
    }

    @AfterClass
    public static void stop() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    public void identity() {
        assertTrue(th.isValid());
        assertTrue(th.inSession());
        assertEquals(ti.infoHashV1(), th.infoHash());
        assertNotNull(th.swig());
        assertEquals("payload", th.name());
        assertEquals(saveDir.getAbsolutePath(), new File(th.savePath()).getAbsolutePath());
        assertTrue(th.makeMagnetUri().startsWith("magnet:?xt=urn:btih:" + ti.infoHashV1().toHex()));
        assertNotNull(th.torrentFile());
        assertEquals(ti.numFiles(), th.torrentFile().numFiles());
        assertNotNull(th.torrentFileWithHashes());
    }

    @Test
    public void statusOfASeed() {
        TorrentStatus st = th.status(true);
        assertEquals(TorrentStatus.State.SEEDING, st.state());
        assertTrue(st.isSeeding());
        assertTrue(st.isFinished());
        assertTrue(st.hasMetadata());
        assertFalse(st.isMovingStorage());
        assertEquals(1.0f, st.progress(), 0.0001f);
        assertEquals(1_000_000, st.progressPpm());
        assertEquals(100000, st.total());
        assertEquals(100000, st.totalDone());
        assertEquals(st.totalWanted(), st.totalWantedDone());
        // the name is only filled in when status is queried with QUERY_NAME, see statusWithQueryFlags
        assertEquals(ti.numPieces(), st.numPieces());
        assertEquals(ti.infoHashV1(), st.infoHash());
        assertEquals(ti.infoHashV1(), st.infoHashV1());
        assertEquals(0, st.errorCode().value());
        assertEquals(StorageMode.STORAGE_MODE_SPARSE, st.storageMode());
        assertTrue(st.addedTime() > 0);
        assertTrue(st.completedTime() >= 0);
        assertEquals(16 * 1024, st.blockSize());
        assertNotNull(st.swig());
        assertNotNull(st.flags());
        assertNotNull(st.pieces());
        assertNotNull(st.verifiedPieces());
        // exercise every remaining accessor; they all answer for a seed with no peers
        assertEquals(0, st.numPeers());
        assertEquals(0, st.numSeeds());
        assertEquals(0, st.downloadRate());
        assertEquals(0, st.uploadRate());
        assertEquals(0, st.downloadPayloadRate());
        assertEquals(0, st.uploadPayloadRate());
        assertEquals(0, st.totalDownload());
        assertEquals(0, st.totalUpload());
        assertEquals(0, st.totalPayloadDownload());
        assertEquals(0, st.totalPayloadUpload());
        assertEquals(0, st.totalFailedBytes());
        assertEquals(0, st.totalRedundantBytes());
        assertEquals(0, st.allTimeUpload());
        assertEquals(0, st.allTimeDownload());
        st.nextAnnounce();
        st.currentTracker();
        st.lastSeenComplete();
        st.numComplete();
        st.numIncomplete();
        st.listSeeds();
        st.listPeers();
        st.connectCandidates();
        st.distributedFullCopies();
        st.distributedFraction();
        st.distributedCopies();
        st.numUploads();
        st.numConnections();
        st.uploadsLimit();
        st.connectionsLimit();
        st.upBandwidthQueue();
        st.downBandwidthQueue();
        st.seedRank();
        st.queuePosition();
        st.needSaveResume();
        st.hasIncoming();
        st.announcingToTrackers();
        st.announcingToLsd();
        st.announcingToDht();
        st.lastUpload();
        st.lastDownload();
        st.activeDuration();
        st.finishedDuration();
        st.seedingDuration();
        assertNotNull(st.infoHashV2());
    }

    @Test
    public void statusWithQueryFlags() {
        TorrentStatus st = th.status(TorrentHandle.QUERY_PIECES
                .or_(TorrentHandle.QUERY_VERIFIED_PIECES)
                .or_(TorrentHandle.QUERY_NAME)
                .or_(TorrentHandle.QUERY_SAVE_PATH)
                .or_(TorrentHandle.QUERY_TORRENT_FILE)
                .or_(TorrentHandle.QUERY_DISTRIBUTED_COPIES)
                .or_(TorrentHandle.QUERY_LAST_SEEN_COMPLETE)
                .or_(TorrentHandle.QUERY_ACCURATE_DOWNLOAD_COUNTERS));
        assertEquals(ti.numPieces(), st.pieces().count());
        assertTrue(st.pieces().isAllSet());
        assertEquals("payload", st.name());
        assertEquals(saveDir.getAbsolutePath(), new File(th.savePath()).getAbsolutePath());
        // the cached overload answers the same snapshot until it is forced again
        assertEquals(th.status().state(), th.status().state());
        assertEquals(TorrentStatus.State.SEEDING, th.status(false).state());
    }

    @Test
    public void statusCanBeCloned() {
        TorrentStatus st = th.status(true);
        TorrentStatus copy = st.clone();
        assertEquals(st.name(), copy.name());
        assertEquals(st.state(), copy.state());
        assertEquals(st.total(), copy.total());
    }

    @Test
    public void pieceAndFilePriorities() {
        Priority[] files = th.filePriorities();
        assertEquals(2, files.length);
        th.filePriority(0, Priority.SEVEN);
        // priority changes are posted to the network thread: reads see them eventually
        LoopbackSession.awaitCondition("file priority 0", () -> th.filePriority(0) == Priority.SEVEN);
        assertEquals(Priority.FOUR, th.filePriority(1));
        th.prioritizeFiles(new Priority[]{Priority.NORMAL, Priority.IGNORE});
        LoopbackSession.awaitCondition("file priorities", () ->
                Arrays.equals(new Priority[]{Priority.NORMAL, Priority.IGNORE}, th.filePriorities()));
        th.prioritizeFiles(Priority.array(Priority.NORMAL, 2));
        LoopbackSession.awaitCondition("file priorities reset", () ->
                Arrays.equals(Priority.array(Priority.NORMAL, 2), th.filePriorities()));

        Priority[] pieces = th.piecePriorities();
        assertEquals(ti.numPieces(), pieces.length);
        // a finished torrent has no piece picker, so piece priorities can only be read (see SessionLeechTest)
        assertEquals(Priority.FOUR, th.piecePriority(1));
    }

    @Test
    public void progressAvailabilityAndPieces() {
        long[] progress = th.fileProgress();
        assertArrayEquals(new long[]{30000, 70000}, progress);
        assertArrayEquals(new long[]{30000, 70000}, th.fileProgress(TorrentHandle.PIECE_GRANULARITY));
        for (int i = 0; i < ti.numPieces(); i++) {
            assertTrue(th.havePiece(i));
        }
        // a seed with no peers has no availability to report
        assertEquals(0, th.pieceAvailability().length);
        assertTrue(th.peerInfo().isEmpty());
        assertTrue(th.getDownloadQueue().isEmpty());
    }

    @Test
    public void pieceDeadlinesAreAccepted() {
        th.setPieceDeadline(0, 1000);
        th.setPieceDeadline(1, 2000, TorrentHandle.ALERT_WHEN_AVAILABLE);
        th.resetPieceDeadline(0);
        th.clearPieceDeadlines();
    }

    @Test
    public void limits() {
        th.setUploadLimit(12_345);
        assertEquals(12_345, th.getUploadLimit());
        th.setDownloadLimit(23_456);
        assertEquals(23_456, th.getDownloadLimit());
        th.setUploadLimit(-1);
        th.setDownloadLimit(-1);
    }

    @Test
    public void flagsCanBeSetAndUnset() {
        th.setFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
        assertNotEquals(0, th.flags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD).to_int());
        th.unsetFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
        assertEquals(0, th.flags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD).to_int());
        th.setFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD, TorrentFlags.SEQUENTIAL_DOWNLOAD);
        assertNotEquals(0, th.flags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD).to_int());
        th.unsetFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
    }

    @Test
    public void queueMovesDoNotFailForASingleTorrent() {
        int pos = th.queuePosition();
        th.queuePositionUp();
        th.queuePositionDown();
        th.queuePositionTop();
        th.queuePositionBottom();
        th.queuePositionSet(pos);
        assertTrue(th.queuePosition() >= -1);
    }

    @Test
    public void trackersCanBeAddedAndReplaced() {
        th.addTracker(new AnnounceEntry("udp://127.0.0.1:9/announce"));
        List<AnnounceEntry> trackers = th.trackers();
        assertEquals(1, trackers.size());
        assertEquals("udp://127.0.0.1:9/announce", trackers.get(0).url());

        AnnounceEntry other = new AnnounceEntry("udp://127.0.0.1:10/announce");
        other.tier((short) 1);
        th.replaceTrackers(Collections.singletonList(other));
        assertEquals("udp://127.0.0.1:10/announce", th.trackers().get(0).url());
        assertEquals(1, th.trackers().get(0).tier());

        th.replaceTrackers(Collections.<AnnounceEntry>emptyList());
        assertTrue(th.trackers().isEmpty());
    }

    @Test
    public void webSeedsCanBeAddedAndRemoved() {
        assertTrue(th.urlSeeds().isEmpty());
        th.addUrlSeed("http://127.0.0.1:9/seed/");
        assertEquals(Collections.singletonList("http://127.0.0.1:9/seed/"), th.urlSeeds());
        th.removeUrlSeed("http://127.0.0.1:9/seed/");
        assertTrue(th.urlSeeds().isEmpty());
    }

    @Test
    public void announcesAreRequested() {
        th.forceReannounce();
        th.forceReannounce(10);
        th.forceReannounce(10, 0);
        th.forceReannounce(10, 0, TorrentHandle.IGNORE_MIN_INTERVAL);
        th.forceDHTAnnounce();
        th.scrapeTracker();
    }

    @Test
    public void ownedPiecesCanBeReadBack() {
        CompletableFuture<Integer> read = session.expect(ReadPieceAlert.class,
                a -> a.piece() == 0 && a.error().value() == 0,
                a -> a.bufferPtr() != 0 ? a.size() : -1);
        th.readPiece(0);
        assertEquals(TestTorrents.PIECE_SIZE, (int) LoopbackSession.get(read, "the piece to be read"));
    }

    @Test
    public void pausingResumingAndFlushing() {
        CompletableFuture<Boolean> paused = session.expect(TorrentPausedAlert.class);
        th.pause();
        LoopbackSession.get(paused, "the pause");
        CompletableFuture<Boolean> resumed = session.expect(TorrentResumedAlert.class);
        th.resume();
        LoopbackSession.get(resumed, "the resume");

        CompletableFuture<Boolean> flushed = session.expect(CacheFlushedAlert.class);
        th.flushCache();
        LoopbackSession.get(flushed, "the cache flush");
    }

    @Test
    public void resumeDataIsSavedAndAFileStatusIsPosted() {
        CompletableFuture<Integer> saved = session.expect(SaveResumeDataAlert.class, a -> true,
                a -> AddTorrentParams.writeResumeData(a.params()).bencode().length);
        th.saveResumeData();
        assertTrue(LoopbackSession.get(saved, "the resume data") > 0);

        CompletableFuture<Boolean> saved2 = session.expect(SaveResumeDataAlert.class);
        th.saveResumeData(TorrentHandle.SAVE_INFO_DICT.or_(TorrentHandle.FLUSH_DISK_CACHE));
        LoopbackSession.get(saved2, "the resume data with the info dict");
        th.needSaveResumeData();

        CompletableFuture<Boolean> status = session.expect(FileStatusAlert.class);
        th.postFileStatus();
        LoopbackSession.get(status, "the file status");
        CompletableFuture<Boolean> prio = session.expect(FilePrioritiesAlert.class);
        th.postFilePriorities();
        LoopbackSession.get(prio, "the file priorities");
    }

    @Test
    public void aRecheckFindsTheDataComplete() {
        CompletableFuture<Boolean> checked = session.expect(TorrentCheckedAlert.class);
        th.forceRecheck();
        LoopbackSession.get(checked, "the recheck");
        assertEquals(TorrentStatus.State.SEEDING, th.status(true).state());
    }

    @Test
    public void filesCanBeRenamed() throws Exception {
        CompletableFuture<String> renamed = session.expect(FileRenamedAlert.class, a -> a.index() == 1, FileRenamedAlert::newName);
        th.renameFile(1, "payload/renamed.bin");
        assertTrue(LoopbackSession.get(renamed, "the rename").endsWith("renamed.bin"));
        assertTrue(new File(root, "renamed.bin").exists());
        CompletableFuture<String> back = session.expect(FileRenamedAlert.class, a -> a.index() == 1, FileRenamedAlert::newName);
        th.renameFile(1, "payload/one.bin");
        LoopbackSession.get(back, "the rename back");
    }
}
