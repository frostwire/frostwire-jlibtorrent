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
