package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EntryTest2 {

    public static void main(String[] args) throws Throwable {

        entry url_list = new entry(entry.data_type.list_t);
        url_list.list().push_back(new entry("http://server1.com"));
        url_list.list().push_back(new entry("http://server2.com"));
        entry swig_entry = new entry(entry.data_type.dictionary_t);
        swig_entry.dict().set("url-list", url_list);
        Entry e = new Entry(swig_entry);

        byte[] bencoded_entry = e.bencode();
        lazy_entry lentry = new lazy_entry();
        char_vector charvecBuffer = Vectors.bytes2char_vector(bencoded_entry);
        error_code ec = new error_code();
        lazy_entry.bdecode(charvecBuffer, lentry, ec);
        System.out.println(libtorrent.print_entry(lentry));
    }
}
