package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_error;

/**
 * @author gubatron
 * @author aldenml
 */
public final class StorageError {

    private final storage_error e;

    public StorageError(storage_error e) {
        this.e = e;
    }

    public storage_error swig() {
        return e;
    }
}
