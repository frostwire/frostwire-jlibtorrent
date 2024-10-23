package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.frostwire.jlibtorrent.swig.libtorrent.add_files_ex;
import static com.frostwire.jlibtorrent.swig.libtorrent.set_piece_hashes_ex;

/**
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


    public interface Listener {

        boolean accept(String filename);

        void progress(int pieceIndex, int numPieces);
    }

    /**
     *
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
