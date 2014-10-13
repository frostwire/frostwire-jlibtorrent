package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.entry_list;
import com.frostwire.jlibtorrent.swig.entry_vector;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PartialDownload {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        byte[] data = Utils.readFileToByteArray(torrentFile);
        entry e = entry.bdecode(Vectors.bytes2char_vector(data));

        entry_vector files = e.find_key("info").find_key("files").list().to_vector();

        System.out.println("Files inside the torrent");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).find_key("path").list().front().to_string());
        }

        // Download only the first file
        Priority[] priorities = Priority.array(Priority.IGNORE, (int) files.size());
        priorities[0] = Priority.NORMAL;

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(new TorrentInfo(torrentFile), torrentFile.getParentFile(), priorities, null);

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void blockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                System.out.println("Progress: " + p);
            }

            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                signal.countDown();
            }
        });

        signal.await();
    }
}
