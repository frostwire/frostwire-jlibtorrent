package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.sha1_hash_vector;

import java.util.ArrayList;

/**
 * Immutable 20-byte SHA-1 hash wrapper.
 * <p>
 * {@code Sha1Hash} represents a 160-bit SHA-1 cryptographic hash. In the bittorrent world,
 * it's used to uniquely identify content and torrents:
 * <ul>
 *   <li><b>Info-hash V1:</b> SHA-1 of the torrent's info dictionary (uniquely identifies the torrent)</li>
 *   <li><b>Piece hashes:</b> SHA-1 of each 16KB-16MB block of data (verifies downloaded content)</li>
 *   <li><b>Peer IDs:</b> 20-byte identifier for each peer client</li>
 *   <li><b>DHT Node IDs:</b> 20-byte identifiers for DHT network nodes</li>
 * </ul>
 * <p>
 * <b>Creating Sha1Hash Objects:</b>
 * <pre>
 * // From hex string (40 characters)
 * Sha1Hash hash = new Sha1Hash("d8e8fca2dc0f896fd7cb4cb0031ba249");
 *
 * // From 20-byte array
 * byte[] bytes = new byte[20];
 * Sha1Hash hash = new Sha1Hash(bytes);
 *
 * // All-zeros hash
 * Sha1Hash zeroHash = new Sha1Hash();
 * </pre>
 * <p>
 * <b>Using Sha1Hash for Torrent Lookups:</b>
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 * Sha1Hash infoHash = ti.infoHashV1();
 *
 * // Find the torrent in the session
 * TorrentHandle th = sessionManager.find(infoHash);
 * if (th != null) {
 *     System.out.println("Torrent found: " + th.status().name());
 * }
 * </pre>
 * <p>
 * <b>Hash Operations:</b>
 * <pre>
 * Sha1Hash hash = new Sha1Hash("d8e8fca2dc0f896fd7cb4cb0031ba249");
 *
 * // Convert to string formats
 * String hex = hash.toHex();           // Hex representation
 * String hex_lower = hash.toHexLower(); // Lowercase hex
 *
 * // Properties
 * boolean isZero = hash.isAllZeros();
 * int leadingZeroBits = hash.countLeadingZeros();
 *
 * // Comparison
 * Sha1Hash hash2 = new Sha1Hash("...");
 * int cmp = hash.compareTo(hash2);
 * boolean equal = hash.equals(hash2);
 * </pre>
 * <p>
 * <b>Note:</b> For BitTorrent v2 torrents, use {@link Sha256Hash} instead.
 * Hybrid torrents (v1 + v2) will have both SHA-1 and SHA-256 info-hashes.
 *
 * @see TorrentInfo#infoHashV1() - Get SHA-1 info-hash from torrent
 * @see SessionManager#find(Sha1Hash) - Look up torrent by SHA-1 info-hash
 * @see Sha256Hash - For SHA-256 hashes
 *
 * @author gubatron
 * @author aldenml
 */
public final class Sha1Hash implements Comparable<Sha1Hash>, Cloneable {

    private final sha1_hash h;

    /**
     * @param h native object
     */
    public Sha1Hash(sha1_hash h) {
        this.h = h;
    }

    /**
     * @param bytes hash as an array of bytes
     */
    public Sha1Hash(byte[] bytes) {
        if (bytes.length != 20) {
            throw new IllegalArgumentException("Sha1Hash0byte[]() bytes array must be of length 20");
        }

        this.h = new sha1_hash(Vectors.bytes2byte_vector(bytes));
    }

    /**
     * @param hex hex coded representation of the hash
     */
    public Sha1Hash(String hex) {
        this(Hex.decode(hex));
    }

    /**
     * Constructs an all-zero sha1-hash
     */
    public Sha1Hash() {
        this(new sha1_hash());
    }

    /**
     * @return the native object
     */
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
    public int compareTo(Sha1Hash o) {
        return sha1_hash.compare(this.h, o.h);
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
        if (!(obj instanceof Sha1Hash)) {
            return false;
        }

        return h.op_eq(((Sha1Hash) obj).h);
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return h.hash_code();
    }

    @Override
    public Sha1Hash clone() {
        return new Sha1Hash(new sha1_hash(h));
    }

    /**
     * returns an all-F sha1-hash. i.e. the maximum value
     * representable by a 160 bit number (20 bytes). This is
     * a static member function.
     *
     * @return the maximum number
     */
    public static Sha1Hash max() {
        return new Sha1Hash(sha1_hash.max());
    }

    /**
     * returns an all-zero sha1-hash. i.e. the minimum value
     * representable by a 160 bit number (20 bytes). This is
     * a static member function.
     *
     * @return the minimum number (zero)
     */
    public static Sha1Hash min() {
        return new Sha1Hash(sha1_hash.min());
    }

    static ArrayList<Sha1Hash> convert(sha1_hash_vector v) {
        int size = (int) v.size();
        ArrayList<Sha1Hash> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new Sha1Hash(v.get(i)));
        }

        return l;
    }
}
