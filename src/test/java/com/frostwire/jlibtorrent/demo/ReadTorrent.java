package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.TorrentInfo;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ReadTorrent {

    public static void main(String[] args) throws Throwable {
        args = new String[]{"/Users/aldenml/Downloads/Honey_Larochelle_Hijack_FrostClick_FrostWire_MP3_May_06_2016.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Reading all in memory");
        TorrentInfo ti = new TorrentInfo(torrentFile);
        System.out.println("info-hash: " + ti.infoHash());
        System.out.println(ti.toEntry());

        System.out.println("Reading with memory mapped");
        FileChannel fc = new RandomAccessFile(args[0], "r").getChannel();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        TorrentInfo ti2 = new TorrentInfo(buffer);
        System.out.println("info-hash: " + ti2.infoHash());
        System.out.println("creator: " + ti2.creator());
        System.out.println("comment: " + ti2.comment());
        System.out.println(ti2.toEntry());
    }
}
