package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.SessionStatsAlert;
import com.frostwire.jlibtorrent.swig.counters;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet {

    public static void main(String[] args) throws Throwable {

        //String uri = "magnet:?xt=urn:btih:86d0502ead28e495c9e67665340f72aa72fe304e&dn=Frostwire.5.3.6.+%5BWindows%5D&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.istole.it%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";
        String uri = "magnet:?xt=urn:btih:a83cc13bf4a07e85b938dcf06aa707955687ca7c";

        Session s = new Session();

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.SESSION_STATS.getSwig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert);
                if (alert.getType().equals(AlertType.SESSION_STATS)) {
                    SessionStatsAlert stats = (SessionStatsAlert) alert;

                    System.out.println(stats.value(counters.stats_gauge_t.dht_immutable_data.swigValue()));
                    long dhtPeers = 0;//stats.dhtPeers();
                    System.out.println("Num peers: " + dhtPeers);
                    // wait for at least 10 peers in our DHT storage.
                    if (dhtPeers >= 10) {
                        signal.countDown();
                    }
                }
            }
        });

        Downloader d = new Downloader(s);
        DHT dht = new DHT(s);

        // triggers the internal peers discovery and population
        dht.getPeers("a83cc13bf4a07e85b938dcf06aa707955687ca7c");

        System.out.println("Waiting for nodes in DHT (10 seconds)...");
        signal.await(10, TimeUnit.SECONDS);

        System.out.println("Fetching the magnet uri, please wait...");
        byte[] data = d.fetchMagnet(uri, 30000);

        if (data != null) {
            System.out.println(Entry.bdecode(data));
        } else {
            System.out.println("Failed to retrieve the magnet");
        }
    }
}
