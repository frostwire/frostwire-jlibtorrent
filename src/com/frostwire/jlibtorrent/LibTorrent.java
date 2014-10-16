package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LibTorrent {

    private LibTorrent() {
    }

    public static String version() {
        return libtorrent.LIBTORRENT_VERSION;
    }

    /**
     * returns the string in hexadecimal representation using the internal libtorrent to_hex
     * function.
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {
        return libtorrent.to_hex(Vectors.bytes2char_vector(data));
    }
}
