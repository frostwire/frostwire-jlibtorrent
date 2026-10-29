package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * frostwire/frostwire-jlibtorrent#331: {@link Entry#dictionary()} returned a Map whose entrySet()
 * threw UnsupportedOperationException, breaking values(), iteration, forEach, toString() and equals()
 * (found by EntryBdecodeFuzzTest on valid data).
 */
public class EntryMapTest {

    private static Entry decode(String bencode) {
        return Entry.bdecode(bencode.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    public void everyMapViewWorks() {
        Map<String, Entry> d = decode("d1:ai1e1:b3:xyz1:cli2eee").dictionary();
        assertEquals(3, d.values().size());
        assertEquals(3, d.entrySet().size());
        List<String> keys = new ArrayList<>();
        d.forEach((k, v) -> keys.add(k));
        assertEquals(List.of("a", "b", "c"), keys);
        assertTrue(d.containsValue(decode("i1e")));
        assertTrue(d.toString(), d.toString().contains("a="));
    }

    @Test
    public void equalDataGivesEqualEntriesAndMaps() {
        Entry x = decode("d1:ai1e1:bl1:xee");
        Entry y = decode("d1:ai1e1:bl1:xee");
        assertEquals(x, y);
        assertEquals(x.hashCode(), y.hashCode());
        assertEquals(x.dictionary(), y.dictionary());
        assertNotEquals(x, decode("d1:ai2e1:bl1:xee"));
        assertNotEquals(decode("i1e"), decode("1:1"));
    }

    @Test
    public void setValueAndRemoveWriteThroughToTheNativeDictionary() {
        Entry root = decode("d1:ai1e1:bi2ee");
        Map<String, Entry> d = root.dictionary();
        for (Map.Entry<String, Entry> e : d.entrySet()) {
            if (e.getKey().equals("a")) {
                e.setValue(decode("i42e"));
            }
        }
        Iterator<Map.Entry<String, Entry>> it = d.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getKey().equals("b")) {
                it.remove();
            }
        }
        assertEquals("d1:ai42ee", new String(root.bencode(), StandardCharsets.US_ASCII));
        assertEquals(decode("i42e"), d.remove("a"));
        assertFalse(d.containsKey("a"));
        assertEquals("de", new String(root.bencode(), StandardCharsets.US_ASCII));
    }

    /**
     * frostwire/frostwire-jlibtorrent#335: keys that aren't valid UTF-8 (a v2 torrent's binary piece
     * layers keys, raw file names) came back with null values: the String form of such a key doesn't
     * convert back to the same bytes, so looking the value up by it missed.
     */
    @Test
    public void keysThatAreNotUtf8StillReachTheirValues() throws Exception {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        // Keys in bencode (byte) order: the 32-byte hash (0x80...) sorts before 0xff.
        out.write(ascii("d32:"));
        byte[] hash = new byte[32];
        for (int i = 0; i < hash.length; i++) {
            hash[i] = (byte) (0x80 + i);
        }
        out.write(hash);
        out.write(ascii("4:abcd1:"));
        out.write(0xff);
        out.write(ascii("i7ee"));
        byte[] bencoded = out.toByteArray();

        Entry root = Entry.bdecode(bencoded);
        Map<String, Entry> d = root.dictionary();
        assertEquals(2, d.size());
        for (Entry value : d.values()) {
            assertTrue("no null values", value != null);
        }
        for (Map.Entry<String, Entry> e : d.entrySet()) {
            if (e.getValue().swig().type() == com.frostwire.jlibtorrent.swig.entry.data_type.int_t) {
                assertEquals(7, e.getValue().integer());
                e.setValue(decode("i8e"));
            }
        }
        byte[] expected = bencoded.clone();
        int intAt = new String(bencoded, StandardCharsets.ISO_8859_1).indexOf("i7e");
        expected[intAt + 1] = '8';
        assertTrue("setValue writes through and the keys keep their bytes",
                java.util.Arrays.equals(expected, root.bencode()));
    }

    private static byte[] ascii(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }
}
