package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetPiecesTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    //@Test
    public void testStatusPieces() throws Throwable {

        String torrentFilename = "AJC_and_The_Envelope_Pushers_Fallen_Star_FrostClick_FrostWire_MP3_January_16_2017.torrent";
        File torrentFile = folder.newFile(torrentFilename);
        byte[] data = Utils.resourceBytes(torrentFilename);
        Utils.writeByteArrayToFile(torrentFile, data, false);

        final SessionManager s = new SessionManager();

        final CountDownLatch signalFinished = new CountDownLatch(1);

        s.addListener(new AlertListener() {

            private boolean forceChecked = false;

            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                switch (type) {
                    case ADD_TORRENT:
                        ((AddTorrentAlert) alert).handle().resume();
                        break;
                    case PIECE_FINISHED:
                        int progress = (int) (((PieceFinishedAlert) alert).handle().status().progress() * 100);
                        // this number represents the current progress of
                        // the current status (downloading or checking)
                        log("progress: " + progress);
                        if (progress > 4 && !forceChecked) {
                            forceChecked = true;
                            TorrentHandle th = ((PieceFinishedAlert) alert).handle();
                            PieceIndexBitfield pieces = th.status(TorrentHandle.StatusFlags.QUERY_PIECES.swig()).pieces();
                            log("pieces size: " + pieces.size());
                            assertTrue(pieces.size() > 0);
                            assertTrue(pieces.count() > 0);
                            log("pieces value at 0: " + pieces.getBit(0));
                            signalFinished.countDown();
                        }
                        break;
                }
            }
        });

        s.start();

        TorrentInfo ti = new TorrentInfo(torrentFile);
        s.download(ti, torrentFile.getParentFile());

        Utils.awaitMinutes(signalFinished, "too much time downloading the torrent", 5);
        assertNull(s.lastAlertError());

        s.stop();
    }

    private static void log(String msg) {
        // comment/uncomment for hand debugging
        //System.out.println(msg);
    }
}
