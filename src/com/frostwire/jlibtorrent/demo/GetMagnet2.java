package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.torrent_handle;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet2 {

    public static void main(String[] args) throws Throwable {

        String uri = "magnet:?xt=urn:btih:86d0502ead28e495c9e67665340f72aa72fe304e&dn=Frostwire.5.3.6.+%5BWindows%5D&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.istole.it%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";

        Session s = new Session();

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert);
            }
        });

        DHT dht = new DHT(s);

        dht.getPeers("86d0502ead28e495c9e67665340f72aa72fe304");

        System.out.println("Waiting for nodes in DHT");
        Thread.sleep(5000);
        System.out.println("Nodes in DHT: " + dht.nodes());

        add_torrent_params p = add_torrent_params.create_instance_no_storage();
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, p, ec);

        p.setName("fetchMagnet - " + uri);

        long flags = p.getFlags();
        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
        p.setFlags(flags);

        torrent_handle th = s.getSwig().add_torrent(p);
        th.resume();

        System.in.read();
    }
}
