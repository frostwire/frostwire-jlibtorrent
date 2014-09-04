package com.frostwire.libtorrent;

import com.frostwire.libtorrent.swig.add_torrent_params;
import com.frostwire.libtorrent.swig.alert;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {

        String[] paths = {"/Users/aldenml/Downloads/test.pdf"};

        // trigger the native library's load
        System.out.println(LibTorrent.version());

        final Session s = new Session();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<alert> alerts = s.waitForAlerts(1000);
                    for (alert alert : alerts) {
                        System.out.println(alert.getClass() + ": " + alert.what() + " - " + alert.message());
                    }
                    System.out.println("loop");
                }
            }
        });

        t.setDaemon(true);
        t.start();

        String torrentPath = "/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent";
        TorrentInfo ti = new TorrentInfo(torrentPath);
        System.out.println(ti.mkString());

        add_torrent_params p = new add_torrent_params();
        p.setSave_path("/Users/aldenml/Downloads");
        p.setTi(ti.getInf());
        s.s.add_torrent(p);


        System.out.println("Enter to exit");
        System.in.read();
    }
}
