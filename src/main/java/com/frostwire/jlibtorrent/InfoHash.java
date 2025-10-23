package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.info_hash_t;

/**
 * Dual-hash support for BitTorrent v1 (SHA-1) and v2 (SHA-256) torrents.
 * <p>
 * {@code InfoHash} holds the cryptographic hash(es) that uniquely identify a torrent's content.
 * Modern torrents support both v1 hashes (BEP 3, SHA-1, 20 bytes) and v2 hashes (BEP 52, SHA-256,
 * 32 bytes) to enable backward compatibility with older clients while providing cryptographic
 * advantages of SHA-256. A torrent can have v1 only, v2 only, or both v1 and v2 hashes simultaneously.
 * <p>
 * <b>Understanding Torrent Hashes:</b>
 * <ul>
 *   <li><b>v1 Hash (SHA-1):</b> 20-byte hash (160 bits), used by original BitTorrent protocol (BEP 3)
 *   <li><b>v2 Hash (SHA-256):</b> 32-byte hash (256 bits), improved security and file verification (BEP 52)
 *   <li><b>Dual Mode:</b> Modern torrents include both hashes for maximum compatibility and security
 *   <li><b>Hash Truncation:</b> If only v1 is available, it may be a truncated v2 hash for DHT discovery
 * </ul>
 * <p>
 * <b>Hash Types and Semantics:</b>
 * <pre>
 * Pure v1 Torrent:
 *   hasV1() = true, hasV2() = false
 *   Contains only SHA-1 hash (20 bytes)
 *   Compatible with older BitTorrent clients
 *   Less secure than v2
 *
 * Pure v2 Torrent:
 *   hasV1() = false, hasV2() = true
 *   Contains only SHA-256 hash (32 bytes)
 *   Only works with BitTorrent v2 capable clients
 *   Maximum security and file integrity checking
 *
 * Hybrid v1+v2 Torrent:
 *   hasV1() = true, hasV2() = true
 *   Contains both SHA-1 and SHA-256 hashes
 *   Best practice: works everywhere, maximum compatibility
 *   Both hashes are independent and complete
 * </pre>
 * <p>
 * <b>Accessing Torrent Hashes:</b>
 * <pre>
 * // From torrent metadata
 * TorrentInfo torrentInfo = new TorrentInfo(torrentFile);
 * InfoHash infoHash = torrentInfo.infoHash();  // Gets the InfoHash wrapper
 *
 * // Check which hashes are available
 * if (infoHash.hasV1()) {
 *     Sha1Hash v1 = infoHash.getV1();  // Get SHA-1 hash (20 bytes)
 *     System.out.println("v1 hash: " + v1.toHex());
 * }
 *
 * if (infoHash.hasV2()) {
 *     Sha256Hash v2 = infoHash.getV2();  // Get SHA-256 hash (32 bytes)
 *     System.out.println("v2 hash: " + v2.toHex());
 * }
 *
 * // Get the "best" hash (prefers v2 for security)
 * Sha1Hash best = infoHash.getBest();  // Returns v2 if available, else v1
 * System.out.println("Best hash: " + best.toHex());
 * </pre>
 * <p>
 * <b>Hash Selection Strategy:</b>
 * <pre>
 * // The getBest() method uses this logic:
 * // 1. If v2 hash exists, return it (SHA-256, more secure)
 * // 2. Otherwise return v1 hash (SHA-1, backward compatibility)
 * // 3. Never returns null - at least one hash always exists
 *
 * // Use v1 for compatibility with older peers
 * Sha1Hash v1 = infoHash.getV1();
 * sm.find(v1);  // Look up torrent by v1 hash
 *
 * // Use v2 for security-critical operations
 * Sha256Hash v2 = infoHash.getV2();
 * // DHT lookups, peer discovery, verification
 * </pre>
 * <p>
 * <b>Torrent Lookup by Hash:</b>
 * <pre>
 * // Find torrent in SessionManager by v1 hash
 * Sha1Hash v1 = infoHash.getV1();
 * TorrentHandle handle = sm.find(v1);
 *
 * // Find torrent by v2 hash
 * Sha256Hash v2 = infoHash.getV2();
 * TorrentHandle handle = sm.find(v2);
 *
 * // Get best available hash for reliable lookup
 * Sha1Hash bestHash = infoHash.getBest();  // Returns available hash
 * TorrentHandle handle = sm.find(bestHash);  // Works with either v1 or v2
 * </pre>
 * <p>
 * <b>BitTorrent v1 vs v2 Comparison:</b>
 * <table border="1">
 * <caption>BitTorrent v1 vs v2 Hash Comparison</caption>
 *   <tr><th>Feature</th><th>v1 (SHA-1)</th><th>v2 (SHA-256)</th></tr>
 *   <tr><td>Hash Size</td><td>20 bytes</td><td>32 bytes</td></tr>
 *   <tr><td>Algorithm</td><td>SHA-1</td><td>SHA-256</td></tr>
 *   <tr><td>Security</td><td>Deprecated</td><td>Secure</td></tr>
 *   <tr><td>Compatibility</td><td>Universal</td><td>v2+ clients only</td></tr>
 *   <tr><td>BEP</td><td>BEP 3 (original)</td><td>BEP 52 (modern)</td></tr>
 * </table>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Hash lookups are O(1) - direct field access</li>
 *   <li>Use the specific getV1() or getV2() if you know which hash you need</li>
 *   <li>getBest() prefers v2 but returns v1 as fallback - safe for all cases</li>
 *   <li>Hybrid torrents use the same hash for discovery via DHT regardless of peer version</li>
 * </ul>
 *
 * @see Sha1Hash - For v1 (SHA-1) hash operations
 * @see Sha256Hash - For v2 (SHA-256) hash operations
 * @see TorrentInfo#infoHash() - Get InfoHash from torrent metadata
 * @see SessionManager#find(Sha1Hash) - Look up torrent by v1 hash
 * @see SessionManager#find(Sha256Hash) - Look up torrent by v2 hash
 *
 * @author aldenml@libtorrent4j
 * @author gubatron
 */
public class InfoHash {
    private final info_hash_t ih_t;

    public InfoHash(info_hash_t swig) {
        this.ih_t = swig;
    }

    public InfoHash() {
        this.ih_t = new info_hash_t();
    }

    public info_hash_t swig() {
        return ih_t;
    }

    public boolean hasV1() {
        return ih_t.has_v1();
    }

    public boolean hasV2() {
        return ih_t.has_v2();
    }

    public Sha1Hash getBest() {
        return new Sha1Hash(ih_t.get_best());
    }

    public Sha1Hash getV1() {
        return new Sha1Hash(ih_t.getV1());
    }

    public Sha256Hash getV2() {
        return new Sha256Hash(ih_t.getV2());
    }
}