package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private int padFileLimit;
    private int flags;
    private int alignment;

    private String comment;
    private String creator;
    private List<String> urlSeeds;
    private List<String> httpSeeds;
    private List<Pair<String, Integer>> nodes;
    private List<Pair<String, Integer>> trackers;
    private boolean priv;

    private List<Sha1Hash> similarTorrents;
    private List<String> collections;

    private Listener listener;

    public TorrentBuilder() {
        this.pieceSize = 0;
        this.padFileLimit = -1;
        this.flags = Flags.OPTIMIZE_ALIGNMENT.swig();
        this.alignment = -1;

        this.urlSeeds = new LinkedList<>();
        this.httpSeeds = new LinkedList<>();
        this.nodes = new LinkedList<>();
        this.trackers = new LinkedList<>();

        this.similarTorrents = new LinkedList<>();
        this.collections = new LinkedList<>();
    }

    /**
     * @return
     */
    public File path() {
        return path;
    }

    /**
     * Adds the file specified by {@code value}
     *
     * @param value
     * @return
     */
    public TorrentBuilder path(File value) {
        this.path = value;
        return this;
    }

    /**
     * @return
     */
    public int pieceSize() {
        return pieceSize;
    }

    /**
     * The size of each piece in bytes. It must
     * be a multiple of 16 kiB. If a piece size of 0 is specified, a
     * {@code pieceSize} will be calculated such that the torrent file is roughly 40 kB.
     *
     * @param value
     * @return
     */
    public TorrentBuilder pieceSize(int value) {
        this.pieceSize = value;
        return this;
    }

    /**
     * @return
     */
    public int padFileLimit() {
        return padFileLimit;
    }

    /**
     * If a ``pad_size_limit`` is specified (other than -1), any file larger than
     * the specified number of bytes will be preceded by a pad file to align it
     * with the start of a piece. The pad_file_limit is ignored unless the
     * {@link Flags#OPTIMIZE_ALIGNMENT} flag is passed. Typically it doesn't make sense
     * to set this any lower than 4kiB.
     *
     * @param value
     * @return
     */
    public TorrentBuilder padFileLimit(int value) {
        this.padFileLimit = value;
        return this;
    }

    /**
     * @return
     */
    public int flags() {
        return flags;
    }

    /**
     * Specifies options for the torrent creation. It can
     * be any combination of the flags defined by {@link Flags}
     *
     * @param value
     * @return
     */
    public TorrentBuilder flags(int value) {
        this.flags = value;
        return this;
    }

    /**
     * @return
     */
    public boolean merkle() {
        return (flags & Flags.MERKLE.swig()) != 0;
    }

    public TorrentBuilder merkle(boolean value) {
        if (value) {
            this.flags |= Flags.MERKLE.swig();
        } else {
            this.flags &= ~Flags.MERKLE.swig();
        }
        return this;
    }

    /**
     * @return
     */
    public int alignment() {
        return alignment;
    }

    /**
     * Used when pad files are enabled. This is the size
     * eligible files are aligned to. The default is -1, which means the
     * piece size of the torrent.
     *
     * @param value
     * @return
     */
    public TorrentBuilder alignment(int value) {
        this.alignment = value;
        return this;
    }

    /**
     * The comment for the torrent. The comment in a torrent file is optional.
     *
     * @return
     */
    public String comment() {
        return comment;
    }

    /**
     * Sets the comment for the torrent. The comment in a torrent file is optional.
     *
     * @param value
     * @return
     */
    public TorrentBuilder comment(String value) {
        this.comment = value;
        return this;
    }

    /**
     * The creator of the torrent. This is optional.
     *
     * @return
     */
    public String creator() {
        return creator;
    }

    /**
     * Sets the creator of the torrent. This is optional.
     *
     * @param value
     * @return
     */
    public TorrentBuilder creator(String value) {
        this.creator = value;
        return this;
    }

    /**
     * @return
     */
    public List<String> urlSeeds() {
        return urlSeeds;
    }

    /**
     * @param value
     * @return
     */
    public TorrentBuilder addUrlSeeds(List<String> value) {
        if (value != null) {
            this.urlSeeds.addAll(value);
        }
        return this;
    }

    /**
     * This adds a url seed to the torrent. You can have any number of url seeds. For a
     * single file torrent, this should be an HTTP url, pointing to a file with identical
     * content as the file of the torrent. For a multi-file torrent, it should point to
     * a directory containing a directory with the same name as this torrent, and all the
     * files of the torrent in it.
     *
     * @param value
     * @return
     */
    public TorrentBuilder addUrlSeed(String value) {
        if (value != null) {
            this.urlSeeds.add(value);
        }
        return this;
    }

    /**
     * @return
     */
    public List<String> httpSeeds() {
        return httpSeeds;
    }

    /**
     * @param value
     * @return
     */
    public TorrentBuilder addHttpSeeds(List<String> value) {
        if (value != null) {
            this.httpSeeds.addAll(value);
        }
        return this;
    }

    /**
     * This adds a HTTP seed to the torrent. You can have any number of url seeds. For a
     * single file torrent, this should be an HTTP url, pointing to a file with identical
     * content as the file of the torrent. For a multi-file torrent, it should point to
     * a directory containing a directory with the same name as this torrent, and all the
     * files of the torrent in it.
     *
     * @param value
     * @return
     */
    public TorrentBuilder addHttpSeed(String value) {
        if (value != null) {
            this.httpSeeds.add(value);
        }
        return this;
    }

    /**
     * @return
     */
    public List<Pair<String, Integer>> nodes() {
        return nodes;
    }

    /**
     * @param value
     * @return
     */
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
     *
     * @param value
     * @return
     */
    public TorrentBuilder addNode(Pair<String, Integer> value) {
        if (value != null) {
            this.nodes.add(value);
        }
        return this;
    }

    /**
     * @return
     */
    public List<Pair<String, Integer>> trackers() {
        return trackers;
    }

    /**
     * @param value
     * @return
     */
    public TorrentBuilder addTrackers(List<Pair<String, Integer>> value) {
        if (value != null) {
            this.trackers.addAll(value);
        }
        return this;
    }

    /**
     * @param value
     * @return
     */
    public TorrentBuilder addTracker(Pair<String, Integer> value) {
        if (value != null) {
            this.trackers.add(value);
        }
        return this;
    }

    /**
     * Adds a tracker to the torrent. This is not strictly required, but most torrents
     * use a tracker as their main source of peers. The url should be an http:// or udp://
     * url to a machine running a bittorrent tracker that accepts announces for this torrent's
     * info-hash. The tier is the fallback priority of the tracker. All trackers with tier 0 are
     * tried first (in any order). If all fail, trackers with tier 1 are tried. If all of those
     * fail, trackers with tier 2 are tried, and so on.
     *
     * @param url
     * @param tier
     * @return
     */
    public TorrentBuilder addTracker(String url, int tier) {
        return addTracker(new Pair<>(url, tier));
    }

    /**
     * @param url
     * @return
     */
    public TorrentBuilder addTracker(String url) {
        return addTracker(url, 0);
    }

    /**
     * @return
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
     *
     * @param value
     * @return
     */
    public TorrentBuilder setPrivate(boolean value) {
        this.priv = value;
        return this;
    }

    /**
     * @return
     */
    public List<Sha1Hash> similarTorrents() {
        return similarTorrents;
    }

    /**
     * @param value
     * @return
     */
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
     * BEP 38: http://www.bittorrent.org/beps/bep_0038.html
     *
     * @param value
     * @return
     */
    public TorrentBuilder addSimilarTorrent(Sha1Hash value) {
        if (value != null) {
            this.similarTorrents.add(value);
        }
        return this;
    }

    /**
     * @return
     */
    public List<String> collections() {
        return collections;
    }

    /**
     * @param value
     * @return
     */
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
     * BEP 38: http://www.bittorrent.org/beps/bep_0038.html
     *
     * @param value
     * @return
     */
    public TorrentBuilder addCollection(String value) {
        if (value != null) {
            this.collections.add(value);
        }
        return this;
    }

    /**
     * @return
     */
    public Listener listener() {
        return listener;
    }

    /**
     * @param value
     * @return
     */
    public TorrentBuilder listener(Listener value) {
        this.listener = value;
        return this;
    }

    /**
     * This function will generate a result withe the .torrent file as a bencode tree.
     *
     * @return
     * @throws IOException
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
                return listener != null ? listener.accept(p) : true;
            }
        };
        add_files_ex(fs, absPath.getPath(), l1, flags);
        if (fs.total_size() == 0) {
            throw new IOException("content total size can't be 0");
        }
        create_torrent t = new create_torrent(fs, pieceSize, padFileLimit, flags, alignment);
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
        if (!urlSeeds.isEmpty()) {
            for (String s : urlSeeds) {
                t.add_url_seed(new string_view(s));
            }
        }
        if (!httpSeeds.isEmpty()) {
            for (String s : httpSeeds) {
                t.add_http_seed(new string_view(s));
            }
        }
        if (!nodes.isEmpty()) {
            for (Pair<String, Integer> n : nodes) {
                t.add_node(n.to_string_int_pair());
            }
        }
        if (!trackers.isEmpty()) {
            for (Pair<String, Integer> tr : trackers) {
                t.add_tracker(new string_view(tr.first), tr.second);
            }
        }
        if (priv) {
            t.set_priv(priv);
        }

        if (!similarTorrents.isEmpty()) {
            for (Sha1Hash h : similarTorrents) {
                t.add_similar_torrent(h.swig());
            }
        }
        if (!collections.isEmpty()) {
            for (String s : collections) {
                t.add_collection(new string_view(s));
            }
        }

        return new Result(t);
    }

    /**
     *
     */
    public enum Flags {

        /**
         * This will insert pad files to align the files to piece boundaries, for
         * optimized disk-I/O. This will minimize the number of bytes of pad-
         * files, to keep the impact down for clients that don't support
         * them.
         */
        OPTIMIZE_ALIGNMENT(create_torrent.flags_t.optimize_alignment.swigValue()),

        /**
         * This will create a merkle hash tree torrent. A merkle torrent cannot
         * be opened in clients that don't specifically support merkle torrents.
         * The benefit is that the resulting torrent file will be much smaller and
         * not grow with more pieces. When this option is specified, it is
         * recommended to have a fairly small piece size, say 64 kiB.
         * When creating merkle torrents, the full hash tree is also generated
         * and should be saved off separately.
         */
        MERKLE(create_torrent.flags_t.merkle.swigValue()),

        /**
         * This will include the file modification time as part of the torrent.
         * This is not enabled by default, as it might cause problems when you
         * create a torrent from separate files with the same content, hoping to
         * yield the same info-hash. If the files have different modification times,
         * with this option enabled, you would get different info-hashes for the
         * files.
         */
        MODIFICATION_TIME(create_torrent.flags_t.modification_time.swigValue()),

        /**
         * If this flag is set, files that are symlinks get a symlink attribute
         * set on them and their data will not be included in the torrent. This
         * is useful if you need to reconstruct a file hierarchy which contains
         * symlinks.
         */
        SYMLINKS(create_torrent.flags_t.symlinks.swigValue()),

        /**
         * To create a torrent that can be updated via a *mutable torrent*
         * (see BEP38_). This also needs to be enabled for torrents that update
         * another torrent.
         * <p>
         * BEP38: http://www.bittorrent.org/beps/bep_0038.html
         */
        MUTABLE_TORRENT_SUPPORT(create_torrent.flags_t.mutable_torrent_support.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Flags(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue
         * @return
         */
        public static Flags fromSwig(int swigValue) {
            Flags[] enumValues = Flags.class.getEnumConstants();
            for (Flags ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     *
     */
    public interface Listener {

        /**
         * @param filename
         * @return
         */
        boolean accept(String filename);

        /**
         * @param pieceIndex
         * @param numPieces
         */
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

        /**
         * @return
         */
        public Entry entry() {
            return entry;
        }

        /**
         * @return
         */
        public int numPieces() {
            return t.num_pieces();
        }

        /**
         * @return
         */
        public int pieceLength() {
            return t.piece_length();
        }

        /**
         * @param index
         * @return
         */
        public int pieceSize(int index) {
            return t.piece_size(index);
        }

        /**
         * This function returns the merkle hash tree, if the torrent was created
         * as a merkle torrent. The tree is created by {@link #generate()} and won't
         * be valid until that function has been called.
         * <p>
         * When creating a merkle tree torrent, the actual tree itself has to
         * be saved off separately and fed into libtorrent the first time you start
         * seeding it, through the {@link TorrentInfo#merkleTree(List)} function.
         * From that point onwards, the tree will be saved in the resume data.
         *
         * @return
         */
        public ArrayList<Sha1Hash> merkleTree() {
            return Sha1Hash.convert(t.merkle_tree());
        }
    }
}
