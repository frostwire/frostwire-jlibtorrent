package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Round trips of every {@link AddTorrentParams} property through the native add_torrent_params.
 *
 * @author gubatron
 * @author aldenml
 */
public class AddTorrentParamsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void scalarPropertiesRoundTrip() {
        AddTorrentParams p = new AddTorrentParams();
        assertNotNull(p.swig());

        p.name("a name");
        assertEquals("a name", p.name());
        p.savePath("/some/where");
        assertEquals("/some/where", p.savePath());
        p.trackerId("tid");
        assertEquals("tid", p.trackerId());
        p.maxUploads(7);
        assertEquals(7, p.maxUploads());
        p.maxConnections(9);
        assertEquals(9, p.maxConnections());
        p.uploadLimit(1234);
        assertEquals(1234, p.uploadLimit());
        p.downloadLimit(4321);
        assertEquals(4321, p.downloadLimit());
        p.storageMode(StorageMode.STORAGE_MODE_ALLOCATE);
        assertEquals(StorageMode.STORAGE_MODE_ALLOCATE, p.storageMode());
        p.storageMode(StorageMode.STORAGE_MODE_SPARSE);
        assertEquals(StorageMode.STORAGE_MODE_SPARSE, p.storageMode());
        assertTrue(p.version() >= 0);

        torrent_flags_t flags = TorrentFlags.PAUSED;
        p.flags(flags);
        assertEquals(flags.to_int(), p.flags().to_int());
    }

    @Test
    public void listPropertiesRoundTrip() {
        AddTorrentParams p = AddTorrentParams.createInstance();

        assertTrue(p.trackers().isEmpty());
        p.trackers(Arrays.asList("udp://one:1/announce", "udp://two:2/announce"));
        assertEquals(Arrays.asList("udp://one:1/announce", "udp://two:2/announce"), p.trackers());

        assertTrue(p.trackerTiers().isEmpty());
        p.trackerTiers(Arrays.asList(0, 1));
        assertEquals(Arrays.asList(0, 1), p.trackerTiers());

        assertTrue(p.dhtNodes().isEmpty());
        p.dhtNodes(Arrays.asList(new Pair<>("1.2.3.4", 6881), new Pair<>("5.6.7.8", 6882)));
        List<Pair<String, Integer>> nodes = p.dhtNodes();
        assertEquals(2, nodes.size());
        assertEquals("1.2.3.4", nodes.get(0).first);
        assertEquals(Integer.valueOf(6882), nodes.get(1).second);

        assertTrue(p.urlSeeds().isEmpty());
        p.urlSeeds(Collections.singletonList("http://seed/"));
        assertEquals(Collections.singletonList("http://seed/"), p.urlSeeds());

        assertTrue(p.peers().isEmpty());
        p.peers(Arrays.asList(new TcpEndpoint("127.0.0.1", 1111), new TcpEndpoint("127.0.0.1", 2222)));
        assertEquals(2, p.peers().size());
        assertEquals(1111, p.peers().get(0).port());
        assertEquals(2222, p.peers().get(1).port());

        assertTrue(p.bannedPeers().isEmpty());
        p.bannedPeers(Collections.singletonList(new TcpEndpoint("127.0.0.2", 3333)));
        assertEquals(1, p.bannedPeers().size());
        assertEquals("127.0.0.2", p.bannedPeers().get(0).address().toString());
    }

    @Test
    public void prioritiesAreAccepted() {
        AddTorrentParams p = new AddTorrentParams();
        p.filePriorities(Priority.array(Priority.SEVEN, 3));
        p.piecePriorities(Priority.array(Priority.IGNORE, 5));
    }

    @Test
    public void infoHashesRoundTrip() {
        AddTorrentParams p = new AddTorrentParams();
        InfoHash empty = p.getInfoHashes();
        assertEquals(false, empty.hasV1());
        assertEquals(false, empty.hasV2());

        AddTorrentParams parsed = AddTorrentParams.parseMagnetUri(
                "magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567&dn=hello&tr=udp%3A%2F%2Ftracker.example%3A80%2Fannounce");
        InfoHash ih = parsed.getInfoHashes();
        assertTrue(ih.hasV1());
        assertEquals("0123456789abcdef0123456789abcdef01234567", ih.getV1().toHex());
        assertEquals("hello", parsed.name());
        assertEquals(Collections.singletonList("udp://tracker.example:80/announce"), parsed.trackers());

        p.setInfoHashes(ih);
        assertEquals("0123456789abcdef0123456789abcdef01234567", p.getInfoHashes().getV1().toHex());
    }

    @Test
    public void invalidMagnetIsRejected() {
        try {
            AddTorrentParams.parseMagnetUri("this is not a magnet");
            fail("a bad magnet uri must be rejected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("Invalid magnet uri"));
        }
    }

    @Test
    public void torrentInfoIsNullUntilOneIsSet() throws IOException {
        AddTorrentParams p = new AddTorrentParams();
        assertNull(p.torrentInfo());

        File dir = folder.newFolder();
        Utils.writeByteArrayToFile(new File(dir, "a.bin"), new byte[40000], false);
        TorrentInfo ti = TorrentInfo.bdecode(new TorrentBuilder().path(dir).generate().entry().bencode());
        p.torrentInfo(ti);
        TorrentInfo back = p.torrentInfo();
        assertNotNull(back);
        assertEquals(ti.infoHashV1(), back.infoHashV1());
        // 40000 bytes are three 16 KiB leaves of the v2 merkle tree, all known from the torrent file
        assertEquals(Collections.singletonList(Arrays.asList(true, true, true)), p.get_verified_leaf_hashes());
    }

    @Test
    public void resumeDataOfAParamsObjectIsABencodedDictionary() {
        AddTorrentParams p = AddTorrentParams.parseMagnetUri(
                "magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567");
        p.savePath("/tmp/x");
        Entry e = AddTorrentParams.writeResumeData(p);
        assertNotNull(e);
        assertTrue(e.bencode().length > 0);
        assertTrue(e.dictionary().containsKey("info-hash"));
    }
}
