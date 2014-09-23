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

import com.frostwire.jlibtorrent.swig.sha1_hash;

/**
 * This type holds a SHA-1 digest or any other kind of 20 byte
 * sequence. It implements a number of convenience functions, such
 * as bit operations, comparison operators etc.
 * <p/>
 * In libtorrent it is primarily used to hold info-hashes, piece-hashes,
 * peer IDs, node IDs etc.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Sha1Hash {

    private final sha1_hash h;

    public Sha1Hash(sha1_hash h) {
        this.h = h;
    }

    public Sha1Hash(String hex) {
        this();
        sha1_hash.from_hex(hex, h);
    }

    /**
     * Constructs an all-sero sha1-hash
     */
    public Sha1Hash() {
        this(new sha1_hash());
    }

    public sha1_hash getSwig() {
        return h;
    }

    /**
     * set the sha1-hash to all zeroes.
     */
    public void clear() {
        h.clear();
    }

    /**
     * return true if the sha1-hash is all zero.
     *
     * @return
     */
    public boolean isAllZeros() {
        return h.is_all_zeros();
    }

    @Override
    public String toString() {
        return h.to_hex();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sha1Hash)) {
            return false;
        }

        return h.op_eq(((Sha1Hash) obj).getSwig());
    }

    /**
     * returns an all-F sha1-hash. i.e. the maximum value
     * representable by a 160 bit number (20 bytes). This is
     * a static member function.
     *
     * @return
     */
    public static Sha1Hash max() {
        return new Sha1Hash(sha1_hash.max());
    }

    /**
     * returns an all-zero sha1-hash. i.e. the minimum value
     * representable by a 160 bit number (20 bytes). This is
     * a static member function.
     *
     * @return
     */
    public static Sha1Hash min() {
        return new Sha1Hash(sha1_hash.min());
    }
}
