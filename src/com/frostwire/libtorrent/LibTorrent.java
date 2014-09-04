package com.frostwire.libtorrent;

import com.frostwire.libtorrent.swig.libtorrent;

import java.io.File;

public final class LibTorrent {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private LibTorrent() {
    }

    public static String version() {
        return libtorrent.LIBTORRENT_VERSION;
    }
}
