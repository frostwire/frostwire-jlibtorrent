package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;

/**
 * Parser and wrapper for bencoded data structures (BitTorrent metadata format).
 * <p>
 * {@code BDecodeNode} provides structured access to bencoded data, the serialization format
 * used by BitTorrent for .torrent files and DHT messages. Bencode is a simple encoding that
 * supports integers, byte strings, lists, and dictionaries. BDecodeNode allows navigating
 * this hierarchical structure efficiently without parsing bencoded strings manually.
 * <p>
 * <b>Understanding Bencode Format:</b>
 * <pre>
 * Integers:     i42e          → represents integer 42
 * Strings:      4:spam        → represents "spam" (4-byte string)
 * Lists:        l4:spami42ee  → represents ["spam", 42]
 * Dicts:        d3:agei42ee   → represents {"age": 42}
 *
 * Bencoding used extensively in BitTorrent:
 * - .torrent file metadata (announce, info hash, file list)
 * - DHT protocol messages (finds, nodes)
 * - Tracker responses (peer lists)
 * - Magnet link extensions
 * </pre>
 * <p>
 * <b>Parsing Torrent Files:</b>
 * <pre>
 * // Decode bencoded torrent file data
 * byte[] torrentData = Files.readAllBytes(Paths.get("download.torrent"));
 * BDecodeNode root = BDecodeNode.bdecode(torrentData);
 *
 * // Navigate the dictionary structure
 * String announce = root.getString("announce");  // Tracker URL
 * long creationDate = root.getInt("creation date");
 *
 * // Access nested structures
 * BDecodeNode info = root.getDict("info");  // The info dict
 * long pieceLength = info.getInt("piece length");
 * String pieces = info.getString("pieces");  // Raw piece hashes
 *
 * // Access lists
 * BDecodeNode files = info.getList("files");  // Only in multi-file torrents
 * </pre>
 * <p>
 * <b>Typical Torrent File Structure (bencoded):</b>
 * <pre>
 * {
 *   "announce": "http://tracker.example.com/announce",
 *   "creation date": 1234567890,
 *   "comment": "Optional comment",
 *   "info": {
 *     "piece length": 16384,        // Typical: 16 KB
 *     "pieces": "...20 bytes... → hex string of SHA-1 hashes (concat)",
 *     "name": "filename.iso",       // Single file torrent
 *     "length": 1073741824          // File size in bytes
 *   }
 * }
 *
 * OR Multi-file structure:
 * {
 *   "info": {
 *     "piece length": 16384,
 *     "pieces": "...",
 *     "name": "directory",
 *     "files": [
 *       {"length": 100, "path": ["dir1", "file1.txt"]},
 *       {"length": 200, "path": ["dir1", "file2.txt"]},
 *       {"length": 300, "path": ["dir2", "file3.txt"]}
 *     ]
 *   }
 * }
 * </pre>
 * <p>
 * <b>Key Methods for Dictionary Navigation:</b>
 * <pre>
 * BDecodeNode node = ...;
 *
 * // Check if key exists and what type it is
 * if (node.hasString("name")) {
 *     String name = node.getString("name");
 * }
 * if (node.hasInt("length")) {
 *     long length = node.getInt("length");
 * }
 * if (node.hasDict("info")) {
 *     BDecodeNode info = node.getDict("info");
 * }
 * if (node.hasList("files")) {
 *     BDecodeNode files = node.getList("files");
 * }
 *
 * // Type checking is important - wrong type returns null or 0
 * // Example: trying to getInt() on a string field returns 0, not error
 * </pre>
 * <p>
 * <b>Parsing DHT Messages:</b>
 * <pre>
 * // DHT messages are also bencoded
 * byte[] dhtResponse = receiveDhtMessage();
 * BDecodeNode msg = BDecodeNode.bdecode(dhtResponse);
 *
 * // DHT message structure
 * String type = msg.getString("y");  // "r" for response, "q" for query
 * BDecodeNode response = msg.getDict("r");
 *
 * if (response != null) {
 *     // Extract nodes list: "nodes" is a string of packed node info
 *     String nodesData = response.getString("nodes");  // 26 bytes per node (20 hash + 6 addr)
 *     BDecodeNode values = response.getList("values");  // Peer list
 * }
 * </pre>
 * <p>
 * <b>Accessing List Elements:</b>
 * <pre>
 * BDecodeNode files = torrentInfo.getList("files");
 *
 * // Iterate through files in multi-file torrent
 * // Note: Limitation - no direct iteration in current implementation
 * // Use with TorrentInfo or FileStorage for better file handling
 *
 * // Direct file access is typically done through TorrentInfo
 * TorrentInfo ti = new TorrentInfo(torrentData);
 * FileStorage fs = ti.files();
 * for (int i = 0; i < fs.numFiles(); i++) {
 *     String path = fs.filePath(i);
 *     long size = fs.fileSize(i);
 * }
 * </pre>
 * <p>
 * <b>Error Handling:</b>
 * <pre>
 * try {
 *     BDecodeNode root = BDecodeNode.bdecode(torrentData);
 *     // Successfully parsed
 * } catch (IllegalArgumentException e) {
 *     // Parsing failed - invalid bencoding format
 *     System.err.println("Invalid torrent file: " + e.getMessage());
 * }
 *
 * // Safe null checks for missing fields
 * BDecodeNode info = root.getDict("info");
 * if (info == null) {
 *     throw new IllegalArgumentException("Missing 'info' dict in torrent");
 * }
 * </pre>
 * <p>
 * <b>Type Safety Pattern:</b>
 * <pre>
 * // Always use has* methods before accessing fields
 * if (node.hasDict("info")) {
 *     BDecodeNode info = node.getDict("info");  // Safe - already checked
 *     // Now process info...
 * }
 *
 * // Avoid this:
 * BDecodeNode info = node.getDict("typo");  // Returns null - no error thrown
 * long x = info.getInt("field");  // NPE - null reference!
 *
 * // Better:
 * if (node.hasDict("info")) {
 *     info = node.getDict("info");
 *     long x = info.getInt("field");  // Safe
 * }
 * </pre>
 * <p>
 * <b>Performance and Memory:</b>
 * <ul>
 *   <li>Parsing does NOT copy the bencoded data - it creates views over the original bytes</li>
 *   <li>BDecodeNode holds a reference to the underlying byte buffer to prevent GC</li>
 *   <li>Navigation is O(1) for direct dict lookups, O(n) for list access</li>
 *   <li>Best for one-time parsing of static metadata (not streaming)</li>
 * </ul>
 * <p>
 * <b>Common Torrent Fields Reference:</b>
 * <table border="1">
 *   <tr><th>Field</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>announce</td><td>String</td><td>Primary tracker URL</td></tr>
 *   <tr><td>announce-list</td><td>List</td><td>List of tracker tiers (backup trackers)</td></tr>
 *   <tr><td>creation date</td><td>Int</td><td>Unix timestamp when created</td></tr>
 *   <tr><td>comment</td><td>String</td><td>Optional torrent description</td></tr>
 *   <tr><td>created by</td><td>String</td><td>Client that created torrent</td></tr>
 *   <tr><td>info</td><td>Dict</td><td>File metadata and hashes</td></tr>
 *   <tr><td>info.name</td><td>String</td><td>File or directory name</td></tr>
 *   <tr><td>info.piece length</td><td>Int</td><td>Size of each piece (16384 common)</td></tr>
 *   <tr><td>info.pieces</td><td>String</td><td>Concatenated SHA-1 hashes (20 bytes each)</td></tr>
 *   <tr><td>info.length</td><td>Int</td><td>File size (single-file torrents)</td></tr>
 *   <tr><td>info.files</td><td>List</td><td>File entries (multi-file torrents)</td></tr>
 * </table>
 *
 * @see BDecodeNode#bdecode(byte[]) - Parse bencoded data
 * @see TorrentInfo - Higher-level torrent metadata API
 * @see FileStorage - File list from parsed torrent
 *
 * @author gubatron
 * @author aldenml
 */
public final class BDecodeNode {

    private final bdecode_node n;
    private final byte_vector buffer;

    /**
     * Creates a BDecodeNode wrapping a native bdecode_node object.
     *
     * @param n the native bdecode_node object (usually from bdecode() or dict/list navigation)
     */
    public BDecodeNode(bdecode_node n) {
        this(n, null);
    }

    /**
     * Creates a BDecodeNode with reference to backing buffer for memory safety.
     * <p>
     * The buffer reference prevents premature garbage collection of the bencoded
     * data that this node references. This is critical when the node was created
     * by bdecode() - without the buffer reference, the underlying bytes could be GC'd
     * while the node is still in use, causing crashes or data corruption.
     *
     * @param n      the native bdecode_node object
     * @param buffer the byte buffer containing the original bencoded data
     */
    public BDecodeNode(bdecode_node n, byte_vector buffer) {
        this.n = n;
        this.buffer = buffer;
    }

    /**
     * Returns the underlying native SWIG object.
     *
     * @return the bdecode_node for direct native API access
     */
    public bdecode_node swig() {
        return n;
    }

    /**
     * Returns the buffer backing this node's data.
     * <p>
     * This method returns the byte buffer used during parsing if this node was
     * created via bdecode(), or null if constructed directly. The buffer is kept
     * as a reference to prevent garbage collection of the data this node navigates.
     *
     * @return the pinned byte_vector buffer, or null if not created via bdecode()
     */
    public byte_vector buffer() {
        return buffer;
    }

    /**
     * Returns a JSON-like string representation of this node and its contents.
     *
     * @return formatted string showing the structure and values
     */
    @Override
    public String toString() {
        return bdecode_node.to_string(n, false, 2);
    }

    public boolean hasList(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        bdecode_node target_node = n.dict_find_list_s(key);
        return target_node.type() != bdecode_node.type_t.none_t;
    }

    public boolean hasDict(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        bdecode_node target_node = n.dict_find_dict_s(key);
        return target_node.type() != bdecode_node.type_t.none_t;
    }

    public boolean hasString(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return false;
        }
        return n.dict_find_string_value_s(key) != null;
    }

    public boolean hasInt(String key) {
        if (n.type() != bdecode_node.type_t.int_t) {
            return false;
        }
        return n.dict_find_int_value_s(key,0) != 0;
    }

    public BDecodeNode getList(String key) {
        if (!hasList(key)) {
            return null;
        }

        BDecodeNode result = new BDecodeNode(n.dict_find_list_s(key));
        return result;
    }

    public BDecodeNode getDict(String key) {
        if (!hasDict(key)) {
            return null;
        }

        BDecodeNode result = new BDecodeNode(n.dict_find_dict_s(key));
        return result;
    }

    public String getString(String key) {
        if (n.type() == bdecode_node.type_t.none_t) {
            return null;
        }
        return n.dict_find_string_value_s(key);
    }

    public long getInt(String key) {
        if (n.type() != bdecode_node.type_t.int_t) {
            return 0;
        }
        return n.dict_find_int_value_s(key,0);
    }

    public static BDecodeNode bdecode(byte[] data) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            return new BDecodeNode(n, buffer);
        } else {
            throw new IllegalArgumentException("Can't decode data: " + ec.message());
        }
    }
}
