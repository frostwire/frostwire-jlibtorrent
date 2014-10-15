package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.swig.string_entry_map;
import com.frostwire.jlibtorrent.swig.string_vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        List<Object> l = new ArrayList<Object>();

        l.add("l1");
        l.add("l2");

        m.put("m", e);
        m.put("l", l);

        e = Entry.fromMap(m);
        System.out.println(e);

        string_entry_map dict = e.getSwig().dict();
        string_vector keys = dict.keys();
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            System.out.println(k + "=" + dict.get(k).to_string());
        }
    }
}
