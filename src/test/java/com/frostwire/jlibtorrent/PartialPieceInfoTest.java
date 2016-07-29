package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.BlockDownloadingAlert;
import com.frostwire.jlibtorrent.alerts.TorrentRemovedAlert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.frostwire.jlibtorrent.Utils.awaitSeconds;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class PartialPieceInfoTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testGetInfoFromFirstPiece() throws IOException, InterruptedException {
        byte[] tb = Utils.resourceBytes("Shinobi_Ninja_FrostWire_Mixtape__MP3_128K_Oct_02_2015.torrent");
        final TorrentInfo ti = TorrentInfo.bdecode(tb);

        final CountDownLatch s1 = new CountDownLatch(1);
        final CountDownLatch s2 = new CountDownLatch(1);

        final Session s = SessionTest.session();

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof BlockDownloadingAlert) {
                    s1.countDown();
                }

                if (alert instanceof TorrentRemovedAlert) {
                    if (((TorrentRemovedAlert) alert).infoHash().equals(ti.infoHash())) {
                        s2.countDown();
                    }
                }
            }
        };

        s.addListener(l);

        AddTorrentParams params = new AddTorrentParams();
        params.torrentInfo(ti);

        File savePath = folder.newFolder("torrent_test");
        params.savePath(savePath.getAbsolutePath());

        Priority[] priorities = Priority.array(Priority.IGNORE, ti.numFiles());
        priorities[0] = Priority.NORMAL;
        params.filePriorities(priorities);

        ErrorCode ec = new ErrorCode();
        TorrentHandle th = s.addTorrent(params, ec);
        priorities = Priority.array(Priority.IGNORE, ti.numPieces());
        priorities[0] = Priority.NORMAL;
        th.prioritizePieces(priorities);

        awaitSeconds(s1, "Waiting for block downloading alert", 30);

        ArrayList<PartialPieceInfo> q = th.getDownloadQueue();

        assertTrue(q.size() > 0);
        PartialPieceInfo p = q.get(0);
        ArrayList<BlockInfo> blocks = p.blocks();
        assertTrue(blocks.size() > 0);
        BlockInfo b = blocks.get(0);
        assertNotEquals(BlockInfo.BlockState.FINISHED, b.state());

        s.removeTorrent(th);

        awaitSeconds(s2, "Waiting for torrent handle removed", 30);

        s.removeListener(l);
    }
}
