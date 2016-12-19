package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author haperlot
 * @samplingIntervalInMs the sampling interval time in milliseconds
 * @maxHistoryInMs max history in milliseconds to be tracked
 */
public final class TorrentStatsTest {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/maximiliamgierschmann/Downloads/Honey_Larochelle_Hijack_FrostClick_FrostWire_MP3_May_06_2016.torrent"};
        File torrentFile = new File(args[0]);
        final SessionManager sessionManager = new SessionManager();
        final CountDownLatch signal = new CountDownLatch(1);

        //starting sessionManager & torrent download
        sessionManager.start();
        TorrentInfo ti = new TorrentInfo(torrentFile);
        sessionManager.download(ti, torrentFile.getParentFile());

        //getting the torrentHandle for the TorrentStats tracker
        final TorrentHandle torrentHandle = sessionManager.find(ti.infoHash());
        long samplingIntervalInMs = 1000; // 0.5 second, 500
        long maxHistoryInMs = 1 * 60 * 1000; // 1 minutes

        // This creates a new TorrentStatsKeeper instance and all the internal alert listeners necessary
        final TorrentStats stats = sessionManager.trackTorrentStats(torrentHandle, samplingIntervalInMs, maxHistoryInMs);

        //declaring listener to check updated values
        sessionManager.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                if (!(alert instanceof TorrentAlert<?>)) {
                    return;
                }
                //if not eq to the torrentHandle return.
                if (!((TorrentAlert<?>) alert).handle().swig().op_eq(torrentHandle.swig())) {
                    return;
                }

                switch (type) {
                    case TORRENT_ADDED:
                        System.out.println("Torrent added");
                        ((TorrentAddedAlert) alert).handle().resume();
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished");
                        signal.countDown();
                        break;
                }

                //gets all the available upload speed samples (bytes/sec), in this case that'd be <= 600 elements
                int[] speedRate = stats.get(TorrentStats.Metric.DownloadRate, 15);
                if (!torrentHandle.status().isFinished()) {
                    for (int i = 0; i < speedRate.length; i++) System.out.print(speedRate[i] + " ");
                }
                System.out.println(" ");
            }
        });
        //stop sessionManager
        signal.await();
        sessionManager.stop();
    }
}