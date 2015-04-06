package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.libtorrent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * authors: @aldenml
 *          @gubatron
 *
 * Given a folder, this tool will try to create a .torrent file
 * for each immediate child file found in the folder.
 *
  */
public class FolderTorrentizer {

    public static void torrentize(File folder) {
        if (folder != null && folder.isDirectory()) {
            final File[] files = folder.listFiles();

            final String[] trackers = new String[] {
                    "udp://open.demonii.com:1337",
                    "udp://tracker.coppersurfer.tk:6969",
                    "udp://tracker.leechers-paradise.org:6969",
                    "udp://exodus.desync.com:6969",
                    "udp://tracker.pomf.se"
            };


            if (files != null) {
                int nTorrents = 0;
                for (File f : files) {
                    if (f.isFile() && !f.getName().endsWith(".torrent")) {
                        createTorrent(f, trackers);
                        nTorrents+=1;
                        System.out.println(f.getName() + ".torrent created.");
                    }
                }
                System.out.println(nTorrents + " .torrents created.");
            }

        }
    }

    public static void createTorrent(File f, String[] tracker_urls) {
        try {
            file_storage fs = new file_storage();
            libtorrent.add_files(fs, f.getPath());
            create_torrent torrent = new create_torrent(fs);
            torrent.set_priv(false);

            if (tracker_urls != null && tracker_urls.length > 0) {
                for (String tracker_url : tracker_urls) {
                    torrent.add_tracker(tracker_url);
                }
            }

            error_code ec = new error_code();
            libtorrent.set_piece_hashes(torrent, f.getParentFile().getAbsolutePath(), ec);

            if (ec.value() > 0) {
                System.out.println("error: " + ec.message());
                return;
            }

            final File torrent_file = new File(f.getAbsolutePath() + ".torrent");
            final byte[] torrent_bytes = Vectors.char_vector2bytes(torrent.generate().bencode());
            FileOutputStream fos = new FileOutputStream(torrent_file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(torrent_bytes);
            bos.flush();
            bos.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        FolderTorrentizer.torrentize(new File("/Users/gubatron/Desktop/300_torrent_test"));
    }
}
