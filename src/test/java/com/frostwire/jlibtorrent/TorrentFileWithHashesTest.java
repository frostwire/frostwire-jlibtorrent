package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * frostwire/frostwire-jlibtorrent#333: a v2-only torrent taken from a session could not be written
 * back out. {@code torrentFile()} has no piece layers, so {@code bencode()} threw a bare
 * RuntimeException, and the SWIG accessor for the copy with piece layers returned a dangling
 * pointer. Found by TorrentInfoFuzzTest, confirmed in a session.
 */
public class TorrentFileWithHashesTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void aSeedingV2TorrentCanBeWrittenFromTheSession() throws Exception {
        File data = tmp.newFolder("data");
        File folder = new File(data, "folder");
        assertTrue(new File(folder, "sub").mkdirs());
        Random random = new Random(42);
        byte[] a = new byte[40000];
        byte[] b = new byte[5000];
        random.nextBytes(a);
        random.nextBytes(b);
        Files.write(new File(folder, "a.bin").toPath(), a);
        Files.write(new File(folder, "sub/b.bin").toPath(), b);
        byte[] torrent = new TorrentBuilder().path(folder).pieceSize(16384)
                .flags(TorrentBuilder.V2_ONLY).generate().entry().bencode();
        TorrentInfo original = new TorrentInfo(torrent);

        SettingsPack settings = new SettingsPack();
        settings.setEnableDht(false);
        settings.setEnableLsd(false);
        settings.listenInterfaces("127.0.0.1:0");
        SessionManager session = new SessionManager(false);
        session.start(new SessionParams(settings));
        try {
            session.download(original, data);
            TorrentHandle handle = null;
            for (int i = 0; i < 100 && (handle == null || !handle.status().isSeeding()); i++) {
                Thread.sleep(100);
                handle = session.find(original);
            }
            assertNotNull(handle);
            assertTrue("the torrent checks its local data and seeds", handle.status().isSeeding());

            TorrentInfo withoutLayers = handle.torrentFile();
            IllegalStateException e = assertThrows(IllegalStateException.class, withoutLayers::toEntry);
            assertTrue(e.getMessage(), e.getMessage().startsWith("Can't write this torrent: "));

            byte[] saved = handle.torrentFileWithHashes().bencode();
            TorrentInfo reopened = new TorrentInfo(saved);
            assertEquals(original.infoHashV2(), reopened.infoHashV2());
            assertEquals(original.numFiles(), reopened.numFiles());
        } finally {
            session.stop();
        }
    }
}
