package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.plugins.DhtStorageBase;
import com.frostwire.jlibtorrent.swig.entry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class DhtStorageBaseTest {

    private static final Sha1Hash n1 = new Sha1Hash("5fbfbff10c5d6a4ec8a88e4c6ab4c28b95eee401");

    @Test
    public void testAnnouncePeer() {
        DhtSettings sett = testSettings();
        DhtStorageBase s = new DhtStorageBase(new Sha1Hash(), sett);

        entry peers = new entry();
        s.getPeers(n1, false, false, peers);

        assertEquals(peers.get("n").type(), entry.data_type.undefined_t);
        assertEquals(peers.get("values").type(), entry.data_type.undefined_t);
/*
    tcp::endpoint p1 = ep("124.31.75.21", 1);
	tcp::endpoint p2 = ep("124.31.75.22", 1);
	tcp::endpoint p3 = ep("124.31.75.23", 1);
	tcp::endpoint p4 = ep("124.31.75.24", 1);

	s->announce_peer(n1, p1, "torrent_name", false);
	s->get_peers(n1, false, false, peers);
	TEST_EQUAL(peers["n"].string(), "torrent_name")
	TEST_EQUAL(peers["values"].list().size(), 1)

	s->announce_peer(n2, p2, "torrent_name1", false);
	s->announce_peer(n2, p3, "torrent_name1", false);
	s->announce_peer(n3, p4, "torrent_name2", false);
	bool r = s->get_peers(n1, false, false, peers);
	TEST_CHECK(!r);
         */
    }

    private static DhtSettings testSettings() {
        DhtSettings sett = new DhtSettings();
        sett.maxTorrents(2);
        sett.setMaxDhtItems(2);
        sett.itemLifetime(120 * 60);
        return sett;
    }
}
