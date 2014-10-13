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

    /**
     * Returns the hex representation of this has.
     * <p/>
     * This method uses internally the libtorrent to_hex function.
     *
     * @return
     */
    public String toHex() {
        return h.to_hex();
    }

    @Override
    public String toString() {
        return toHex();
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
