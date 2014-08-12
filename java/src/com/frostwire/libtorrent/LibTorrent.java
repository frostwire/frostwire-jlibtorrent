package com.frostwire.libtorrent;

import java.io.File;

public final class LibTorrent {

    static {
        System.load(new File("../jlibtorrent.dylib").getAbsolutePath());
    }

    private LibTorrent() {
    }

    public static native String version();
}
