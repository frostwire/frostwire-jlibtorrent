package com.frostwire.jlibtorrent.tools;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MkTorrent extends Tool {

    public MkTorrent(String[] args) {
        super(args);
    }

    public void run() {
        file_storage fs = new file_storage();

        libtorrent.add_files(fs, "/Users/aldenml/Downloads/commons");

        create_torrent t = new create_torrent(fs);
        t.add_tracker("http://my.tracker.com/announce");
        t.set_creator("libtorrent example");

        error_code ec = new error_code();
        set_piece_hashes_listener l = new set_piece_hashes_listener() {
            @Override
            public void progress(String id, int num_pieces, int i) {
                System.out.println(i + "/" + num_pieces);
            }
        };

        libtorrent.set_piece_hashes("id", t, "/Users/aldenml/Downloads", ec, l);

        if (ec.value() != 0) {
            System.out.println(ec.message());
        }

        entry e = t.generate();

        System.out.println(e.to_string());
    }

    @Override
    protected String usage() {
        return "usage: -i <file|dir>";
    }

    @Override
    protected ParseCmd parser() {
        return new ParseCmd.Builder()
                .help(usage())
                .parm("-i", "<file|dir>")
                .build();
    }

    public static void main(String[] args) {
        MkTorrent t = new MkTorrent(args);
        t.run();
    }
}
