package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.string_entry_map;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionTest {

    private static Session session;

    public static synchronized Session session() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    @Test
    public void testIsValid() {
        assertTrue(session().isValid());
    }

    @Test
    public void testSaveState() {
        // get only dht settings
        byte[] data = session().saveState(SessionHandle.SaveStateFlags.SAVE_DHT_SETTINGS.swig());
        entry e = entry.bdecode(Vectors.bytes2byte_vector(data));
        string_entry_map m = e.dict();
        assertEquals(1, m.size());
        assertTrue(m.has_key("dht"));
        assertNotEquals(1234, m.get("dht").dict().get("max_dht_items").integer());

        // get all settings
        data = session().saveState();
        e = entry.bdecode(Vectors.bytes2byte_vector(data));
        m = e.dict();
        assertTrue(m.size() > 1);
        m.get("dht").dict().set("max_dht_items", new entry(1234));
        session().loadState(Vectors.byte_vector2bytes(e.bencode()));

        data = session().saveState(SessionHandle.SaveStateFlags.SAVE_DHT_SETTINGS.swig());
        e = entry.bdecode(Vectors.bytes2byte_vector(data));
        m = e.dict();
        assertEquals(1234, m.get("dht").dict().get("max_dht_items").integer());

    }
}
