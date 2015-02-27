package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class CreateTorrentTest {

    public static void main(String[] args) throws Throwable {
        file_storage fs = new file_storage();

        // recursively adds files in directories
        libtorrent.add_files(fs, "/Users/aldenml/Downloads/Kellee");

        create_torrent t = new create_torrent(fs);
        t.add_tracker("http://my.tracker.com/announce");
        t.set_creator("libtorrent example");

        error_code ec = new error_code();
        // reads the files and calculates the hashes
        libtorrent.set_piece_hashes(t, "/Users/aldenml/Downloads", ec);

        if (ec.value() != 0) {
            System.out.println(ec.message());
        }

        entry e = t.generate();

        System.out.println(e.to_string());
    }
}
