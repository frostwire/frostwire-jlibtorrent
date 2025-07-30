package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DownloadTorrent {

    public static void main(String[] args) throws Throwable {
        // comment this line for a real application
        args = new String[]{"src/test/resources/Hi-Rez-Someday_Ft_Jordan_Meyer_Live_Studio_Version-_FrostWire.mp4.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final SessionManager s = new SessionManager();

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                switch (type) {
                    case ADD_TORRENT:
                        System.out.println("Torrent added");
                        ((AddTorrentAlert) alert).handle().resume();
                        break;
                    case BLOCK_FINISHED:
                        BlockFinishedAlert a = (BlockFinishedAlert) alert;
                        int p = (int) (a.handle().status().progress() * 100);
                        System.out.println("Progress: " + p + " for torrent name: " + a.torrentName());
                        System.out.println(s.stats().totalDownload());
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent finished");
                        signal.countDown();
                        break;
                    default:
                        System.out.println("Alert: " + alert.type() + " " + alert.message());
                        break;
                }
            }
        });

        s.start();

        TorrentInfo ti = new TorrentInfo(torrentFile);

        // This download method works fine
        // s.download(ti, torrentFile.getParentFile());

        // This other download method always ends up in:
        // ```Exception in thread "main" java.lang.RuntimeException: invalid torrent handle used [libtorrent:20]```
        torrent_flags_t flags = new torrent_flags_t();
        Priority[] priorities = Priority.array(Priority.NORMAL, ti.numFiles());
        //priorities = null;
        s.download(ti, torrentFile.getParentFile(), null, priorities, null, flags);

        signal.await();

        s.stop();
    }
}
