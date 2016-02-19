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

    public Sha1Hash(byte[] bytes) {
        if (bytes.length != 20) {
            throw new IllegalArgumentException("bytes array must be of length 20");
        }

        this.h = new sha1_hash(Vectors.bytes2byte_vector(bytes));
    }

    public Sha1Hash(String hex) {
        this();
        if (!sha1_hash.from_hex(hex, h)) {
            throw new IllegalArgumentException("Invalid sha1 hex string");
        }
    }

    /**
     * Constructs an all-zero sha1-hash
     */
    public Sha1Hash() {
        this(new sha1_hash());
    }

    public sha1_hash swig() {
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

    public byte[] toBytes() {
        return Vectors.byte_vector2bytes(h.to_bytes());
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

    /**
     * Returns an hex representation of this hash. Internally it
     * calls {@link #toHex()}.
     *
     * @return
     */
    @Override
    public String toString() {
        return toHex();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sha1Hash)) {
            return false;
        }

        return h.op_eq(((Sha1Hash) obj).h);
    }

    @Override
    public int hashCode() {
        return h.hash_code();
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

    public static int compare(Sha1Hash h1, Sha1Hash h2) {
        return sha1_hash.compare(h1.h, h2.h);
    }
}
