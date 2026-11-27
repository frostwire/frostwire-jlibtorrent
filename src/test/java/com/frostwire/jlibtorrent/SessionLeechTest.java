package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.alerts.PieceInfoAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataAlert;
import com.frostwire.jlibtorrent.alerts.StorageMovedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentDeletedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentRemovedAlert;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Downloading with a real seeder over loopback: partial state of a download in progress, resume
 * data, magnets, removal and moving.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionLeechTest {

    private static final int BIG_PIECE = 65536;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File seedDir;
    private File root;
    private TorrentInfo ti;

    private void createTorrent(int pieceSize, Object... files) throws IOException {
        seedDir = folder.newFolder("seed");
        root = TestTorrents.content(seedDir, "leech", files);
        ti = TestTorrents.torrentInfo(new TorrentBuilder().path(root).pieceSize(pieceSize).flags(TorrentBuilder.V1_ONLY));
    }

    private LoopbackSession startSeeder(boolean throttled) {
        SettingsPack sp = new SettingsPack();
        LoopbackSession seeder = LoopbackSession.start(false, sp);
        CompletableFuture<Boolean> finished = seeder.expect(TorrentFinishedAlert.class);
        seeder.sm.download(ti, seedDir);
        LoopbackSession.get(finished, "the seeder to check its data");
        if (throttled) {
            // the per torrent limit also applies to loopback peers, unlike the session wide one
            seeder.sm.find(ti).setUploadLimit(40_000);
        }
        return seeder;
    }

    @Test
    public void aDownloadInProgressShowsPeersPiecesAndBlocks() throws IOException {
        createTorrent(BIG_PIECE, "a.bin", 300_000);
        try (LoopbackSession seeder = startSeeder(true);
             LoopbackSession leecher = LoopbackSession.start(true, new SettingsPack())) {
            File leechDir = folder.newFolder("leech");
            CompletableFuture<Boolean> done = leecher.expect(TorrentFinishedAlert.class);
            leecher.sm.download(ti, leechDir, null, null, Collections.singletonList(seeder.endpoint()), new torrent_flags_t());
            LoopbackSession.awaitCondition("the leecher's torrent", () -> leecher.sm.find(ti) != null);
            TorrentHandle th = leecher.sm.find(ti);

            // while the throttled transfer runs: a peer, a partial piece and its blocks
            final List<PeerInfo> peers = new ArrayList<>();
            final List<PartialPieceInfo> queue = new ArrayList<>();
            LoopbackSession.awaitCondition("a peer and a piece in flight", () -> {
                peers.clear();
                queue.clear();
                peers.addAll(th.peerInfo());
                queue.addAll(th.getDownloadQueue());
                return !peers.isEmpty() && !queue.isEmpty();
            });
            PeerInfo peer = peers.get(0);
            assertEquals(Collections.<String>emptyList(), Accessors.touch(peer));
            assertEquals(seeder.listenPort(), Integer.parseInt(peer.ip().substring(peer.ip().lastIndexOf(':') + 1)));
            assertNotNull(peer.connectionType());
            assertTrue(peer.progress() >= 0f);

            // the blocks of the pieces in flight, as the download queue alert reports them
            CompletableFuture<List<String>> blocks = leecher.expect(PieceInfoAlert.class,
                    a -> !a.getBlockData().isEmpty(), a -> {
                        List<String> failures = new ArrayList<>();
                        for (BlockInfo b : a.getBlockData()) {
                            failures.addAll(Accessors.touch(b));
                            assertNotNull(b.state());
                            assertTrue(b.blockSize() > 0);
                        }
                        return failures;
                    });
            LoopbackSession.awaitCondition("blocks in flight", () -> {
                th.swig().post_download_queue();
                return blocks.isDone();
            });
            assertEquals(Collections.<String>emptyList(), LoopbackSession.get(blocks, "the blocks"));

            PartialPieceInfo piece = queue.get(0);
            assertEquals(Collections.<String>emptyList(), Accessors.touch(piece));
            assertTrue(piece.blocksInPiece() > 0);
            assertTrue(piece.pieceIndex() >= 0 && piece.pieceIndex() < ti.numPieces());

            // piece priorities live on a torrent that still has a picker
            th.piecePriority(0, Priority.SEVEN);
            LoopbackSession.awaitCondition("piece priority", () -> th.piecePriority(0) == Priority.SEVEN);
            th.prioritizePieces(Priority.array(Priority.NORMAL, ti.numPieces()));
            LoopbackSession.awaitCondition("piece priorities", () ->
                    Arrays.equals(Priority.array(Priority.NORMAL, ti.numPieces()), th.piecePriorities()));
            assertEquals(ti.numPieces(), th.pieceAvailability().length);
            assertFalse(th.status(true).isFinished());
            th.setPieceDeadline(ti.numPieces() - 1, 100);

            LoopbackSession.get(done, "the leecher to finish");
            assertEquals(Collections.<String>emptyList(), leecher.accessorFailures());
            assertArrayEquals(Utils.readFileToByteArray(new File(root, "a.bin")),
                    Utils.readFileToByteArray(new File(leechDir, "leech/a.bin")));
            LoopbackSession.awaitCondition("download totals", () -> leecher.sm.totalDownload() > 0);
        }
    }

    @Test
    public void downloadingAKnownTorrentJustAppliesPriorities() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "a.bin", 20_000, "b.bin", 20_000);
        try (LoopbackSession s = LoopbackSession.start()) {
            File dl = folder.newFolder("dl");
            CompletableFuture<Boolean> added = s.expect(AddTorrentAlert.class);
            s.sm.download(ti, dl, null, null, null, TorrentFlags.PAUSED);
            LoopbackSession.get(added, "the torrent to be added");
            TorrentHandle th = s.sm.find(ti);
            assertNotNull(th);

            try {
                s.sm.download(ti, dl, null, new Priority[]{Priority.NORMAL}, null, new torrent_flags_t());
                fail("one priority for two files");
            } catch (IllegalArgumentException expected) {
                assertEquals("priorities count should be equals to the number of files", expected.getMessage());
            }

            s.sm.download(ti, dl, null, new Priority[]{Priority.IGNORE, Priority.SEVEN}, null, new torrent_flags_t());
            LoopbackSession.awaitCondition("priorities", () ->
                    Arrays.equals(new Priority[]{Priority.IGNORE, Priority.SEVEN}, th.filePriorities()));
            s.sm.download(ti, dl); // no priorities: everything back to normal
            LoopbackSession.awaitCondition("normal priorities", () ->
                    Arrays.equals(Priority.array(Priority.NORMAL, 2), th.filePriorities()));
            assertEquals(1, s.sm.getTorrentHandles().length);
            assertEquals(th.infoHash(), s.sm.getTorrentHandles()[0].infoHash());
        }
    }

    @Test
    public void resumeDataMakesTheNextSessionSkipTheDownload() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "a.bin", 50_000);
        File resume = new File(folder.getRoot(), "resume.dat");
        try (LoopbackSession seeder = startSeeder(false)) {
            TorrentHandle th = seeder.sm.find(ti);
            CompletableFuture<byte[]> saved = seeder.expect(SaveResumeDataAlert.class, a -> true,
                    a -> AddTorrentParams.writeResumeData(a.params()).bencode());
            th.saveResumeData(TorrentHandle.SAVE_INFO_DICT);
            Utils.writeByteArrayToFile(resume, LoopbackSession.get(saved, "the resume data"), false);
        }
        assertTrue(resume.length() > 0);

        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, seedDir, resume, null, null, new torrent_flags_t());
            LoopbackSession.get(finished, "the resumed torrent to be seeding");
            assertTrue(s.sm.find(ti).status(true).isSeeding());
        }

        // a corrupt resume file is ignored, the torrent is still added
        File corrupt = folder.newFile("corrupt.dat");
        Utils.writeByteArrayToFile(corrupt, new byte[]{1, 2, 3}, false);
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, seedDir, corrupt, null, null, new torrent_flags_t());
            LoopbackSession.get(finished, "the torrent with a corrupt resume file");
        }
    }

    @Test
    public void aMagnetFetchesItsMetadataFromAPeerAndDownloads() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "m.bin", 60_000);
        try (LoopbackSession seeder = startSeeder(false);
             LoopbackSession leecher = LoopbackSession.start()) {
            String magnet = "magnet:?xt=urn:btih:" + ti.infoHashV1().toHex() + "&x.pe=127.0.0.1:" + seeder.listenPort();
            File leechDir = folder.newFolder("magnet-dl");
            CompletableFuture<Boolean> metadata = leecher.expect(MetadataReceivedAlert.class);
            CompletableFuture<Boolean> done = leecher.expect(TorrentFinishedAlert.class);
            leecher.sm.download(magnet, leechDir, new torrent_flags_t());
            leecher.sm.download(magnet, leechDir, new torrent_flags_t()); // already there: ignored
            LoopbackSession.get(metadata, "the metadata");
            LoopbackSession.get(done, "the download");
            assertArrayEquals(Utils.readFileToByteArray(new File(root, "m.bin")),
                    Utils.readFileToByteArray(new File(leechDir, "leech/m.bin")));
        }
    }

    @Test
    public void fetchMagnetReturnsTheTorrentFileOfAPeer() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "f.bin", 40_000);
        try (LoopbackSession seeder = startSeeder(false);
             LoopbackSession fetcher = LoopbackSession.start()) {
            String magnet = "magnet:?xt=urn:btih:" + ti.infoHashV1().toHex() + "&x.pe=127.0.0.1:" + seeder.listenPort();
            byte[] data = fetcher.sm.fetchMagnet(magnet, 60, folder.newFolder("tmp"));
            assertNotNull(data);
            TorrentInfo fetched = TorrentInfo.bdecode(data);
            assertEquals(ti.infoHashV1(), fetched.infoHashV1());
            assertEquals(ti.numFiles(), fetched.numFiles());
            // the temporary torrent was removed again
            assertEquals(0, fetcher.sm.getTorrentHandles().length);

            try {
                fetcher.sm.fetchMagnet("not a magnet", 1, folder.getRoot());
                fail("bad magnet");
            } catch (IllegalArgumentException expected) {
                // expected
            }
        }
    }

    @Test
    public void removeKeepsOrDeletesTheFiles() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "r.bin", 30_000);
        try (LoopbackSession s = startSeeder(false)) {
            TorrentHandle th = s.sm.find(ti);
            CompletableFuture<Boolean> removed = s.expect(TorrentRemovedAlert.class);
            s.sm.remove(th);
            LoopbackSession.get(removed, "the removal");
            assertNull(s.sm.find(ti));
            assertTrue(new File(root, "r.bin").exists());
            // an invalid handle is ignored
            s.sm.remove(th);
            s.sm.remove(th, SessionHandle.DELETE_FILES);

            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the torrent to be added again");
            TorrentHandle again = s.sm.find(ti);
            CompletableFuture<Boolean> deleted = s.expect(TorrentDeletedAlert.class);
            s.sm.remove(again, SessionHandle.DELETE_FILES);
            LoopbackSession.get(deleted, "the deletion");
            LoopbackSession.awaitCondition("the files to be deleted", () -> !new File(root, "r.bin").exists());
        }
    }

    @Test
    public void storageOfIncompleteTorrentsIsMoved() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "s.bin", 30_000);
        try (LoopbackSession s = LoopbackSession.start()) {
            File dl = folder.newFolder("dl");
            File moved = folder.newFolder("moved");
            CompletableFuture<Boolean> added = s.expect(AddTorrentAlert.class);
            s.sm.download(ti, dl, null, null, null, TorrentFlags.PAUSED);
            LoopbackSession.get(added, "the torrent");

            CompletableFuture<String> movedAlert = s.expect(StorageMovedAlert.class, a -> true, StorageMovedAlert::storagePath);
            s.sm.moveStorage(moved);
            assertEquals(moved.getAbsolutePath(), new File(LoopbackSession.get(movedAlert, "the move")).getAbsolutePath());

            TorrentHandle th = s.sm.find(ti);
            File second = folder.newFolder("moved2");
            CompletableFuture<String> again = s.expect(StorageMovedAlert.class,
                    a -> a.storagePath().contains("moved2"), StorageMovedAlert::storagePath);
            th.moveStorage(second.getAbsolutePath());
            assertTrue(LoopbackSession.get(again, "the second move").contains("moved2"));

            File third = folder.newFolder("moved3");
            CompletableFuture<String> withFlags = s.expect(StorageMovedAlert.class,
                    a -> a.storagePath().contains("moved3"), StorageMovedAlert::storagePath);
            th.moveStorage(third.getAbsolutePath(), MoveFlags.DONT_REPLACE);
            assertTrue(LoopbackSession.get(withFlags, "the third move").contains("moved3"));
        }
    }

    @Test
    public void aPieceCanBeAddedFromOutside() throws IOException {
        createTorrent(TestTorrents.PIECE_SIZE, "p.bin", 40_000);
        try (LoopbackSession s = LoopbackSession.start()) {
            File dl = folder.newFolder("dl");
            CompletableFuture<Boolean> added = s.expect(AddTorrentAlert.class);
            s.sm.download(ti, dl, null, null, null, TorrentFlags.PAUSED);
            LoopbackSession.get(added, "the torrent");
            TorrentHandle th = s.sm.find(ti);
            th.resume();

            byte[] piece0 = Arrays.copyOf(Utils.readFileToByteArray(new File(root, "p.bin")), TestTorrents.PIECE_SIZE);
            CompletableFuture<Boolean> finished = s.expect(PieceFinishedAlert.class, a -> a.pieceIndex() == 0);
            th.addPiece(0, piece0);
            LoopbackSession.get(finished, "piece 0");
            LoopbackSession.awaitCondition("the piece", () -> th.havePiece(0));

            byte[] piece1 = Arrays.copyOfRange(Utils.readFileToByteArray(new File(root, "p.bin")),
                    TestTorrents.PIECE_SIZE, 2 * TestTorrents.PIECE_SIZE);
            CompletableFuture<Boolean> finished1 = s.expect(PieceFinishedAlert.class, a -> a.pieceIndex() == 1);
            th.addPiece(1, piece1, TorrentHandle.OVERWRITE_EXISTING);
            LoopbackSession.get(finished1, "piece 1");
        }
    }
}
