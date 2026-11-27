package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link TorrentBuilder} properties and what {@code generate()} does with them.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentBuilderPropertiesTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void defaultsAndFluentSetters() {
        TorrentBuilder b = new TorrentBuilder();
        assertNull(b.path());
        assertNull(b.comment());
        assertNull(b.creator());
        assertNull(b.listener());
        assertFalse(b.isPrivate());
        assertTrue(b.urlSeeds().isEmpty());
        assertTrue(b.nodes().isEmpty());
        assertTrue(b.trackers().isEmpty());
        assertTrue(b.similarTorrents().isEmpty());
        assertTrue(b.collections().isEmpty());

        File f = new File("x");
        assertSame(b, b.path(f));
        assertEquals(f, b.path());
        assertSame(b, b.pieceSize(32768));
        assertEquals(32768, b.pieceSize());
        assertSame(b, b.alignment(8192));
        assertEquals(8192, b.alignment());
        assertSame(b, b.flags(TorrentBuilder.V1_ONLY));
        assertEquals(TorrentBuilder.V1_ONLY.to_int(), b.flags().to_int());
        assertSame(b, b.comment("c"));
        assertEquals("c", b.comment());
        assertSame(b, b.creator("me"));
        assertEquals("me", b.creator());
        assertSame(b, b.creationDate(1234567890L));
        assertEquals(1234567890L, b.creationDate());
        assertSame(b, b.setPrivate(true));
        assertTrue(b.isPrivate());

        TorrentBuilder.Listener l = new TorrentBuilder.Listener() {
            @Override
            public boolean accept(String filename) {
                return true;
            }

            @Override
            public void progress(int pieceIndex, int numPieces) {
            }
        };
        assertSame(b, b.listener(l));
        assertSame(l, b.listener());
    }

    @Test
    public void listAdders() {
        TorrentBuilder b = new TorrentBuilder();
        b.addUrlSeeds(Arrays.asList("http://a/", "http://b/"));
        assertEquals(Arrays.asList("http://a/", "http://b/"), b.urlSeeds());
        b.addUrlSeed("http://c/");
        assertEquals(3, b.urlSeeds().size());

        b.addNodes(Arrays.asList(new Pair<>("1.1.1.1", 1), new Pair<>("2.2.2.2", 2)));
        b.addNode(new Pair<>("3.3.3.3", 3));
        assertEquals(3, b.nodes().size());
        assertEquals("3.3.3.3", b.nodes().get(2).first);

        b.addTrackers(Arrays.asList(new Pair<>("udp://a/", 0), new Pair<>("udp://b/", 1)));
        b.addTracker(new Pair<>("udp://c/", 2));
        b.addTracker("udp://d/", 3);
        b.addTracker("udp://e/");
        assertEquals(5, b.trackers().size());
        assertEquals(Integer.valueOf(3), b.trackers().get(3).second);
        assertEquals("udp://e/", b.trackers().get(4).first);

        b.addSimilarTorrents(Arrays.asList(Sha1Hash.min(), Sha1Hash.max()));
        b.addSimilarTorrent(Sha1Hash.min());
        assertEquals(3, b.similarTorrents().size());

        b.addCollections(Arrays.asList("x", "y"));
        b.addCollection("z");
        assertEquals(Arrays.asList("x", "y", "z"), b.collections());
    }

    @Test
    public void generateWritesEveryPropertyIntoTheTorrent() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "props", "a.bin", 20000, "b.bin", 5000);
        TorrentBuilder b = TestTorrents.builder(root)
                .flags(TorrentBuilder.V1_ONLY) // a hybrid torrent pads files to piece boundaries
                .comment("a comment")
                .creator("a creator")
                .setPrivate(true)
                .addUrlSeeds(Collections.singletonList("http://seed/"))
                .addNodes(Collections.singletonList(new Pair<>("9.9.9.9", 99)))
                .addTrackers(Arrays.asList(new Pair<>("udp://t1:1/announce", 0), new Pair<>("udp://t2:2/announce", 1)))
                .addSimilarTorrents(Collections.singletonList(Sha1Hash.max()))
                .addCollections(Collections.singletonList("coll"));

        TorrentBuilder.Result r = b.generate();
        assertEquals(PieceCount.of(25000, TestTorrents.PIECE_SIZE), r.numPieces());
        assertEquals(TestTorrents.PIECE_SIZE, r.pieceLength());
        assertEquals(TestTorrents.PIECE_SIZE, r.pieceSize(0));
        assertEquals(25000 - TestTorrents.PIECE_SIZE, r.pieceSize(r.numPieces() - 1));

        TorrentInfo ti = TorrentInfo.bdecode(r.entry().bencode());
        assertEquals("a comment", ti.comment());
        assertEquals("a creator", ti.creator());
        assertTrue(ti.isPrivate());
        assertEquals("http://seed/", ti.webSeeds().get(0).url());
        assertEquals("9.9.9.9", ti.nodes().get(0).first);
        assertEquals(2, ti.trackers().size());
        assertEquals(Sha1Hash.max(), ti.similarTorrents().get(0));
        assertEquals("coll", ti.collections().get(0));
        assertEquals(25000, ti.totalSize());
        assertEquals("props", ti.name());
    }

    @Test
    public void listenerSeesEveryFileAndEveryPiece() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "listen", "one.bin", 20000, "two.bin", 20000, "skip.bin", 100);
        final List<String> accepted = new ArrayList<>();
        final AtomicInteger progress = new AtomicInteger();
        final AtomicInteger total = new AtomicInteger();
        TorrentBuilder.Result r = TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY).listener(new TorrentBuilder.Listener() {
            @Override
            public boolean accept(String filename) {
                accepted.add(new File(filename).getName());
                return !filename.endsWith("skip.bin");
            }

            @Override
            public void progress(int pieceIndex, int numPieces) {
                progress.incrementAndGet();
                total.set(numPieces);
            }
        }).generate();

        assertTrue(accepted.toString(), accepted.contains("skip.bin"));
        TorrentInfo ti = TorrentInfo.bdecode(r.entry().bencode());
        assertEquals(40000, ti.totalSize());
        assertEquals(r.numPieces(), progress.get());
        assertEquals(r.numPieces(), total.get());
    }

    @Test
    public void v1OnlyAndV2OnlyFlags() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "flags", "f.bin", 40000);

        TorrentInfo v1 = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        assertTrue(v1.infoHashType().has_v1());
        assertFalse(v1.infoHashType().has_v2());
        assertNull(v1.infoHashV2());

        TorrentInfo v2 = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V2_ONLY));
        assertTrue(v2.infoHashType().has_v2());
        assertFalse(v2.infoHashType().has_v1());
        assertNull(v2.infoHashV1());
        assertEquals(64, v2.infoHashV2().toHex().length());

        TorrentInfo hybrid = TestTorrents.torrentInfo(TestTorrents.builder(root));
        assertTrue(hybrid.infoHashType().has_v1());
        assertTrue(hybrid.infoHashType().has_v2());
    }

    @Test
    public void modificationTimeAndCanonicalFilesFlagsAreAccepted() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "flags2", "f.bin", 1000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root)
                .flags(TorrentBuilder.MODIFICATION_TIME.or_(TorrentBuilder.CANONICAL_FILES).or_(TorrentBuilder.SYMLINKS)));
        assertEquals(1000, ti.totalSize());
    }

    @Test
    public void generateNeedsAPath() throws IOException {
        try {
            new TorrentBuilder().generate();
            fail("a path is required");
        } catch (IOException e) {
            assertEquals("path can't be null", e.getMessage());
        }
    }

    @Test
    public void emptyContentIsRejected() throws IOException {
        File empty = folder.newFolder("empty");
        try {
            new TorrentBuilder().path(empty).generate();
            fail("no content");
        } catch (IOException e) {
            assertEquals("content total size can't be 0", e.getMessage());
        }
    }

    @Test
    public void unreadableContentFailsHashing() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "gone", "f.bin", 40000);
        TorrentBuilder b = TestTorrents.builder(root).listener(new TorrentBuilder.Listener() {
            @Override
            public boolean accept(String filename) {
                // the file is listed, then removed before the pieces are hashed
                new File(filename).delete();
                return true;
            }

            @Override
            public void progress(int pieceIndex, int numPieces) {
            }
        });
        try {
            b.generate();
            fail("a file that disappeared can't be hashed");
        } catch (IOException e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    private static final class PieceCount {
        static int of(long size, int pieceSize) {
            return (int) ((size + pieceSize - 1) / pieceSize);
        }
    }
}
