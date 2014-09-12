package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.libtorrent;

public final class LibTorrent {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private LibTorrent() {
    }

    public static String version() {
        return libtorrent.LIBTORRENT_VERSION;
    }

    public static byte[] bencode(entry e) {
        char_vector vector = libtorrent.entry_bencode(e);

        int size = (int) vector.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = (byte) vector.get(i);
        }

        return arr;
    }

    static long time2millis(int time) {
        return ((long) time) * 1000;
    }
}
