package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

/**
 * types of storage allocation used for add_torrent_params::storage_mode.
 *
 * @author gubatron
 * @author aldenml
 */
public enum StorageMode {

    /**
     * All pieces will be written to their final position, all files will be
     * allocated in full when the torrent is first started. This is done with
     * ``fallocate()`` and similar calls. This mode minimizes fragmentation.
     */
    STORAGE_MODE_ALLOCATE(storage_mode_t.storage_mode_allocate),

    /**
     * All pieces will be written to the place where they belong and sparse files
     * will be used. This is the recommended, and default mode.
     */
    STORAGE_MODE_SPARSE(storage_mode_t.storage_mode_sparse);

    private StorageMode(storage_mode_t swigObj) {
        this.swigObj = swigObj;
    }

    private final storage_mode_t swigObj;

    public storage_mode_t getSwig() {
        return swigObj;
    }

    public static StorageMode fromSwig(storage_mode_t swigObj) {
        StorageMode[] enumValues = StorageMode.class.getEnumConstants();
        for (StorageMode ev : enumValues) {
            if (ev.getSwig() == swigObj) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + StorageMode.class + " with swig value " + swigObj);
    }
}
