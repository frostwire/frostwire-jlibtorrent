package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.DhtBootstrapAlert;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtTest {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        final CountDownLatch signal = new CountDownLatch(2);

        s.addListener(new AlertListener() {

            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.getSwig().message());

                if (alert instanceof DhtBootstrapAlert) {
                    signal.countDown();
                }

                if (alert instanceof DhtGetPeersReplyAlert) {
                    ArrayList<TcpEndpoint> peers = ((DhtGetPeersReplyAlert) alert).getPeers();
                    for (int i = 0; i < peers.size(); i++) {
                        System.out.println(peers.get(i));
                    }
                }
            }
        });

        System.out.println("Waiting to DHT bootstrap");

        signal.await();

        System.out.println("Calling dht_get_peers");

        s.getSwig().dht_get_peers(new Sha1Hash("86d0502ead28e495c9e67665340f72aa72fe304").getSwig());

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
