package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public class DhtGetPeersTest {

    @Test
    public void testGetPeers() {

        Sha1Hash sha1 = new Sha1Hash("1f8cf8c452b3c9c7499def03308134c4774f0d18");

        final Session s = new Session();

        final CountDownLatch s1 = new CountDownLatch(1);
        final CountDownLatch s2 = new CountDownLatch(1);

        // the session stats are posted about once per second.
        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.SESSION_STATS.swig(), AlertType.DHT_STATS.swig(), AlertType.DHT_GET_PEERS_REPLY.swig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                if (type == AlertType.SESSION_STATS) {
                    s.postDHTStats();
                }

                if (type == AlertType.DHT_STATS) {
                    long nodes = ((DhtStatsAlert) alert).totalNodes();
                    // wait for at least 10 nodes in the DHT.
                    if (nodes >= 10) {
                        s1.countDown();
                    }
                }

                if (type == AlertType.DHT_GET_PEERS_REPLY) {
                    ArrayList<TcpEndpoint> peers = ((DhtGetPeersReplyAlert) alert).peers();
                    for (TcpEndpoint endp : peers) {
                        System.out.println(endp);
                    }
                    s2.countDown();
                }
            }
        };

        s.addListener(l);
        s.postDHTStats();

        // waiting for nodes in DHT (10 seconds)
        boolean r = false;
        try {
            r = s1.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignore
        }
        assertTrue("DHT bootstrap timeout", r);

        s.dhtGetPeers(sha1);

        try {
            r = s2.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }
        assertTrue("DHT get_peers timeout", r);

        s.abort();
    }
}
