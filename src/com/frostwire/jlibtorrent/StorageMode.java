/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.storage_mode_t;

/**
 * @author gubatron
 * @author aldenml
 */
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
