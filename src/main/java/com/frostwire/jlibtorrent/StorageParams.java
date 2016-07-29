package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_params;

/**
 * @author gubatron
 * @author aldenml
 */
public final class StorageParams {

    private final storage_params p;

    public StorageParams(storage_params p) {
        this.p = p;
    }

    public storage_params swig() {
        return p;
    }
}
