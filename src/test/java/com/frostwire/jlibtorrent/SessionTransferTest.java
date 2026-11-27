package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A seeder and a leecher on the loopback interface exchange a real torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionTransferTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void aLeecherDownloadsFromASeederOverLoopback() throws IOException {
        File seedDir = folder.newFolder("seed");
        File root = TestTorrents.content(seedDir, "payload", "one.bin", 70000, "two.bin", 30000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root));

        try (LoopbackSession seeder = LoopbackSession.start();
             LoopbackSession leecher = LoopbackSession.start(true, new SettingsPack())) {
            CompletableFuture<Boolean> seeding = seeder.expect(TorrentFinishedAlert.class);
            seeder.sm.download(ti, seedDir);
            LoopbackSession.get(seeding, "the seeder to finish checking");
            assertNotNull(seeder.sm.find(ti.infoHashV1()));

            File leechDir = folder.newFolder("leech");
            CompletableFuture<Boolean> done = leecher.expect(TorrentFinishedAlert.class);
            leecher.sm.download(ti, leechDir, null, null, Collections.singletonList(seeder.endpoint()), new com.frostwire.jlibtorrent.swig.torrent_flags_t());
            LoopbackSession.get(done, "the leecher to finish");

            assertTrue(leecher.hasSeen(PieceFinishedAlert.class));
            assertTrue(leecher.hasSeen(BlockFinishedAlert.class));
            assertArrayEquals(Utils.readFileToByteArray(new File(root, "one.bin")),
                    Utils.readFileToByteArray(new File(leechDir, "payload/one.bin")));
            assertEquals(Collections.<String>emptyList(), leecher.accessorFailures());
            assertEquals(Collections.<String>emptyList(), seeder.accessorFailures());
            assertArrayEquals(Utils.readFileToByteArray(new File(root, "two.bin")),
                    Utils.readFileToByteArray(new File(leechDir, "payload/two.bin")));
        }
    }
}
