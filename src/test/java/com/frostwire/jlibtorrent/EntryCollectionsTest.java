package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link Entry}: scalar constructors, the live list and dictionary views and the
 * {@code fromList}/{@code fromMap} builders.
 *
 * @author gubatron
 * @author aldenml
 */
public class EntryCollectionsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void scalarEntries() {
        Entry s = new Entry("hello");
        assertEquals("hello", s.string());
        assertArrayEquals("hello".getBytes(StandardCharsets.UTF_8), s.bytes());
        assertArrayEquals("5:hello".getBytes(StandardCharsets.UTF_8), s.bencode());
        assertNotNull(s.swig());

        Entry n = new Entry(42L);
        assertEquals(42L, n.integer());
        assertArrayEquals("i42e".getBytes(StandardCharsets.UTF_8), n.bencode());
        assertTrue(n.toString(), n.toString().contains("42"));
    }

    @Test
    public void equalityAndHashCodeFollowTheEncoding() {
        Entry a = new Entry("x");
        Entry b = new Entry("x");
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Entry("y"));
        assertNotEquals(a, new Entry(1L));
        assertNotEquals(a, "x");
        assertNotEquals(new Entry("1"), new Entry(1L));
    }

    @Test
    public void listViewIsLive() {
        Entry list = Entry.fromList(Arrays.asList("a", "b"));
        List<Entry> view = list.list();
        assertEquals(2, view.size());
        assertFalse(view.isEmpty());
        assertEquals("a", view.get(0).string());

        assertTrue(view.add(new Entry("c")));
        assertEquals(3, list.list().size());
        assertEquals("c", list.list().get(2).string());

        view.clear();
        assertTrue(view.isEmpty());
        assertEquals(0, view.size());
        assertArrayEquals("le".getBytes(StandardCharsets.UTF_8), list.bencode());
    }

    @Test
    public void dictionaryViewIsLiveAndSortedByKeyBytes() {
        Entry d = Entry.fromMap(Collections.<String, Object>emptyMap());
        Map<String, Entry> view = d.dictionary();
        assertTrue(view.isEmpty());
        assertEquals(0, view.size());

        assertNull(view.put("b", new Entry("2")));
        assertNull(view.put("a", new Entry("1")));
        assertEquals("1", view.put("a", new Entry("one")).string());
        assertEquals(2, view.size());
        assertTrue(view.containsKey("a"));
        assertFalse(view.containsKey("z"));
        assertNull(view.get("z"));
        assertEquals("one", view.get("a").string());
        assertEquals(new HashSet<>(Arrays.asList("a", "b")), view.keySet());

        // the encoding is canonical: keys come out sorted
        assertEquals("d1:a3:one1:b1:2e", new String(d.bencode(), StandardCharsets.UTF_8));

        assertNull(view.remove("z"));
        assertEquals("2", view.remove("b").string());
        assertEquals(1, view.size());

        view.clear();
        assertTrue(view.isEmpty());
    }

    @Test
    public void dictionaryEntrySetIterates() {
        Entry d = Entry.fromMap(new LinkedHashMap<String, Object>() {{
            put("b", "2");
            put("a", "1");
            put("c", "3");
        }});
        Map<String, Entry> view = d.dictionary();

        StringBuilder keys = new StringBuilder();
        for (Map.Entry<String, Entry> e : view.entrySet()) {
            keys.append(e.getKey());
            assertTrue(e.toString().startsWith(e.getKey() + "="));
            assertEquals(e, e);
            assertEquals(e.hashCode(), e.hashCode());
        }
        assertEquals("abc", keys.toString());
        assertEquals(3, view.entrySet().size());

        Iterator<Map.Entry<String, Entry>> it = view.entrySet().iterator();
        try {
            it.remove();
            fail("remove before next must be rejected");
        } catch (IllegalStateException expected) {
            // expected
        }
        Map.Entry<String, Entry> first = it.next();
        assertEquals("a", first.getKey());
        assertEquals("1", first.getValue().string());
        Map.Entry<String, Entry> same = new java.util.AbstractMap.SimpleEntry<>("a", new Entry("1"));
        assertEquals(first, same);
        assertNotEquals(first, "a=1");
        assertNotEquals(first, new java.util.AbstractMap.SimpleEntry<>("a", new Entry("2")));
        assertNotEquals(first, new java.util.AbstractMap.SimpleEntry<>("q", new Entry("1")));
        assertEquals("1", first.setValue(new Entry("uno")).string());
        it.remove();
        assertFalse(view.containsKey("a"));
        assertEquals(2, view.size());
    }

    @Test
    public void dictionaryKeysAndLookupByBytes() {
        Entry d = Entry.fromMap(new LinkedHashMap<String, Object>() {{
            put("k1", "v1");
            put("k2", 7);
        }});
        List<byte[]> keys = d.dictionaryKeys();
        assertEquals(2, keys.size());
        assertArrayEquals("k1".getBytes(StandardCharsets.UTF_8), keys.get(0));
        assertEquals("v1", d.get("k1".getBytes(StandardCharsets.UTF_8)).string());
        assertEquals(7L, d.get("k2".getBytes(StandardCharsets.UTF_8)).integer());
        assertNull(d.get("nope".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void fromListMapsEveryElementType() {
        Entry inner = new Entry("entry");
        Entry list = Entry.fromList(Arrays.asList(
                "str",
                5,
                inner,
                inner.swig(),
                Arrays.asList("nested", 1),
                Collections.singletonMap("k", "v"),
                new StringBuilder("other")));
        List<Entry> l = list.list();
        assertEquals(7, l.size());
        assertEquals("str", l.get(0).string());
        assertEquals(5L, l.get(1).integer());
        assertEquals("entry", l.get(2).string());
        assertEquals("entry", l.get(3).string());
        assertEquals("nested", l.get(4).list().get(0).string());
        assertEquals("v", l.get(5).dictionary().get("k").string());
        assertEquals("other", l.get(6).string());
    }

    @Test
    public void fromMapMapsEveryValueType() {
        Entry inner = new Entry("entry");
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("str", "s");
        m.put("int", 9);
        m.put("entry", inner);
        m.put("swig", inner.swig());
        m.put("list", Arrays.asList(1, 2));
        m.put("map", Collections.singletonMap("deep", "x"));
        m.put("other", new StringBuilder("sb"));
        Map<String, Entry> d = Entry.fromMap(m).dictionary();
        assertEquals(7, d.size());
        assertEquals("s", d.get("str").string());
        assertEquals(9L, d.get("int").integer());
        assertEquals("entry", d.get("entry").string());
        assertEquals("entry", d.get("swig").string());
        assertEquals(2, d.get("list").list().size());
        assertEquals("x", d.get("map").dictionary().get("deep").string());
        assertEquals("sb", d.get("other").string());
    }

    @Test
    public void bdecodeFromAFile() throws IOException {
        File f = folder.newFile("d.bin");
        Utils.writeByteArrayToFile(f, "d1:a1:be".getBytes(StandardCharsets.UTF_8), false);
        Entry e = Entry.bdecode(f);
        assertEquals("b", e.dictionary().get("a").string());

        try {
            Entry.bdecode(new File(folder.getRoot(), "missing.bin"));
            fail("a missing file can't be read");
        } catch (IOException expected) {
            // expected
        }
    }
}
