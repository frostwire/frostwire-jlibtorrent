package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.storage_error;
import com.frostwire.jlibtorrent.swig.swig_storage;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigStoragePlugin extends swig_storage {

    private static final Logger LOG = Logger.getLogger(SwigStoragePlugin.class);

    private final StoragePlugin p;

    public SwigStoragePlugin(StoragePlugin p) {
        this.p = p;
    }

    @Override
    public void initialize(storage_error ec) {
        System.out.println("Storage: initialize");
    }

    @Override
    public int read(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        System.out.println("Storage: read");
        return 0;
    }

    @Override
    public int write(long iov_base, long iov_len, int piece, int offset, int flags, storage_error ec) {
        System.out.println("Storage: write");
        return 0;
    }
}
