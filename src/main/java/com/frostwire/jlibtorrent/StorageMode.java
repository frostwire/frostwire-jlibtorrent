package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

/**
 * Types of storage allocation used for {@link AddTorrentParams#storageMode(StorageMode)}.
 *
 * @author gubatron
 * @author aldenml
 */
public enum StorageMode {

    /**
     * All pieces will be written to their final position, all files will be
     * allocated in full when the torrent is first started. This is done with
     * {@code fallocate()} and similar calls. This mode minimizes fragmentation.
     */
    STORAGE_MODE_ALLOCATE(storage_mode_t.storage_mode_allocate.swigValue()),

    /**
     * All pieces will be written to the place where they belong and sparse files
     * will be used. This is the recommended, and default mode.
     */
    STORAGE_MODE_SPARSE(storage_mode_t.storage_mode_sparse.swigValue()),

    /**
     * internal
     */
    INTERNAL_STORAGE_MODE_COMPACT_DEPRECATED(storage_mode_t.internal_storage_mode_compact_deprecated.swigValue()),

    /**
     *
     */
    UNKNOWN(-1);

    StorageMode(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return
     */
    public int swig() {
        return swigValue;
    }

    /**
     * @param swigValue
     * @return
     */
    public static StorageMode fromSwig(int swigValue) {
        StorageMode[] enumValues = StorageMode.class.getEnumConstants();
        for (StorageMode ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
