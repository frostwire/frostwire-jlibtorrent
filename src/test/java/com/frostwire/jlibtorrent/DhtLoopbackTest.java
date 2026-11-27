package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.DhtAnnounceAlert;
import com.frostwire.jlibtorrent.alerts.DhtBootstrapAlert;
import com.frostwire.jlibtorrent.alerts.DhtDirectResponseAlert;
import com.frostwire.jlibtorrent.alerts.DhtGetPeersAlert;
import com.frostwire.jlibtorrent.alerts.DhtOutgoingGetPeersAlert;
import com.frostwire.jlibtorrent.alerts.DhtPutAlert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Two DHT nodes on the loopback interface store and find items and peers; no public node is involved.
 *
 * @author gubatron
 * @author aldenml
 */
public class DhtLoopbackTest {

    private static void connect(LoopbackSession a, LoopbackSession b) {
        SessionHandle handle = new SessionHandle(b.sm.swig());
        handle.addDhtNode(new Pair<>("127.0.0.1", a.listenPort()));
        LoopbackSession.awaitCondition("the nodes to know each other", () -> {
            b.sm.postDhtStats();
            a.sm.postDhtStats();
            return b.sm.dhtNodes() > 0 && a.sm.dhtNodes() > 0;
        });
    }

    @Test
    public void immutableAndMutableItemsAreStoredAndFound() {
        try (LoopbackSession a = LoopbackSession.startDht(true);
             LoopbackSession b = LoopbackSession.startDht(true)) {
            assertTrue(a.sm.isDhtRunning());
            connect(a, b);

            CompletableFuture<Boolean> put = b.expect(DhtPutAlert.class);
            Sha1Hash target = b.sm.dhtPutItem(new Entry("hello dht"));
            assertNotNull(target);
            LoopbackSession.get(put, "the immutable put to finish");
            // the item went to the closest node it knew, the other one: asking again reaches it there
            Entry got = b.sm.dhtGetItem(target, 30);
            assertNotNull(got);
            assertEquals("hello dht", got.string());

            byte[] seed = Ed25519.createSeed();
            Pair<byte[], byte[]> keys = Ed25519.createKeypair(seed);
            byte[] salt = "salty".getBytes(StandardCharsets.UTF_8);
            CompletableFuture<Boolean> mutablePut = b.expect(DhtPutAlert.class, x -> true);
            b.sm.dhtPutItem(keys.first, keys.second, new Entry("mutable value"), salt);
            LoopbackSession.get(mutablePut, "the mutable put to finish");
            SessionManager.MutableItem item = b.sm.dhtGetItem(keys.first, salt, 30);
            assertNotNull("the mutable item should be found", item);
            assertEquals("mutable value", item.item.string());
            assertEquals(Ed25519.SIGNATURE_SIZE, item.signature.length);
            assertTrue(item.seq >= 0);
            assertTrue(Ed25519.verify(item.signature, signedBytes(salt, item), keys.first));

            assertEquals(Collections0.emptyList(), a.accessorFailures());
            assertEquals(Collections0.emptyList(), b.accessorFailures());
        }
    }

    private static byte[] signedBytes(byte[] salt, SessionManager.MutableItem item) {
        // BEP 44: the signature covers "4:salt<len>:<salt>3:seqi<seq>e1:v<bencoded value>"
        byte[] value = item.item.bencode();
        String head = "4:salt" + salt.length + ":" + new String(salt, StandardCharsets.UTF_8)
                + "3:seqi" + item.seq + "e1:v";
        byte[] h = head.getBytes(StandardCharsets.UTF_8);
        byte[] all = new byte[h.length + value.length];
        System.arraycopy(h, 0, all, 0, h.length);
        System.arraycopy(value, 0, all, h.length, value.length);
        return all;
    }

    private static final class Collections0 {
        static java.util.List<String> emptyList() {
            return new ArrayList<>();
        }
    }

    @Test
    public void announcedPeersAreFoundAndRepliesAreSeen() {
        try (LoopbackSession a = LoopbackSession.startDht(true);
             LoopbackSession b = LoopbackSession.startDht(true)) {
            connect(a, b);
            Sha1Hash infoHash = new Sha1Hash(TestTorrents.bytes(20, 3));

            // the announce is stored by the other node, which sees it as an incoming request
            CompletableFuture<Boolean> announced = a.expect(DhtAnnounceAlert.class);
            b.sm.dhtAnnounce(infoHash, 4242, 0);
            LoopbackSession.get(announced, "the announce to arrive");
            b.sm.dhtAnnounce(infoHash);

            CompletableFuture<Boolean> asked = a.expect(DhtGetPeersAlert.class);
            CompletableFuture<Boolean> outgoing = b.expect(DhtOutgoingGetPeersAlert.class);
            ArrayList<TcpEndpoint> peers = b.sm.dhtGetPeers(infoHash, 30);
            LoopbackSession.get(asked, "the incoming get_peers query");
            LoopbackSession.get(outgoing, "the outgoing get_peers query");
            boolean found = false;
            for (TcpEndpoint p : peers) {
                found |= p.port() == 4242;
            }
            assertTrue("the announced peer should be found: " + peers, found);
        }
    }

    @Test
    public void aDirectRequestGetsADirectResponse() {
        try (LoopbackSession a = LoopbackSession.startDht(true);
             LoopbackSession b = LoopbackSession.startDht(true)) {
            connect(a, b);
            Map<String, Object> args = new LinkedHashMap<>();
            args.put("id", new String(TestTorrents.bytes(20, 9), StandardCharsets.ISO_8859_1));
            Map<String, Object> query = new LinkedHashMap<>();
            query.put("q", "ping");
            query.put("a", args);

            CompletableFuture<Boolean> response = b.expect(DhtDirectResponseAlert.class);
            SessionHandle handle = new SessionHandle(b.sm.swig());
            handle.dhtDirectRequest(new UdpEndpoint("127.0.0.1", a.listenPort()), Entry.fromMap(query), 77L);
            handle.dhtDirectRequest(new UdpEndpoint("127.0.0.1", a.listenPort()), Entry.fromMap(query));
            LoopbackSession.get(response, "the direct response");
        }
    }
}
