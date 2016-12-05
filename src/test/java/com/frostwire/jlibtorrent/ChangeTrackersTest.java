package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class ChangeTrackersTest {

    @Test
    public void testChangeTrackersUsingCreateTorrent() throws IOException {
        byte[] torrentBytes = Utils.resourceBytes("test4.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        // do we have any tracker
        assertTrue(ti.trackers().size() > 0);

        entry e = entry.bdecode(Vectors.bytes2byte_vector(torrentBytes));
        string_entry_map m = e.dict();
        if (m.has_key("announce")) {
            m.erase("announce");
        }
        if (m.has_key("announce-list")) {
            m.erase("announce-list");
        }

        ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(e.bencode()));
        // did we remove all trackers
        assertEquals(ti.trackers().size(), 0);

        create_torrent c = new create_torrent(ti.swig());

        c.add_tracker("http://a:6969/announce", 0);
        c.add_tracker("http://b:6969/announce", 1);

        e = c.generate();
        ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(e.bencode()));
        ArrayList<AnnounceEntry> trackers = ti.trackers();
        // do we have exactly the two added trackers
        assertEquals(trackers.size(), 2);
        assertEquals(trackers.get(0).url(), "http://a:6969/announce");
        assertEquals(trackers.get(1).url(), "http://b:6969/announce");
    }

    @Test
    public void testChangeTrackersLowLevel() throws IOException {
        byte[] torrentBytes = Utils.resourceBytes("test4.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        // do we have any tracker
        assertTrue(ti.trackers().size() > 0);

        entry e = entry.bdecode(Vectors.bytes2byte_vector(torrentBytes));
        string_entry_map m = e.dict();

        // remove trackers
        if (m.has_key("announce")) {
            m.erase("announce");
        }
        if (m.has_key("announce-list")) {
            m.erase("announce-list");
        }

        // add trackers
        String[] tks = new String[]{"http://a:6969/announce", "http://b:6969/announce"};
        entry_vector l = new entry_vector();
        l.push_back(new entry(tks[0]));
        m.set("announce", new entry(l));

        entry_vector tl = new entry_vector();
        for (int i = 0; i < tks.length; i++) {
            l.clear();
            l.push_back(new entry(tks[i]));
            tl.push_back(new entry(l));
        }
        m.set("announce-list", new entry(tl));

        ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(e.bencode()));
        ArrayList<AnnounceEntry> trackers = ti.trackers();
        // do we have exactly the two added trackers
        assertEquals(trackers.size(), 2);
        assertEquals(trackers.get(0).url(), "http://a:6969/announce");
        assertEquals(trackers.get(1).url(), "http://b:6969/announce");
    }

    @Test
    public void testChangeTrackersWithTorrentInfo() throws IOException {
        byte[] torrentBytes = Utils.resourceBytes("test4.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        // do we have any tracker
        assertTrue(ti.trackers().size() > 0);

        ti.clearTrackers();

        // did we remove all trackers
        assertEquals(ti.trackers().size(), 0);

        ti.addTracker("http://a:6969/announce", 0);
        ti.addTracker("http://b:6969/announce", 1);

        ArrayList<AnnounceEntry> trackers = ti.trackers();
        // do we have exactly the two added trackers
        assertEquals(trackers.size(), 2);
        assertEquals(trackers.get(0).url(), "http://a:6969/announce");
        assertEquals(trackers.get(1).url(), "http://b:6969/announce");
    }
}
