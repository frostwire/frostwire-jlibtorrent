package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Entry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EntryTest {

    public static void main(String[] args) throws Throwable {

        Map<String, Object> m = new HashMap<String, Object>();

        m.put("a", 1);
        m.put("b", "b");
        m.put("c", new Entry("es"));

        Entry e = Entry.fromMap(m);

        m.put("m", e);

        e = Entry.fromMap(m);

        System.out.println(e.toString());
    }
}
