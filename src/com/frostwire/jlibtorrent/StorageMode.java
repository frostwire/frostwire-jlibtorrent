package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

public enum StorageMode {

    STORAGE_MODE_ALLOCATE,
    STORAGE_MODE_SPARSE,
    INTERNAL_STORAGE_MODE_COMPACT_DEPRECATED;

    public static StorageMode fromSwig(storage_mode_t mode) {
        switch (mode) {
            case storage_mode_allocate:
                return STORAGE_MODE_ALLOCATE;
            case storage_mode_sparse:
                return STORAGE_MODE_SPARSE;
            case internal_storage_mode_compact_deprecated:
                return INTERNAL_STORAGE_MODE_COMPACT_DEPRECATED;
            default:
                throw new IllegalArgumentException("Enum value not supported");
        }
    }
}
