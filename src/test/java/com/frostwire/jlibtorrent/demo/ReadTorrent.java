package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.TorrentInfo;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ReadTorrent {

    public static void main(String[] args) {
        args = new String[]{"/Users/aldenml/Downloads/Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015.torrent"};

        File torrentFile = new File(args[0]);

        TorrentInfo ti = new TorrentInfo(torrentFile);
        System.out.println("info-hash: " + ti.getInfoHash());
        System.out.println(ti.toEntry());
    }
}
