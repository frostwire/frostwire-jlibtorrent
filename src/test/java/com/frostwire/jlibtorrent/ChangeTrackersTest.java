package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.create_torrent;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.string_entry_map;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class ChangeTrackersTest {

    @Test
    public void testChangeTrackersUsingCreateTorrent() throws IOException {
        byte[] torrentBytes = Utils.getResourceBytes("test4.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        // do we have any tracker
        assertTrue(ti.trackers().size() > 0);

        entry e = entry.bdecode(Vectors.bytes2byte_vector(torrentBytes));
        string_entry_map m = e.dict();
        if (m.has_key("announce")) {
            m.del("announce");
        }
        if (m.has_key("announce-list")) {
            m.del("announce-list");
        }

        ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(e.bencode()));
        // did we remove all trackers
        assertEquals(ti.trackers().size(), 0);

        create_torrent c = new create_torrent(ti.getSwig());

        c.add_tracker("http://a:6969/announce", 0);
        c.add_tracker("http://b:6969/announce", 1);

        e = c.generate();
        ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(e.bencode()));
        List<AnnounceEntry> trackers = ti.trackers();
        // do we have exactly the two added trackers
        assertEquals(trackers.size(), 2);
        assertEquals(trackers.get(0).getUrl(), "http://a:6969/announce");
        assertEquals(trackers.get(1).getUrl(), "http://b:6969/announce");
    }
}
