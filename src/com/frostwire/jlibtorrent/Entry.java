package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.entry_list;
import com.frostwire.jlibtorrent.swig.string_entry_map;

import java.util.List;
import java.util.Map;

/**
 * The ``entry`` class represents one node in a bencoded hierarchy. It works as a
 * variant type, it can be either a list, a dictionary (``std::map``), an integer
 * or a string.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Entry {

    private final entry e;

    public Entry(entry e) {
        this.e = e;
    }

    public Entry(String s) {
        this(new entry(s));
    }

    public Entry(long n) {
        this(new entry(n));
    }

    public entry getSwig() {
        return e;
    }

    public byte[] bencode() {
        return Vectors.char_vector2bytes(e.bencode());
    }

    @Override
    public String toString() {
        return e.to_string();
    }

    public static Entry bdecode(byte[] data) {
        return new Entry(entry.bdecode(Vectors.bytes2char_vector(data)));
    }

    public static Entry fromList(List<?> list) {
        entry e = new entry(entry.data_type.list_t);

        entry_list d = e.list();
        for (Object v : list) {
            if (v instanceof String) {
                d.push_back(new entry((String) v));
            } else if (v instanceof Integer) {
                d.push_back(new entry((Integer) v));
            } else if (v instanceof Entry) {
                d.push_back(((Entry) v).getSwig());
            } else if (v instanceof entry) {
                d.push_back((entry) v);
            } else if (v instanceof List) {
                d.push_back(fromList((List<?>) v).getSwig());
            } else if (v instanceof Map) {
                d.push_back(fromMap((Map<?, ?>) v).getSwig());
            } else {
                d.push_back(new entry(v.toString()));
            }
        }

        return new Entry(e);
    }

    public static Entry fromMap(Map<?, ?> map) {
        entry e = new entry(entry.data_type.dictionary_t);

        string_entry_map d = e.dict();
        for (Map.Entry<?, ?> kv : map.entrySet()) {
            String k = kv.getKey().toString();
            Object v = kv.getValue();

            if (v instanceof String) {
                d.set(k, new entry((String) v));
            } else if (v instanceof Integer) {
                d.set(k, new entry((Integer) v));
            } else if (v instanceof Entry) {
                d.set(k, ((Entry) v).getSwig());
            } else if (v instanceof entry) {
                d.set(k, (entry) v);
            } else if (v instanceof List) {
                d.set(k, fromList((List<?>) v).getSwig());
            } else if (v instanceof Map) {
                d.set(k, fromMap((Map<?, ?>) v).getSwig());
            } else {
                d.set(k, new entry(v.toString()));
            }
        }

        return new Entry(e);
    }
}
