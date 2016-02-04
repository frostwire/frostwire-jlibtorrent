package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.TorrentAlertAdapter;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.libtorrent;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DownloadTorrent {

    public static void main(String[] args) throws Throwable {

        //default_storage.disk_write_access_log(true);
        //libtorrent.set_utp_stream_logging(true);

        // comment this line for a real application
        args = new String[]{"/home/user1/Downloads/Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void blockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                System.out.println("Progress: " + p + " for torrent name: " + alert.torrentName());
                System.out.println(s.getStats().download());
            }

            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                signal.countDown();
            }
        });

        th.resume();

        signal.await();

        libtorrent.default_storage_disk_write_access_log(false);
        //libtorrent.set_utp_stream_logging(false);
    }
}
