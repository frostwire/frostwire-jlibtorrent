package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * To test issue https://github.com/frostwire/frostwire-jlibtorrent/issues/174
 *
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet4 {

    public static void main(String[] args) throws Throwable {

        final String magnet = "<magnet here>";

        final SessionManager s = new SessionManager();

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                switch (alert.type()) {
                    case ADD_TORRENT:
                        System.out.println("Torrent added");
                        TorrentHandle th = ((AddTorrentAlert) alert).handle();
                        th.resume();
                        break;
                    case METADATA_RECEIVED:
                        th = ((MetadataReceivedAlert) alert).handle();
                        TorrentInfo ti = th.torrentFile();
                        Priority[] p = th.filePriorities();
                        p[0] = Priority.IGNORE;

                        System.out.println("Expected priorities:");
                        for (int i = 0; i < ti.numFiles(); i++)
                            System.out.println(String.format("priority=%-8sfile=%s",
                                    p[i],
                                    ti.files().fileName(i)));
                        System.out.println();
                        th.prioritizeFiles(p);
                        break;
                    case STATS:
                        th = ((StatsAlert) alert).handle();
                        ti = th.torrentFile();
                        // ti is null while the metadata is not received
                        if (ti != null) {
                            p = th.filePriorities();
                            System.out.println(String.format("[%s] Current priorities:",
                                    new Time(System.currentTimeMillis())));
                            for (int i = 0; i < ti.numFiles(); i++)
                                System.out.println(String.format("priority=%-8sfile=%s",
                                        p[i],
                                        ti.files().fileName(i)));
                            System.out.println();
                        }
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished\n");
                        break;
                }
            }
        };

        s.addListener(l);
        s.start();

        waitForNodesInDHT(s);

        System.out.println("About to download magnet: " + magnet);
        s.download(magnet, null);

        System.in.read();
        s.stop();
    }

    private static void waitForNodesInDHT(final SessionManager s) throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long nodes = s.stats().dhtNodes();
                if (nodes >= 10) {
                    System.out.println("DHT contains " + nodes + " nodes");
                    signal.countDown();
                    timer.cancel();
                }
            }
        }, 0, 1000);

        System.out.println("Waiting for nodes in DHT (10 seconds)...");
        boolean r = signal.await(10, TimeUnit.SECONDS);
        if (!r) {
            System.out.println("DHT bootstrap timeout");
            System.exit(0);
        }
    }
}
