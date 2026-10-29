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
}
