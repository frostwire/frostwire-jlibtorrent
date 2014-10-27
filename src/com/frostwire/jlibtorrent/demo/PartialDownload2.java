package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.FileCompletedAlert;
import com.frostwire.jlibtorrent.alerts.StateChangedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentAlert;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PartialDownload2 {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        Session s = new Session();

        final CountDownLatch s1 = new CountDownLatch(1);
        final CountDownLatch s2 = new CountDownLatch(2);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                if (!(alert instanceof TorrentAlert<?>)) {
                    return;
                }
                TorrentAlert<?> ta = (TorrentAlert<?>) alert;
                TorrentHandle th = ta.getHandle();

                switch (alert.getType()) {
                    case TORRENT_ADDED:
                        th.resume();
                        break;
                    case BLOCK_FINISHED:
                        int p = (int) (th.getStatus().getProgress() * 100);
                        System.out.println("Progress: " + p);
                        break;
                    case FILE_COMPLETED:
                        FileCompletedAlert fca = (FileCompletedAlert) ta;
                        System.out.println("File finished: " + th.getTorrentInfo().getFileAt(fca.getIndex()).getPath());
                        s1.countDown();
                        s2.countDown();
                        break;
                    case STATE_CHANGED:
                        StateChangedAlert sca = (StateChangedAlert) ta;
                        System.out.println("State change: " + sca.getPrevState() + " -> " + sca.getState());
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished");
                        break;
                }
            }
        });

        Downloader d = new Downloader(s);

        TorrentInfo ti = new TorrentInfo(torrentFile);
        File saveDir = torrentFile.getParentFile();
        Priority[] priorities = Priority.array(Priority.IGNORE, ti.getNumFiles());

        System.out.println("About to download first file.");

        priorities[0] = Priority.NORMAL;
        d.download(ti, saveDir, priorities, null);

        s1.await();

        System.out.println("About to download second file.");

        priorities[1] = Priority.NORMAL;
        d.download(ti, saveDir, priorities, null);

        s2.await();
    }
}
