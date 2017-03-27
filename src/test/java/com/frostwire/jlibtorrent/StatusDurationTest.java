package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class StatusDurationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private long activeDuration = 0;
    private Entry resumeData;

    //@Test
    public void testDuration() throws Throwable {

        String torrentFilename = "AJC_and_The_Envelope_Pushers_Fallen_Star_FrostClick_FrostWire_MP3_January_16_2017.torrent";
        File torrentFile = folder.newFile(torrentFilename);
        byte[] data = Utils.resourceBytes(torrentFilename);
        Utils.writeByteArrayToFile(torrentFile, data, false);

        final SessionManager s = new SessionManager();

        final CountDownLatch signal1 = new CountDownLatch(1);
        final CountDownLatch signal2 = new CountDownLatch(1);

        s.addListener(new AlertListener() {

            private boolean paused = false;

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
                        if (progress > 2 && !paused) {
                            paused = true;
                            ((PieceFinishedAlert) alert).handle().pause();
                        }
                        break;
                    case TORRENT_PAUSED:
                        log("Torrent paused");
                        TorrentHandle th = ((TorrentPausedAlert) alert).handle();
                        th.saveResumeData();
                        break;
                    case SAVE_RESUME_DATA:
                        // TODO: restore later
                        resumeData = null;//((SaveResumeDataAlert) alert).resumeData();
                        TorrentHandle th2 = ((SaveResumeDataAlert) alert).handle();
                        TorrentStatus status2 = th2.status();
                        activeDuration = status2.activeDuration();
                        signal1.countDown();
                        break;
                    case TORRENT_FINISHED:
                        TorrentHandle th1 = ((TorrentFinishedAlert) alert).handle();
                        TorrentStatus status1 = th1.status();
                        activeDuration = status1.activeDuration();
                        signal2.countDown();
                        break;

                }
            }
        });

        s.start();

        TorrentInfo ti = new TorrentInfo(torrentFile);
        s.download(ti, torrentFile.getParentFile());

        Utils.awaitMinutes(signal1, "to much time downloading the torrent 2%", 5);
        assertNull(s.lastAlertError());

        s.stop();

        log("activeDuration: " + activeDuration);

        //log(resumeData.toString());
        long savedActiveDuration = resumeData.dictionary().get("active_time").integer() * 1000;
        assertEquals(activeDuration, savedActiveDuration);

        File resumeFile = folder.newFile("resume.data");
        Utils.writeByteArrayToFile(resumeFile, resumeData.bencode(), false);

        Thread.sleep(10000);

        s.start();

        long timeMark = System.currentTimeMillis();
        s.download(ti, torrentFile.getParentFile(), resumeFile, null, null);

        Utils.awaitMinutes(signal2, "too much time downloading the torrent 100%", 5);
        assertNull(s.lastAlertError());

        log("activeDuration: " + activeDuration);
        long t1 = activeDuration - savedActiveDuration; // time active in the 98%
        long t2 = System.currentTimeMillis() - timeMark; // approximate time in the 98%
        assertTrue(t2 - t1 < 2000); // active time should not be affected by the sleep(10000)
    }

    private static void log(String msg) {
        // comment/uncomment for hand debugging
        System.out.println(msg);
    }
}
