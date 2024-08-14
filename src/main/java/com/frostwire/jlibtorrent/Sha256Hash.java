package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha256_hash;
import com.frostwire.jlibtorrent.swig.sha256_hash_vector;

import java.util.ArrayList;

/**
 * SHA-256 digest or any other kind of 32 byte sequence.
 */
public final class Sha256Hash implements Comparable<Sha256Hash>, Cloneable {
    private final sha256_hash h;

    /**
     * @param h native object
     */
    public Sha256Hash(sha256_hash h) {
        this.h = h;
    }

    /**
     * Constructs an all-zero sha256-hash
     */
    public Sha256Hash() {
        this(new sha256_hash());
    }

    /**
     * @param bytes hash as an array of bytes
     */
    public Sha256Hash(byte[] bytes) {
        if (bytes.length != 32) {
            throw new IllegalArgumentException("Sha256Hash(byte[]) bytes array must be of length 32");
        }

        this.h = new sha256_hash(Vectors.bytes2byte_vector(bytes));
    }

    /**
     * @param hex hex coded representation of the hash
     */
    public Sha256Hash(String hex) {
        this(Hex.decode(hex));
    }

    /**
     * set the sha256-hash to all zeroes.
     */
    public void clear() {
        h.clear();
    }

    /**
     * return true if the sha256-hash is all zero.
     *
     * @return true if zero
     */
    public boolean isAllZeros() {
        return h.is_all_zeros();
    }

    /**
     * @return the number of leading zeroes
     */
    public int countLeadingZeroes() {
        return h.count_leading_zeroes();
    }

    /**
     * Returns the hex representation of this has.
     * <p>
     * This method uses internally the libtorrent to_hex function.
     *
     * @return the hex representation
     */
    public String toHex() {
        return h.to_hex();
    }

    /**
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int compareTo(Sha256Hash o) {
        return sha256_hash.compare(this.h, o.h);
    }

    /**
     * Returns an hex representation of this hash. Internally it
     * calls {@link #toHex()}.
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString() {
        return toHex();
    }

    /**
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sha256Hash)) {
            return false;
        }

        return h.op_eq(((Sha256Hash) obj).h);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return h.hash_code();
    }

    @Override
    public Sha256Hash clone() {
        return new Sha256Hash(new sha256_hash(h));
    }

    /**
     * returns an all-F sha256-hash. i.e. the maximum value
     * representable by a 256 bit number (32 bytes). This is
     * a static member function.
     *
     * @return the maximum number
     */
    public static Sha256Hash max() {
        return new Sha256Hash(sha256_hash.max());
    }

    /**
     * returns an all-zero sha256-hash. i.e. the minimum value
     * representable by a 256 bit number (32 bytes). This is
     * a static member function.
     *
     * @return the minimum number (zero)
     */
    public static Sha256Hash min() {
        return new Sha256Hash(sha256_hash.min());
    }

    static ArrayList<Sha256Hash> convert(sha256_hash_vector v) {
        ArrayList<Sha256Hash> l = new ArrayList<>(v.size());

        for (sha256_hash hash : v) {
            l.add(new Sha256Hash(hash));
        }

        return l;
    }
}