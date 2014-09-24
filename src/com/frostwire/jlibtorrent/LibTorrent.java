package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
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
