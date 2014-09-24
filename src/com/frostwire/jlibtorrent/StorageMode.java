package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

/**
 * @author gubatron
 * @author aldenml
 */
public enum StorageMode {

    STORAGE_MODE_ALLOCATE(storage_mode_t.storage_mode_allocate),
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
