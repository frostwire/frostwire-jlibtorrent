package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.frostwire.jlibtorrent.swig.libtorrent.add_files_ex;
import static com.frostwire.jlibtorrent.swig.libtorrent.set_piece_hashes_ex;

/**
 * Fluent builder for creating .torrent files from local files and directories.
 * <p>
 * {@code TorrentBuilder} provides a convenient API for creating BitTorrent metafiles (.torrent)
 * from local files and directories. It supports all modern torrent features including trackers,
 * DHT nodes, web seeds, metadata, and hash computation with progress tracking.
 * <p>
 * <b>Understanding Torrent Creation:</b>
 * <br/>
 * A .torrent file is a bencoded metadata file containing:
 * <ul>
 *   <li><b>File Information:</b> Names, sizes, and pieces (chunks)</li>
 *   <li><b>Piece Hashes:</b> SHA-1 or SHA-256 hashes for verification</li>
 *   <li><b>Announce URLs:</b> Tracker URLs organized by tier/priority</li>
 *   <li><b>DHT Nodes:</b> Bootstrap nodes for decentralized peer discovery</li>
 *   <li><b>Web Seeds:</b> HTTP/FTP URLs for web seeding</li>
 *   <li><b>Metadata:</b> Comment, creator, creation time, private flag</li>
 * </ul>
 * <p>
 * <b>Basic Torrent Creation:</b>
 * <pre>
 * // Create a torrent for a single file
 * TorrentBuilder builder = new TorrentBuilder();
 * builder.path(new File(\"/path/to/file.iso\"));
 * builder.pieceSize(262144);  // 256 KB pieces
 * builder.addTracker(\"http://tracker.example.com:6969/announce\", 0);
 * builder.comment(\"My ISO image\");
 * builder.creator(\"My App v1.0\");
 *
 * TorrentBuilder.Result result = builder.build();
 * Entry torrentEntry = result.entry();
 * // Save torrentEntry to file...
 * </pre>
 * <p>
 * <b>Multi-File Torrent:</b>
 * <pre>
 * // Create a torrent for a directory
 * TorrentBuilder builder = new TorrentBuilder();
 * builder.path(new File(\"/path/to/directory\"));
 * builder.pieceSize(16384);  // 16 KB pieces for flexibility
 * builder.addTracker(\"http://primary-tracker.com/announce\", 0);  // Tier 0
 * builder.addTracker(\"http://backup-tracker.com/announce\", 1);   // Tier 1
 * builder.addNode(new Pair&lt;&gt;(\"router.bittorrent.com\", 6881));\n * builder.addUrlSeed(\"http://mirror.example.com/data/\");\n *
 * TorrentBuilder.Result result = builder.build();
 * </pre>
 * <p>
 * <b>Piece Size Considerations:</b>
 * <pre>
 * // Piece sizes must be power of 2, between 16 KiB and 16 MiB
 * // Common sizes:
 * builder.pieceSize(16384);    // 16 KiB - many small pieces, larger .torrent file
 * builder.pieceSize(32768);    // 32 KiB
 * builder.pieceSize(65536);    // 64 KiB
 * builder.pieceSize(262144);   // 256 KiB - good default for large files
 * builder.pieceSize(1048576);  // 1 MiB - for very large files
 *
 * // Auto-sizing: set to 0 to let TorrentBuilder choose
 * builder.pieceSize(0);  // Will be ~40 KB torrent file size
 * </pre>
 * <p>
 * <b>Tracker Organization:</b>
 * <pre>
 * // Trackers are tried in tier order (priority)
 * // Tier 0 tried first, then tier 1 if all tier 0 fail, etc.
 * builder.addTracker(\"http://tier0-a.com/announce\", 0);
 * builder.addTracker(\"http://tier0-b.com/announce\", 0);  // Alternative tier 0
 * builder.addTracker(\"http://tier1-backup.com/announce\", 1);
 * builder.addTracker(\"http://tier2-last-resort.com/announce\", 2);
 * </pre>
 * <p>
 * <b>Progress Tracking:</b>
 * <pre>
 * TorrentBuilder builder = new TorrentBuilder();
 * // ... setup configuration ...
 *
 * builder.listener(new TorrentBuilder.Listener() {
 *     public boolean accept(String filename) {
 *         // Return true to include, false to skip
 *         return !filename.startsWith(\".\");  // Skip hidden files
 *     }
 *
 *     public void progress(int pieceIndex, int numPieces) {
 *         // Called as hashes are computed
 *         double percent = (double) pieceIndex / numPieces * 100;
 *         System.out.println(String.format(\"Hashing: %.1f%%\", percent));
 *     }
 * });
 *
 * TorrentBuilder.Result result = builder.build();
 * </pre>
 * <p>
 * <b>DHT Nodes and Web Seeds:</b>
 * <pre>
 * // DHT nodes for decentralized peer discovery (tracker-less torrent)
 * builder.addNode(new Pair&lt;&gt;(\"router.bittorrent.com\", 6881));
 * builder.addNode(new Pair&lt;&gt;(\"router.transmissionbt.com\", 6881));
 * builder.addNode(new Pair&lt;&gt;(\"dht.transmissionbt.com\", 6881));
 *
 * // Web seeds for HTTP/FTP distribution
 * builder.addUrlSeed(\"http://cdn.example.com/downloads/file.iso\");
 * builder.addUrlSeed(\"ftp://mirror.example.com/file.iso\");
 * </pre>
 * <p>
 * <b>Metadata and Privacy:</b>
 * <pre>
 * builder.comment(\"ISO image for my project\");
 * builder.creator(\"My Torrent Creator v1.0\");
 * builder.creationDate(System.currentTimeMillis() / 1000);  // UNIX timestamp
 * builder.priv(true);  // Private torrent (DHT/PEX disabled)
 * </pre>
 * <p>
 * <b>Flags for Advanced Features:</b>
 * <pre>
 * // Enable specific creation options
 * builder.flags(TorrentBuilder.PADDING);  // Add padding files for alignment
 * builder.flags(TorrentBuilder.SYMLINKS);  // Preserve symbolic links
 * builder.flags(TorrentBuilder.V2_ONLY);  // Only v2 metadata (modern clients)
 * </pre>
 * <p>
 * <b>Accessing Build Results:</b>
 * <pre>
 * TorrentBuilder.Result result = builder.build();
 *
 * // Get bencoded torrent data
 * Entry torrentEntry = result.entry();
 *
 * // Get torrent metadata
 * int numPieces = result.numPieces();
 * int pieceLength = result.pieceLength();
 *
 * // Get individual piece size
 * int lastPieceSize = result.pieceSize(result.numPieces() - 1);
 *
 * // Save to file
 * byte[] torrentData = torrentEntry.bencode();
 * Files.write(Paths.get(\"file.torrent\"), torrentData);
 * </pre>
 * <p>
 * <b>Builder Pattern:</b>
 * <p>
 * {@code TorrentBuilder} uses the fluent builder pattern. All configuration methods return
 * the builder itself, enabling method chaining for clean, readable code.
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Hash computation is CPU-intensive; progress callback helps monitor long operations</li>
 *   <li>Smaller pieces = larger .torrent file and more I/O</li>
 *   <li>Larger pieces = smaller .torrent file but less granular progress</li>
 *   <li>Call build() only once; reuse TorrentBuilder for multiple torrents</li>
 * </ul>
 *
 * @see TorrentInfo - For reading existing .torrent files
 * @see Entry - For bencoded data manipulation
 * @see Result - Build output containing torrent metadata
 * @see Listener - For progress callbacks during hash computation
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentBuilder {

    private File path;
    private int pieceSize;
    private create_flags_t flags;
    private int alignment;

    private String comment;
    private String creator;
    private Long creationDate;
    private final List<String> urlSeeds;
    private final List<Pair<String, Integer>> nodes;
    private final List<Pair<String, Integer>> trackers;
    private boolean priv;

    private final List<Sha1Hash> similarTorrents;
    private final List<String> collections;

    private Listener listener;

    public TorrentBuilder() {
        this.pieceSize = 0;
        this.flags = new create_flags_t();
        this.alignment = -1;

        this.urlSeeds = new LinkedList<>();
        this.nodes = new LinkedList<>();
        this.trackers = new LinkedList<>();

        this.similarTorrents = new LinkedList<>();
        this.collections = new LinkedList<>();
    }

    public File path() {
        return path;
    }

    /**
     * Sets the path by the specified file
     */
    public TorrentBuilder path(File value) {
        this.path = value;
        return this;
    }

    /**
     * The ``piece_size`` is the size of each piece in bytes. It must be a
     * power of 2 and a minimum of 16 kiB. If a piece size of 0 is
     * specified, a piece_size will be set automatically.
     */
    @SuppressWarnings("unused")
    public int pieceSize() {
        return pieceSize;
    }

    /**
     * The size of each piece in bytes. It must
     * be a multiple of 16 kiB. If a piece size of 0 is specified, a
     * {@code pieceSize} will be calculated such that the torrent file is roughly 40 kB.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder pieceSize(int value) {
        this.pieceSize = value;
        return this;
    }


    /**
     * create_flags_t is a type alias that simplifies the usage of the bitfield_flag structure for a specific case
     * within the libtorrent namespace.
     * <p>
     * The bitfield_flag structure in the provided code is a utility for managing flags in a bitfield,
     * which is essentially a data structure that compactly stores bits.
     * <p>
     * create_flags_t is a type alias for bitfield_flag&lt;std::uint32_t, create_flags_tag&gt;, specifically designed
     * to handle creation flags in libtorrent.
     * <p>
     * It provides a compact, efficient way to manage binary flags using bitwise operations, with the additional
     * type safety provided by the create_flags_tag.
     */
    public create_flags_t flags() {
        return flags;
    }

    /**
     * Specifies options for the torrent creation. It can
     * be any combination of the flags defined by {@link create_flags_t}
     */
    public TorrentBuilder flags(create_flags_t value) {
        this.flags = value;
        return this;
    }

    @SuppressWarnings("unused")
    public int alignment() {
        return alignment;
    }

    /**
     * Used when pad files are enabled. This is the size
     * eligible files are aligned to. The default is -1, which means the
     * piece size of the torrent.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder alignment(int value) {
        this.alignment = value;
        return this;
    }

    /**
     * The comment for the torrent. The comment in a torrent file is optional.
     */
    public String comment() {
        return comment;
    }

    /**
     * Sets the comment for the torrent. The comment in a torrent file is optional.
     */
    public TorrentBuilder comment(String value) {
        this.comment = value;
        return this;
    }

    /**
     * The creator of the torrent. This is optional.
     */
    @SuppressWarnings("unused")
    public String creator() {
        return creator;
    }

    /**
     * Sets the creator of the torrent. This is optional.
     */
    public TorrentBuilder creator(String value) {
        this.creator = value;
        return this;
    }

    /**
     * The "creation time" field. Defaults to the system clock at the
     * time of construction.
     * The timestamp is specified in seconds, posix time. If the creation
     * date is set to 0, the "creation date" field will be omitted from
     * the generated torrent.
     */
    @SuppressWarnings("unused")
    public long creationDate() {
        return creationDate;
    }

    /**
     * Set the "creation time" field. Defaults to the system clock at the
     * time of construction.
     * The timestamp is specified in seconds, posix time. If the creation
     * date is set to 0, the "creation date" field will be omitted from
     * the generated torrent.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder creationDate(long timestamp) {
        this.creationDate = timestamp;
        return this;
    }

    @SuppressWarnings("unused")
    public List<String> urlSeeds() {
        return urlSeeds;
    }

    /**
     * This adds a list of url seeds to the torrent. You can have any number of url seeds. For a
     * single file torrent, this should be an HTTP url, pointing to a file with identical
     * content as the file of the torrent. For a multi-file torrent, it should point to
     * a directory containing a directory with the same name as this torrent, and all the
     * files of the torrent in it.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addUrlSeeds(List<String> value) {
        if (value != null) {
            this.urlSeeds.addAll(value);
        }
        return this;
    }

    /**
     * This adds a URL seed to the torrent. You can have any number of url seeds. For a
     * single file torrent, this should be an HTTP url, pointing to a file with identical
     * content as the file of the torrent. For a multi-file torrent, it should point to
     * a directory containing a directory with the same name as this torrent, and all the
     * files of the torrent in it.
     */
    public TorrentBuilder addUrlSeed(String value) {
        if (value != null) {
            this.urlSeeds.add(value);
        }
        return this;
    }

    /**
     * Lists specified DHT nodes in the torrent (added with addNodes(List&lt;Pair&lt;String, Integer&gt;&gt;)).
     * A node is a hostname and a port number where there is a DHT node running.
     */
    public List<Pair<String, Integer>> nodes() {
        return nodes;
    }

    /**
     * This adds a DHT node to the torrent. This especially useful if you're creating a
     * tracker less torrent. It can be used by clients to bootstrap their DHT node from.
     * The node is a hostname and a port number where there is a DHT node running.
     * You can have any number of DHT nodes in a torrent.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addNodes(List<Pair<String, Integer>> value) {
        if (value != null) {
            this.nodes.addAll(value);
        }
        return this;
    }

    /**
     * This adds a DHT node to the torrent. This especially useful if you're creating a
     * tracker less torrent. It can be used by clients to bootstrap their DHT node from.
     * The node is a hostname and a port number where there is a DHT node running.
     * You can have any number of DHT nodes in a torrent.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addNode(Pair<String, Integer> value) {
        if (value != null) {
            this.nodes.add(value);
        }
        return this;
    }

    public List<Pair<String, Integer>> trackers() {
        return trackers;
    }

    /**
     * Adds a list of trackers to the torrent.
     *
     * @see #addTracker(String, int)
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addTrackers(List<Pair<String, Integer>> value) {
        if (value != null) {
            this.trackers.addAll(value);
        }
        return this;
    }

    /**
     * Adds a tracker to the torrent. This is not strictly required, but most torrents
     * use a tracker as their main source of peers. The url should be a http:// or udp://
     * url to a machine running a bittorrent tracker that accepts announces for this torrent's
     * info-hash. The tier is the fallback priority of the tracker. All trackers with tier 0 are
     * tried first (in any order). If all fail, trackers with tier 1 are tried. If all of those
     * fail, trackers with tier 2 are tried, and so on.
     */
    public TorrentBuilder addTracker(Pair<String, Integer> value) {
        if (value != null) {
            this.trackers.add(value);
        }
        return this;
    }

    /**
     * Adds a tracker to the torrent. This is not strictly required, but most torrents
     * use a tracker as their main source of peers. The url should be a http:// or udp://
     * url to a machine running a bittorrent tracker that accepts announces for this torrent's
     * info-hash. The tier is the fallback priority of the tracker. All trackers with tier 0 are
     * tried first (in any order). If all fail, trackers with tier 1 are tried. If all of those
     * fail, trackers with tier 2 are tried, and so on.
     */
    public TorrentBuilder addTracker(String url, int tier) {
        return addTracker(new Pair<>(url, tier));
    }

    /**
     * Adds a tracker on tier 0 to the torrent.
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addTracker(String url) {
        return addTracker(url, 0);
    }

    /**
     * Torrents with the private flag set ask clients to not use any other
     * sources than the tracker for peers, and to not advertise itself publicly,
     * apart from the tracker.
     */
    public boolean isPrivate() {
        return priv;
    }

    /**
     * Sets the private flag of the torrent.
     * <p>
     * Torrents with the private flag set ask clients to not use any other
     * sources than the tracker for peers, and to not advertise itself publicly,
     * apart from the tracker.
     */
    public TorrentBuilder setPrivate(boolean value) {
        this.priv = value;
        return this;
    }

    /**
     * @see #addSimilarTorrent(Sha1Hash)
     */
    @SuppressWarnings("unused")
    public List<Sha1Hash> similarTorrents() {
        return similarTorrents;
    }

    /**
     * @see #addSimilarTorrent(Sha1Hash)
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addSimilarTorrents(List<Sha1Hash> value) {
        if (value != null) {
            this.similarTorrents.addAll(value);
        }
        return this;
    }

    /**
     * Add similar torrents (by info-hash).
     * <p>
     * Similar torrents are expected to share some files with this torrent.
     * Torrents sharing a collection name with this torrent are also expected
     * to share files with this torrent. A torrent may have more than one
     * collection and more than one similar torrents. For more information,
     * see BEP 38.
     * <p>
     * BEP 38: <a href="http://www.bittorrent.org/beps/bep_0038.html">...</a>
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addSimilarTorrent(Sha1Hash value) {
        if (value != null) {
            this.similarTorrents.add(value);
        }
        return this;
    }

    public List<String> collections() {
        return collections;
    }

    @SuppressWarnings("unused")
    public TorrentBuilder addCollections(List<String> value) {
        if (value != null) {
            this.collections.addAll(value);
        }
        return this;
    }

    /**
     * Add collections of similar torrents.
     * <p>
     * Similar torrents are expected to share some files with this torrent.
     * Torrents sharing a collection name with this torrent are also expected
     * to share files with this torrent. A torrent may have more than one
     * collection and more than one similar torrents. For more information,
     * see BEP 38.
     * <p>
     * BEP 38: <a href="http://www.bittorrent.org/beps/bep_0038.html">...</a>
     */
    @SuppressWarnings("unused")
    public TorrentBuilder addCollection(String value) {
        if (value != null) {
            this.collections.add(value);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Listener listener() {
        return listener;
    }

    public TorrentBuilder listener(Listener value) {
        this.listener = value;
        return this;
    }

    /**
     * This function will generate a result with the .torrent file as a bencoded tree.
     */
    public Result generate() throws IOException {
        if (path == null) {
            throw new IOException("path can't be null");
        }

        File absPath = path.getAbsoluteFile();

        file_storage fs = new file_storage();
        add_files_listener l1 = new add_files_listener() {
            @Override
            public boolean pred(String p) {
                return listener == null || listener.accept(p);
            }
        };
        add_files_ex(fs, absPath.getPath(), l1, flags);
        if (fs.total_size() == 0) {
            throw new IOException("content total size can't be 0");
        }

        /*
         TODO: refactor to use list_files_ex instead of add_files_ex when libtorrent master is merged onto 2.0
         See in libtorrent: https://github.com/arvidn/libtorrent/blob/master/include/libtorrent/create_torrent.hpp#L582
         See commented code in libtorrent.hpp: std::vector<libtorrent::create_file_entry> list_files_ex(
         create_file_entry_vector files = list_files_ex(fs, absPath.getPath(), l1);
        if (files.size() == 0) {
            throw new IOException("content files can't be empty");
        }
        create_torrent t = new create_torrent(files, pieceSize, flags);
        */

        create_torrent t = new create_torrent(fs, pieceSize, flags);
        final int numPieces = t.num_pieces();
        set_piece_hashes_listener l2 = new set_piece_hashes_listener() {
            @Override
            public void progress(int i) {
                if (listener != null) {
                    listener.progress(i, numPieces);
                }
            }
        };
        File parent = absPath.getParentFile();
        if (parent == null) {
            throw new IOException("path's parent can't be null");
        }
        error_code ec = new error_code();
        set_piece_hashes_ex(t, parent.getAbsolutePath(), l2, ec);
        if (ec.value() != 0) {
            throw new IOException(ec.message());
        }

        if (comment != null) {
            t.set_comment(comment);
        }
        if (creator != null) {
            t.set_creator(creator);
        }
        for (String s : urlSeeds) {
            t.add_url_seed(s);
        }
        for (Pair<String, Integer> n : nodes) {
            t.add_node(n.to_string_int_pair());
        }
        for (Pair<String, Integer> tr : trackers) {
            t.add_tracker(tr.first, tr.second);
        }
        if (priv) {
            t.set_priv(true);
        }

        if (!similarTorrents.isEmpty()) {
            for (Sha1Hash h : similarTorrents) {
                t.add_similar_torrent(h.swig());
            }
        }
        if (!collections.isEmpty()) {
            for (String s : collections) {
                t.add_collection(s);
            }
        }

        return new Result(t);
    }

    /**
     * This will include the file modification time as part of the torrent.
     * This is not enabled by default, as it might cause problems when you
     * create a torrent from separate files with the same content, hoping to
     * yield the same info-hash. If the files have different modification times,
     * with this option enabled, you would get different info-hashes for the
     * files.
     */
    @SuppressWarnings("unused")
    public static final create_flags_t MODIFICATION_TIME = create_torrent.modification_time;

    /**
     * If this flag is set, files that are symlinks get a symlink attribute
     * set on them and their data will not be included in the torrent. This
     * is useful if you need to reconstruct a file hierarchy which contains
     * symlinks.
     */
    @SuppressWarnings("unused")
    public static final create_flags_t SYMLINKS = create_torrent.symlinks;

    /**
     * Do not generate v1 metadata. The resulting torrent will only be usable by
     * clients which support v2. This requires setting all v2 hashes, with
     * set_hash2() before calling generate(). Setting v1 hashes (with
     * set_hash()) is an error with this flag set.
     */
    @SuppressWarnings("unused")
    public static final create_flags_t V2_ONLY = create_torrent.v2_only;

    /**
     * Do not generate v2 metadata or enforce v2 alignment and padding rules
     * this is mainly for tests, not recommended for production use. This
     * requires setting all v1 hashes, with set_hash(), before calling
     * generate(). Setting v2 hashes (with set_hash2()) is an error with
     * this flag set.
     */
    @SuppressWarnings("unused")
    public static final create_flags_t V1_ONLY = create_torrent.v1_only;

    /**
     * This flag only affects v1-only torrents, and is only relevant
     * together with the v1_only_flag. This flag will force the
     * same file order and padding as a v2 (or hybrid) torrent would have.
     * It has the effect of ordering files and inserting pad files to align
     * them with piece boundaries.
     */
    @SuppressWarnings("unused")
    public static final create_flags_t CANONICAL_FILES = create_torrent.canonical_files;


    /**
     * Callback interface for torrent creation events.
     * <p>
     * {@code Listener} allows applications to filter files during torrent creation and
     * track hash computation progress. Implement this interface to customize which files
     * are included in the torrent and display progress to the user.
     * <p>
     * <b>File Filtering:</b>
     * <p>
     * The {@code accept()} method is called for every file encountered during directory
     * traversal. Return true to include the file, false to skip it. This is useful for
     * excluding temporary files, hidden files, or other unwanted data.
     * <p>
     * <b>Progress Tracking:</b>
     * <p>
     * The {@code progress()} method is called as each piece's hash is computed, enabling
     * progress bars or status updates during the potentially time-consuming hashing phase.
     * <p>
     * <b>Usage Example:</b>
     * <pre>
     * TorrentBuilder.Listener listener = new TorrentBuilder.Listener() {
     *     public boolean accept(String filename) {
     *         // Skip hidden files and system files
     *         if (filename.startsWith(\".\")) return false;
     *         if (filename.equals(\"Thumbs.db\")) return false;
     *         if (filename.equals(\"Desktop.ini\")) return false;
     *         return true;
     *     }
     *
     *     public void progress(int pieceIndex, int numPieces) {
     *         // Display progress
     *         double percent = (double) pieceIndex / numPieces * 100;
     *         System.out.println(String.format(\"Hashing: %.0f%% (%d/%d pieces)\",
     *             percent, pieceIndex, numPieces));
     *     }
     * };
     *
     * TorrentBuilder builder = new TorrentBuilder();
     * builder.listener(listener);
     * </pre>
     */
    public interface Listener {

        /**
         * Called during directory traversal to filter files.
         * <p>
         * Return true to include the file in the torrent, false to skip it.
         *
         * @param filename the filename being considered
         * @return true to include, false to skip
         */
        boolean accept(String filename);

        /**
         * Called periodically during hash computation.
         * <p>
         * Use this to update progress bars or status displays.
         *
         * @param pieceIndex the current piece being hashed (0-based)
         * @param numPieces the total number of pieces
         */
        void progress(int pieceIndex, int numPieces);
    }

    /**
     * Result of torrent creation containing metadata and bencoded data.
     * <p>
     * {@code Result} contains the successful output of torrent file creation including
     * the bencoded torrent entry and metadata about the created torrent (number of pieces,
     * piece sizes, etc.). This information can be used to save the .torrent file or
     * display torrent information to the user.
     * <p>
     * <b>Accessing Torrent Data:</b>
     * <pre>
     * TorrentBuilder.Result result = builder.build();
     *
     * // Get bencoded torrent entry for saving to file
     * Entry torrentEntry = result.entry();
     * byte[] torrentData = torrentEntry.bencode();
     *
     * // Save to .torrent file
     * Files.write(Paths.get(\"file.torrent\"), torrentData);
     * </pre>
     * <p>
     * <b>Querying Torrent Metadata:</b>
     * <pre>
     * TorrentBuilder.Result result = builder.build();
     *
     * // Number of pieces in the torrent
     * int pieceCount = result.numPieces();
     * System.out.println(\"Total pieces: \" + pieceCount);
     *
     * // Standard piece length (except possibly the last piece)
     * int pieceLen = result.pieceLength();
     * System.out.println(\"Piece size: \" + pieceLen + \" bytes\");
     *
     * // Size of a specific piece (last piece may be smaller)
     * for (int i = 0; i &lt; result.numPieces(); i++) {
     *     int size = result.pieceSize(i);
     *     System.out.println(\"Piece \" + i + \": \" + size + \" bytes\");
     * }
     * </pre>
     * <p>
     * <b>Creating TorrentInfo from Result:</b>
     * <pre>
     * TorrentBuilder.Result result = builder.build();
     *
     * // Create bencoded data
     * byte[] torrentBytes = result.entry().bencode();
     *
     * // Create TorrentInfo from the bencoded data
     * TorrentInfo ti = new TorrentInfo(torrentBytes);
     *
     * // Now can inspect the created torrent
     * System.out.println(\"Info-hash: \" + ti.infoHash());
     * System.out.println(\"Files: \" + ti.numFiles());
     * System.out.println(\"Total size: \" + ti.totalSize());
     * </pre>
     */
    public static final class Result {

        private final create_torrent t;
        private final Entry entry;

        private Result(create_torrent t) {
            this.t = t;
            this.entry = new Entry(t.generate());
        }

        public Entry entry() {
            return entry;
        }

        @SuppressWarnings("unused")
        public int numPieces() {
            return t.num_pieces();
        }

        @SuppressWarnings("unused")
        public int pieceLength() {
            return t.piece_length();
        }

        @SuppressWarnings("unused")
        public int pieceSize(int index) {
            return t.piece_size(index);
        }
    }
}
