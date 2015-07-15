package com.frostwire.jlibtorrent.tools;

import com.frostwire.jlibtorrent.TorrentInfo;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ReadTorrent extends Tool<TorrentInfo> {

    public ReadTorrent(String[] args) {
        super(args);
    }

    @Override
    protected String usage() {
        return "usage: -i <torrent>";
    }

    @Override
    protected ParseCmd parser(ParseCmd.Builder b) {
        return b.parm("-i", "<torrent>").req().rex(".*")
                .build();
    }

    @Override
    public TorrentInfo run() {
        File f = new File(arg("-i"));
        if (!f.exists()) {
            throw new IllegalStateException("Torrent file " + f + " does not exists");
        }

        return new TorrentInfo(f);
    }

    public static void main(String[] args) {
        ReadTorrent t = new ReadTorrent(args);

        TorrentInfo ti = t.run();
        System.out.println(ti.toEntry());
    }
}
