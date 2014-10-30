package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Session;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet2 {

    public static void main(String[] args) throws Throwable {

        String uri = "magnet:?xt=urn:btih:86d0502ead28e495c9e67665340f72aa72fe304e&dn=Frostwire.5.3.6.+%5BWindows%5D&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.istole.it%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";
        File saveDir = new File("/Users/aldenml/Downloads/frostwire_installer.torrent");

        Session s = new Session();

        DHT dht = new DHT(s);

        System.out.println("Waiting for nodes in DHT");
        dht.waitNodes(1);
        System.out.println("Nodes in DHT: " + dht.nodes());

        Entry entry = dht.get("86d0502ead28e495c9e67665340f72aa72fe304e", 1, TimeUnit.MINUTES);

        System.out.println("get result: " + entry);

        System.in.read();
    }
}
