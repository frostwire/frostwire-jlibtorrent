package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersReplyAlert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import static com.frostwire.jlibtorrent.Utils.awaitMinutes;
import static com.frostwire.jlibtorrent.Utils.awaitSeconds;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class DhtGetPeersTest {

    @Test
    public void testGetPeers() {

        Sha1Hash sha1 = new Sha1Hash("33395DA120C9A4758E896DED4DEC5F2495C9973F");

        final Session s = SessionTest.session();

        final CountDownLatch s1 = new CountDownLatch(1);
        final CountDownLatch s2 = new CountDownLatch(1);

        final LinkedList<TcpEndpoint> peers = new LinkedList<>();

        // the session stats are posted about once per second.
        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.SESSION_STATS.swig(), AlertType.DHT_GET_PEERS_REPLY.swig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();

                if (type == AlertType.SESSION_STATS) {
                    long nodes = s.getStats().dhtNodes();
                    // wait for at least 10 nodes in the DHT.
                    if (nodes >= 10) {
                        s1.countDown();
                    }
                }

                if (type == AlertType.DHT_GET_PEERS_REPLY) {
                    peers.addAll(((DhtGetPeersReplyAlert) alert).peers());
                    s2.countDown();
                }
            }
        };

        s.addListener(l);

        // waiting for nodes in DHT
        awaitSeconds(s1, "DHT bootstrap timeout", 10);

        s.dhtGetPeers(sha1);

        awaitMinutes(s2, "DHT get_peers timeout", 1);

        assertTrue("No peers for info hash: " + sha1, peers.size() > 0);

        s.removeListener(l);
    }
}
