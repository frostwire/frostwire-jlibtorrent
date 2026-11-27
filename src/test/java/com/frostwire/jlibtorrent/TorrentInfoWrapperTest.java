package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link TorrentInfo} loaded from every kind of source and edited through its mutable parts.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentInfoWrapperTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private TorrentInfo sample() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "sample", "a.bin", 20000, "dir/b.bin", 30000);
        return TestTorrents.torrentInfo(TestTorrents.builder(root)
                .flags(TorrentBuilder.V1_ONLY)
                .comment("hello")
                .creator("tester")
                .addTracker("udp://t1:1/announce", 0));
    }

    @Test
    public void basicDescriptionOfAMultiFileTorrent() throws IOException {
        TorrentInfo ti = sample();
        assertTrue(ti.isValid());
        assertTrue(ti.isLoaded());
        assertFalse(ti.isPrivate());
        assertFalse(ti.isI2p());
        assertEquals("sample", ti.name());
        assertEquals(2, ti.numFiles());
        assertEquals(50000, ti.totalSize());
        assertEquals(TestTorrents.PIECE_SIZE, ti.pieceLength());
        assertEquals(4, ti.numPieces());
        assertEquals(TestTorrents.PIECE_SIZE, ti.pieceSize(0));
        assertEquals(50000 - 3 * TestTorrents.PIECE_SIZE, ti.pieceSize(3));
        assertEquals(ti.pieceSize(3), ti.pieceSizeForReq(3));
        assertEquals("hello", ti.comment());
        assertEquals("tester", ti.creator());
        assertTrue(ti.creationDate() > 0);
        assertNotNull(ti.swig());
        assertNotNull(ti.swigParams());
        assertNotNull(ti.hashForPiece(0));
        assertFalse(ti.hashForPiece(0).isAllZeros());
        assertTrue(ti.sslCert().length == 0);
        assertTrue(ti.similarTorrents().isEmpty());
        assertTrue(ti.collections().isEmpty());
        assertTrue(ti.infoHashType().has_v1());
        assertNotNull(ti.infoHashV1());
        assertEquals(ti.infoHashV1(), new Sha1Hash(ti.infoHashType().getV1()));
    }

    @Test
    public void filesAndMappings() throws IOException {
        TorrentInfo ti = sample();
        FileStorage fs = ti.files();
        assertEquals(2, fs.numFiles());
        assertSame(ti.swig(), fs.ti());
        @SuppressWarnings("deprecation")
        FileStorage orig = ti.origFiles();
        assertEquals(2, orig.numFiles());

        // piece 1 starts 16384 bytes in: 3616 bytes of the first file, then the start of the second
        List<FileSlice> slices = ti.mapBlock(1, 0, 8000);
        assertEquals(2, slices.size());
        assertEquals(0, slices.get(0).fileIndex());
        assertEquals(16384, slices.get(0).offset());
        assertEquals(3616, slices.get(0).size());
        assertEquals(1, slices.get(1).fileIndex());
        assertEquals(0, slices.get(1).offset());
        assertEquals(8000 - 3616, slices.get(1).size());

        PeerRequest req = ti.mapFile(1, 100, 200);
        assertEquals(1, req.piece());
        assertEquals(20000 - TestTorrents.PIECE_SIZE + 100, req.start());
        assertEquals(200, req.length());
    }

    @Test
    public void trackersTiersAndClear() throws IOException {
        TorrentInfo ti = sample();
        assertEquals(1, ti.trackers().size());
        ti.addTracker("udp://t3:3/announce", 2);
        ti.addTracker("udp://t2:2/announce");
        List<AnnounceEntry> trackers = ti.trackers();
        assertEquals(3, trackers.size());
        // sorted by tier
        assertEquals("udp://t1:1/announce", trackers.get(0).url());
        assertEquals("udp://t2:2/announce", trackers.get(1).url());
        assertEquals("udp://t3:3/announce", trackers.get(2).url());
        assertEquals(2, trackers.get(2).tier());

        ti.clearTrackers();
        assertTrue(ti.trackers().isEmpty());
    }

    @Test
    public void webSeedsAndNodesAreEditable() throws IOException {
        TorrentInfo ti = sample();
        assertTrue(ti.webSeeds().isEmpty());
        ti.addUrlSeed("http://seed/one");
        ti.addUrlSeed("http://seed/one");
        assertEquals(1, ti.webSeeds().size());
        ti.setWebSeeds(Arrays.asList(new WebSeedEntry("http://seed/a"), new WebSeedEntry("http://seed/b")));
        assertEquals(Arrays.asList(new WebSeedEntry("http://seed/a"), new WebSeedEntry("http://seed/b")), ti.webSeeds());

        assertTrue(ti.nodes().isEmpty());
        ti.addNode("1.2.3.4", 6881);
        assertEquals(1, ti.nodes().size());
        assertEquals("1.2.3.4", ti.nodes().get(0).first);
        assertEquals(Integer.valueOf(6881), ti.nodes().get(0).second);
    }

    @Test
    public void magnetUriCarriesTheInfoHashNameAndTrackers() throws IOException {
        TorrentInfo ti = sample();
        String uri = ti.makeMagnetUri();
        assertTrue(uri, uri.startsWith("magnet:?xt=urn:btih:" + ti.infoHashV1().toHex()));
        assertTrue(uri, uri.contains("dn=sample"));
        assertTrue(uri, uri.contains("tr=udp"));
    }

    @Test
    public void torrentFileRoundTripsThroughEntryAndBytes() throws IOException {
        TorrentInfo ti = sample();
        ti.addNode("1.2.3.4", 6881);
        Entry e = ti.toEntry();
        assertTrue(e.dictionary().containsKey("info"));
        assertEquals("udp://t1:1/announce", e.dictionary().get("announce").string());

        byte[] bytes = ti.bencode();
        TorrentInfo back = TorrentInfo.bdecode(bytes);
        assertEquals(ti.infoHashV1(), back.infoHashV1());
        assertEquals(ti.numFiles(), back.numFiles());
        assertEquals("1.2.3.4", back.nodes().get(0).first);
    }

    @Test
    public void loadedFromAFileAndFromAMappedBuffer() throws IOException {
        TorrentInfo ti = sample();
        byte[] bytes = ti.bencode();
        File f = folder.newFile("sample.torrent");
        Utils.writeByteArrayToFile(f, bytes, false);

        TorrentInfo fromFile = new TorrentInfo(f);
        assertEquals(ti.infoHashV1(), fromFile.infoHashV1());

        try (RandomAccessFile raf = new RandomAccessFile(f, "r");
             FileChannel ch = raf.getChannel()) {
            MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
            TorrentInfo mapped = new TorrentInfo(buffer);
            assertEquals(ti.infoHashV1(), mapped.infoHashV1());
        }
    }

    @Test
    public void wrappingParamsWithoutATorrentIsRejected() {
        try {
            new TorrentInfo(com.frostwire.jlibtorrent.swig.add_torrent_params.create_instance());
            fail("no torrent_info in the params");
        } catch (IllegalArgumentException e) {
            assertEquals("add_torrent_params has no torrent_info", e.getMessage());
        }
    }

    @Test
    public void wrappingANativeTorrentInfoHasNoEditableState() throws IOException {
        TorrentInfo ti = sample();
        TorrentInfo wrapper = new TorrentInfo(ti.swig());
        assertTrue(wrapper.trackers().isEmpty());
        assertTrue(wrapper.webSeeds().isEmpty());
        assertTrue(wrapper.nodes().isEmpty());
        assertEquals(0, wrapper.creationDate());
        assertEquals("", wrapper.creator());
        assertEquals("", wrapper.comment());
        wrapper.clearTrackers();
        assertNotNull(wrapper.swigParams());
    }

    @Test
    public void garbageAndMissingFilesAreRejected() {
        try {
            TorrentInfo.bdecode(new byte[]{1, 2, 3});
            fail("garbage");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("Can't decode data"));
        }
        try {
            new TorrentInfo(new File(folder.getRoot(), "missing.torrent"));
            fail("missing file");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("missing.torrent"));
        }
    }

    @Test
    public void aV2OnlyTorrentHasNoV1Hash() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "v2", "x.bin", 40000);
        TorrentInfo v2 = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V2_ONLY));
        assertNull(v2.infoHashV1());
        assertNotNull(v2.infoHashV2());
    }
}
