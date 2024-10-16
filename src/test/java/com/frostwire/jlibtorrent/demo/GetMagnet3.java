package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.StateUpdateAlert;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * To test issue https://github.com/frostwire/frostwire-jlibtorrent/issues/180
 *
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet3 {

    public static void main(String[] args) throws Throwable {

        // code taken from
        // https://github.com/frostwire/frostwire-jlibtorrent/issues/180#issuecomment-345458935
        // author proninyaroslav

        final String magnet = "<magnet here>";

        final SessionManager s = new SessionManager();

        final CountDownLatch signal = new CountDownLatch(1);

        // the session stats are posted about once per second.
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
                    case STATE_UPDATE:
                        StateUpdateAlert sua = (StateUpdateAlert) alert;
                        System.out.println(String.format("[%s] Current priorities:",
                                new Time(System.currentTimeMillis())));
                        sua.status().forEach((torrentStatus -> {
                            TorrentHandle handle = new TorrentHandle(torrentStatus.swig().getHandle());
                            IntStream.range(0, handle.filePriorities().length).forEach((i) -> {
                                Priority priority = handle.filePriorities()[i];
                                System.out.println(String.format("priority=%-8sfile=%s",
                                        priority,
                                        handle.torrentFile().files().fileName(i)));
                            });
                        }));
                        System.out.println();
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished\n");
                        break;
                }
            }
        };

        s.addListener(l);
        s.start();

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

        System.out.println("Fetching the magnet uri, please wait...");
        byte[] data = s.fetchMagnet(magnet, 30, new File("/tmp"));
        if (data == null) {
            System.out.println("data == null");
            s.stop();
            return;
        }
        File f = File.createTempFile("test", "torrent");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(data);
        s.download(new TorrentInfo(f), new File(System.getProperty("user.dir")));

        System.in.read();
        s.stop();
    }
}
