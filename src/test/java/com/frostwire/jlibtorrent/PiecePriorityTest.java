package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

/**
 * This test is for address the issue:
 * https://github.com/frostwire/frostwire-jlibtorrent/issues/77
 *
 * @author gubatron
 * @author aldenml
 */
public class PiecePriorityTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    //@Test
    public void testGeneralCase() throws IOException, InterruptedException {
        final CountDownLatch exit = new CountDownLatch(1);

        final AtomicInteger firstP = new AtomicInteger(0);
        final AtomicInteger lastP = new AtomicInteger(0);

        Session s = new Session();
        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof PieceFinishedAlert) {
                    System.out.println(alert);
                    PieceFinishedAlert a = (PieceFinishedAlert) alert;

                    // on piece, set the next piece to SEVEN
                    int indx = a.pieceIndex();
                    if (firstP.get() <= indx && indx < lastP.get()) {
                        a.handle().piecePriority(indx + 1, Priority.SEVEN);
                    }

                    // last piece, exit
                    if (indx == lastP.get()) {
                        exit.countDown();
                    }
                }
            }
        });

        byte[] torrentBytes = Utils.getResourceBytes("test1.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        assertTrue(ti.numFiles() > 1);

        AddTorrentParams params = new AddTorrentParams();
        params.flags(params.flags() & ~add_torrent_params.flags_t.flag_auto_managed.swigValue());
        params.torrentInfo(ti);

        File savePath = folder.newFolder("test1_torrent_test");
        params.savePath(savePath.getAbsolutePath());

        // add torrent
        ErrorCode ec = new ErrorCode();
        TorrentHandle th = s.addTorrent(params, ec);

        // pause it
        th.pause();

        // select a specific file and set its priority to SEVEN
        Priority[] priorities = Priority.array(Priority.IGNORE, ti.numFiles());
        priorities[1] = Priority.SEVEN;
        th.prioritizeFiles(priorities);

        FileStorage fs = ti.files();
        long size = fs.fileSize(1);
        int firstPiece = ti.mapFile(1, 0, 1).getPiece();
        int lastPiece = ti.mapFile(1, size - 1, 1).getPiece();
        assertTrue(firstPiece < lastPiece);
        firstP.set(firstPiece);
        // use the commented line if you want to download the entire file
        lastP.set(firstPiece + 1);//lastP.set(lastPiece);

        // now set the priority of all pieces of selected file to IGNORE
        for (int i = firstPiece; i <= lastPiece; i++) {
            th.piecePriority(i, Priority.IGNORE);
        }

        // set priority of specific pieces to SEVEN
        th.piecePriority(firstPiece, Priority.SEVEN);

        // resume the torrent
        th.resume();

        // change priority of other pieces of that file to SEVEN in alert listener
        // see above in session construction

        assertTrue(exit.await(20, TimeUnit.MINUTES));
    }
}
