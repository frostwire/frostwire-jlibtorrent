package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.plugins.DhtStorageBase;
import com.frostwire.jlibtorrent.swig.entry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public class DhtStorageBaseTest {

    private static final Sha1Hash n1 = new Sha1Hash("5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee401");
    private static final Sha1Hash n2 = new Sha1Hash("5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee402");
    private static final Sha1Hash n3 = new Sha1Hash("5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee403");
    private static final Sha1Hash n4 = new Sha1Hash("5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee404");

    @Test
    public void testAnnouncePeer() {
        DhtSettings sett = testSettings();
        DhtStorageBase s = new DhtStorageBase(new Sha1Hash(), sett);

        entry peers = new entry();
        s.getPeers(n1, false, false, peers);

        assertEquals(peers.get("n").type(), entry.data_type.undefined_t);
        assertEquals(peers.get("values").type(), entry.data_type.undefined_t);

        TcpEndpoint p1 = new TcpEndpoint("124.31.75.21", 1);
        TcpEndpoint p2 = new TcpEndpoint("124.31.75.22", 1);
        TcpEndpoint p3 = new TcpEndpoint("124.31.75.23", 1);
        TcpEndpoint p4 = new TcpEndpoint("124.31.75.24", 1);

        s.announcePeer(n1, p1, "torrent_name", false);
        s.getPeers(n1, false, false, peers);
        assertEquals(peers.get("n").string(), "torrent_name");
        assertEquals(peers.get("values").list().size(), 1);

        s.announcePeer(n2, p2, "torrent_name1", false);
        s.announcePeer(n2, p3, "torrent_name1", false);
        s.announcePeer(n3, p4, "torrent_name2", false);
        boolean r = s.getPeers(n1, false, false, peers);
        assertFalse(r);
    }

    @Test
    public void testPutImmutableItem() {
        DhtSettings sett = testSettings();
        DhtStorageBase s = new DhtStorageBase(new Sha1Hash(), sett);

        entry item = new entry();
        boolean r = s.getImmutableItem(n4, item);
        assertFalse(r);

        byte[] data = new byte[]{1, 2, 3};
        Address addr = new Address("124.31.75.21");

        s.putImmutableItem(n4, data, addr);
        r = s.getImmutableItem(n4, item);
        assertTrue(r);

        s.putImmutableItem(n1, data, addr);
        s.putImmutableItem(n2, data, addr);
        s.putImmutableItem(n3, data, addr);
        r = s.getImmutableItem(n1, item);
        assertFalse(r);
    }

    @Test
    public void testMutableItem() {
        DhtSettings sett = testSettings();
        DhtStorageBase s = new DhtStorageBase(new Sha1Hash(), sett);

        entry item = new entry();
        boolean r = s.getMutableItem(n4, 0, false, item);
        assertFalse(r);

        byte[] data = new byte[]{1, 2, 3};
        Address addr = new Address("124.31.75.21");

        byte[] public_key = new byte[Ed25519.PUBLIC_KEY_SIZE];
        byte[] signature = new byte[Ed25519.SIGNATURE_SIZE];
        byte[] salt = new byte[]{1, 2, 3, 4};
        s.putMutableItem(n4, data, signature, 1, public_key, salt, addr);
        r = s.getMutableItem(n4, 0, false, item);
        assertTrue(r);
    }

    private static DhtSettings testSettings() {
        DhtSettings sett = new DhtSettings();
        sett.maxTorrents(2);
        sett.maxDhtItems(2);
        sett.itemLifetime(120 * 60);
        return sett;
    }
}
