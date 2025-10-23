package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.sha256_hash;
import com.frostwire.jlibtorrent.swig.sha256_hash_vector;

import java.util.ArrayList;

/**
 * Immutable 32-byte SHA-256 hash wrapper for BitTorrent v2 torrents.
 * <p>
 * {@code Sha256Hash} represents a 256-bit SHA-256 cryptographic hash. It's primarily used in
 * BitTorrent v2 (BEP 52) which introduced stronger cryptography than the original SHA-1 based v1.
 * <p>
 * <b>BitTorrent v1 vs v2 vs Hybrid:</b>
 * <ul>
 *   <li><b>v1 torrents:</b> Use SHA-1 info-hash (20 bytes, {@link Sha1Hash})</li>
 *   <li><b>v2 torrents:</b> Use SHA-256 info-hash (32 bytes, {@code Sha256Hash})</li>
 *   <li><b>Hybrid torrents:</b> Have BOTH SHA-1 and SHA-256 info-hashes for backward compatibility</li>
 * </ul>
 * <p>
 * <b>Creating Sha256Hash Objects:</b>
 * <pre>
 * // From hex string (64 characters for SHA-256)
 * Sha256Hash hash = new Sha256Hash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
 *
 * // From 32-byte array
 * byte[] bytes = new byte[32];
 * Sha256Hash hash = new Sha256Hash(bytes);
 *
 * // All-zeros hash
 * Sha256Hash zeroHash = new Sha256Hash();
 *
 * // Minimum/maximum values
 * Sha256Hash min = Sha256Hash.min();  // All zeros
 * Sha256Hash max = Sha256Hash.max();  // All 0xFF
 * </pre>
 * <p>
 * <b>Using Sha256Hash for v2 Torrent Lookups:</b>
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 * Sha256Hash infoHashV2 = ti.infoHashV2();
 *
 * // Find the v2 torrent in the session
 * TorrentHandle th = sessionManager.find(infoHashV2);
 * if (th != null) {
 *     System.out.println("v2 Torrent found: " + th.status().name());
 * }
 * </pre>
 * <p>
 * <b>Hybrid Torrent Handling:</b>
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 *
 * // Hybrid torrents have both hashes
 * Sha1Hash v1Hash = ti.infoHashV1();
 * Sha256Hash v2Hash = ti.infoHashV2();
 *
 * // The session will use the best available hash
 * TorrentHandle th = sessionManager.find(v2Hash);
 * if (th == null) {
 *     // Fallback to v1 if v2 not found
 *     th = sessionManager.find(v1Hash);
 * }
 * </pre>
 * <p>
 * <b>Hash Operations:</b>
 * <pre>
 * Sha256Hash hash = new Sha256Hash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
 *
 * // Convert to string formats
 * String hex = hash.toHex();           // 64-character hex string
 * String toString = hash.toString();   // Also hex
 *
 * // Properties
 * boolean isZero = hash.isAllZeros();
 * int leadingZeroBits = hash.countLeadingZeroes();
 *
 * // Comparison and equality
 * Sha256Hash hash2 = new Sha256Hash("...");
 * int cmp = hash.compareTo(hash2);
 * boolean equal = hash.equals(hash2);
 * int code = hash.hashCode();
 *
 * // Cloning
 * Sha256Hash copy = hash.clone();
 * </pre>
 * <p>
 * <b>Note:</b> For legacy BitTorrent v1 torrents, use {@link Sha1Hash} (20 bytes).
 * The {@link TorrentInfo} class will automatically provide the appropriate hash type.
 *
 * @see TorrentInfo#infoHashV2() - Get SHA-256 info-hash from torrent
 * @see TorrentInfo#infoHashV1() - Get SHA-1 info-hash from torrent
 * @see SessionManager#find(Sha256Hash) - Look up torrent by SHA-256 info-hash
 * @see Sha1Hash - For BitTorrent v1 SHA-1 hashes
 *
 * @author gubatron
 * @author aldenml
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

    public sha256_hash swig() {
        return h;
    }
}