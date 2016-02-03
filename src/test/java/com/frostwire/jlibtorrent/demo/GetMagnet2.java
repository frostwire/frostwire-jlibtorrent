package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Downloader;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet2 {

    public static void main(String[] args) throws Throwable {

        final String uri = "magnet:?xt=urn:btih:a83cc13bf4a07e85b938dcf06aa707955687ca7c";

        final Session s = new Session();

        final CountDownLatch signal = new CountDownLatch(1);

        // the session stats are posted about once per second.
        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.SESSION_STATS.getSwig(), AlertType.DHT_STATS.getSwig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert.getType().equals(AlertType.SESSION_STATS)) {
                    s.postDHTStats();
                }

                if (alert.getType().equals(AlertType.DHT_STATS)) {

                    long nodes = ((DhtStatsAlert) alert).totalNodes();
                    // wait for at least 10 nodes in the DHT.
                    if (nodes >= 10) {
                        System.out.println("DHT contains " + nodes + " nodes");
                        signal.countDown();
                    }
                }
            }
        };

        s.addListener(l);
        s.postDHTStats();

        final Downloader d = new Downloader(s);

        System.out.println("Waiting for nodes in DHT (10 seconds)...");
        boolean r = signal.await(10, TimeUnit.SECONDS);
        if (!r) {
            System.out.println("DHT bootstrap timeout");
            System.exit(0);
        }

        // no more trigger of DHT stats
        s.removeListener(l);

        System.out.println("Fetching the magnet uri (multi thread), please wait...");

        final AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 50; i++) {
            final int index = i;
            Thread t = new Thread() {
                @Override
                public void run() {
                    byte[] data = d.fetchMagnet(uri, 30);

                    int count = counter.incrementAndGet();
                    if (data != null) {
                        System.out.println("Success fetching magnet: " + index + "/" + count);
                    } else {
                        System.out.println("Failed to retrieve the magnet: " + index + "/" + count);
                    }
                }
            };

            t.start();
            //t.join();
        }

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.abort();
    }
}
