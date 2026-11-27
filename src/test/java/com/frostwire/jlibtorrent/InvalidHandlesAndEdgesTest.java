package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentRemovedAlert;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Handles that stopped being valid, torrents without metadata, and the argument checks of the small
 * value classes.
 *
 * @author gubatron
 * @author aldenml
 */
public class InvalidHandlesAndEdgesTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void aHandleOfARemovedTorrentAnswersWithNeutralValues() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "gone", "g.bin", 30_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, saveDir);
            LoopbackSession.get(finished, "the torrent");
            TorrentHandle th = s.sm.find(ti);
            CompletableFuture<Boolean> removed = s.expect(TorrentRemovedAlert.class);
            s.sm.remove(th);
            LoopbackSession.get(removed, "the removal");

            assertFalse(th.isValid());
            assertTrue(th.peerInfo().isEmpty());
            assertNull(th.torrentFile());
            assertNull(th.torrentFileWithHashes());
            assertNull(th.makeMagnetUri());
            assertTrue(th.trackers().isEmpty());
        }
    }

    @Test
    public void aTorrentWithoutMetadataHasNoTorrentFile() {
        try (LoopbackSession s = LoopbackSession.start()) {
            String hash = "0123456789abcdef0123456789abcdef01234567";
            CompletableFuture<Boolean> added = s.expect(AddTorrentAlert.class);
            s.sm.download("magnet:?xt=urn:btih:" + hash, folder.getRoot(), new torrent_flags_t());
            LoopbackSession.get(added, "the magnet torrent");
            TorrentHandle th = s.sm.find(new Sha1Hash(hash));
            assertNotNull(th);
            assertNull(th.torrentFile());
            assertNull(th.torrentFileWithHashes());
            assertNotNull(th.makeMagnetUri());
            assertFalse(th.status(true).hasMetadata());
        }
    }

    @Test
    public void sslCertificatesCanBeSetOnATorrent() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "ssl", "s.bin", 20_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, saveDir);
            LoopbackSession.get(finished, "the torrent");
            TorrentHandle th = s.sm.find(ti);
            byte[] bytes = "not a certificate".getBytes();
            // libtorrent only uses them for ssl torrents, anything else is ignored or refused
            try {
                th.setSslCertificate("cert.pem", "key.pem", "dh.pem");
                th.setSslCertificate("cert.pem", "key.pem", "dh.pem", "passphrase");
                th.setSslCertificateBuffer(bytes, bytes, bytes);
            } catch (RuntimeException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void aMappedBufferThatIsNotATorrentIsRejected() throws IOException {
        File f = folder.newFile("garbage.torrent");
        Utils.writeByteArrayToFile(f, "this is not bencoded".getBytes(), false);
        try (RandomAccessFile raf = new RandomAccessFile(f, "r");
             FileChannel ch = raf.getChannel()) {
            MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
            try {
                new TorrentInfo(buffer);
                fail("garbage");
            } catch (IllegalArgumentException e) {
                assertTrue(e.getMessage(), e.getMessage().startsWith("Can't decode data mapped buffer"));
            }
        }
    }

    @Test
    public void pairsOfTheWrongTypesDoNotConvert() {
        try {
            new Pair<>(1, 2).to_string_int_pair();
            fail("incompatible types");
        } catch (IllegalArgumentException expected) {
            assertEquals("Incompatible types", expected.getMessage());
        }
        try {
            new Pair<>("a", 2).to_string_string_pair();
            fail("incompatible types");
        } catch (IllegalArgumentException expected) {
            assertEquals("Incompatible types", expected.getMessage());
        }
    }

    @Test
    public void unknownNativeValuesAreHandled() {
        try {
            Priority.fromSwig(99);
            fail("no such priority");
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid native value", expected.getMessage());
        }
        assertEquals(Operation.UNKNOWN, Operation.fromSwig(-12345));
        assertEquals("unknown", Operation.UNKNOWN.nativeName());
    }

    @Test
    public void bdecodeNodesThatAreNotDictionariesHaveNoKeys() {
        BDecodeNode n = BDecodeNode.bdecode("i42e".getBytes());
        assertFalse(n.hasInt("x"));
        assertEquals(0, n.getInt("x"));
    }
}
