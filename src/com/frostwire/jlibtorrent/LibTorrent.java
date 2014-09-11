package com.frostwire.jlibtorrent;

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

    static long time2millis(int time) {
        return ((long) time) * 1000;
    }
}
