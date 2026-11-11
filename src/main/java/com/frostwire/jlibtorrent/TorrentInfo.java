package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the metadata contained in a .torrent file or magnet link.
 * <p>
 * {@code TorrentInfo} is the immutable descriptor of a torrent, containing:
 * <ul>
 *   <li>Information hash(es) (SHA-1 and/or SHA-256)</li>
 *   <li>File list and sizes</li>
 *   <li>Piece hashes for data verification</li>
 *   <li>Tracker URLs</li>
 *   <li>Optional comments and creator info</li>
 *   <li>Web seed URLs</li>
 * </ul>
 * <p>
 * <b>Creating TorrentInfo:</b>
 * <pre>
 * // From a .torrent file
 * TorrentInfo ti = new TorrentInfo(new File("download.torrent"));
 *
 * // From raw bencoded data
 * byte[] torrentData = Files.readAllBytes(Paths.get("download.torrent"));
 * TorrentInfo ti = new TorrentInfo(torrentData);
 *
 * // From a MappedByteBuffer (memory-mapped file)
 * RandomAccessFile raf = new RandomAccessFile(torrentFile, "r");
 * MappedByteBuffer buffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
 * TorrentInfo ti = new TorrentInfo(buffer);
 * </pre>
 * <p>
 * <b>Querying Torrent Properties:</b>
 * Once created, you can query torrent details:
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 *
 * // Get files and sizes
 * FileStorage fs = ti.files();
 * for (int i = 0; i &lt; fs.numFiles(); i++) {
 *     System.out.println(fs.fileName(i) + ": " + fs.fileSize(i) + " bytes");
 * }
 *
 * // Get trackers (grouped by tier)
 * for (AnnounceEntry tracker : ti.trackers()) {
 *     System.out.println("Tier " + tracker.tier() + ": " + tracker.url());
 * }
 *
 * // Get total size
 * long totalSize = ti.totalSize();
 *
 * // Get info-hash
 * Sha1Hash infoHashV1 = ti.infoHashV1();
 * </pre>
 * <p>
 * <b>Modifying Torrent Metadata:</b>
 * You can modify the metadata outside the info dictionary before adding the torrent to a session:
 * <pre>
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 *
 * // Add additional trackers
 * ti.addTracker("http://backup-tracker.example.com:8080/announce", 1);
 * </pre>
 * <p>
 * <b>libtorrent 2.1:</b> libtorrent's {@code torrent_info} holds only the info dictionary. The rest
 * of a .torrent file (trackers, web seeds, DHT nodes, comment, creator, creation date and v2 piece
 * layers) comes back from {@code load_torrent_buffer()} in an {@code add_torrent_params}, which this
 * class keeps next to it. A {@code TorrentInfo} made from a bare {@link torrent_info}, such as
 * {@link TorrentHandle#torrentFile()}, has none of that until you add it.
 * <p>
 * <b>Thread Safety:</b>
 * Reading is thread-safe. The methods that add trackers, web seeds or nodes change state,
 * so synchronization is needed if shared across threads.
 *
 * @see SessionManager#download(TorrentInfo, File) - To start downloading
 * @see FileStorage - For file mapping and information
 * @see AnnounceEntry - For tracker information
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentInfo {

    private static Logger LOG = Logger.getLogger(TorrentInfo.class);

    private final torrent_info ti;

    // the .torrent file's metadata outside the info dictionary (libtorrent 2.1), with ti in its ti
    // field; created on first use for a TorrentInfo made from a bare torrent_info
    private add_torrent_params atp;

    public TorrentInfo(torrent_info ti) {
        this.ti = ti;
    }

    /**
     * @param atp what {@code load_torrent_buffer()} (or {@code load_torrent_file()},
     *            {@code read_resume_data()}) returned: its {@code ti} must be set
     */
    public TorrentInfo(add_torrent_params atp) {
        torrent_info t = atp.ti_ptr();
        if (t == null) {
            throw new IllegalArgumentException("add_torrent_params has no torrent_info");
        }
        // t is a view into atp, and keeps it reachable
        this.ti = t;
        this.atp = atp;
    }

    /**
     * Load the torrent file and decode it inside the constructor, for convenience.
     * <p>
     * This might not be the most suitable for applications that
     * want to be able to report detailed errors on what might go wrong.
     *
     * @param torrent the torrent file
     */
    public TorrentInfo(File torrent) {
        this(bdecode0(torrent));
    }

    /**
     * Load the torrent data and decode it inside the constructor, for convenience.
     * <p>
     * This might not be the most suitable for applications that
     * want to be able to report detailed errors on what might go wrong.
     *
     * @param data the torrent data
     */
    public TorrentInfo(byte[] data) {
        this(bdecode0(data));
    }

    public TorrentInfo(MappedByteBuffer buffer) {
        this(load0(buffer));
    }

    /**
     * @return the native object: the info dictionary only, see the class documentation
     */
    public torrent_info swig() {
        return this.ti;
    }

    /**
     * The native {@code add_torrent_params} holding this torrent's info dictionary ({@code ti}) and
     * the rest of its .torrent file: trackers, web seeds, DHT nodes, comment, creator, creation
     * date and v2 piece layers. Changes made through it show in this object.
     *
     * @return the native object, created on first use
     */
    public add_torrent_params swigParams() {
        if (atp == null) {
            add_torrent_params p = add_torrent_params.create_instance();
            p.set_ti(ti);
            atp = p;
        }
        return atp;
    }

    /**
     * Sets {@code p}'s torrent to this one: its info dictionary, and the trackers, web seeds, DHT
     * nodes, comment, creator, creation date and v2 piece layers {@code p} doesn't have already
     * (resume data read into {@code p} keeps its own). In libtorrent 2.1 the info dictionary alone
     * has no trackers, so a torrent added with only {@code set_ti()} would have none.
     *
     * @param p the parameters to add a torrent with
     */
    void applyTo(add_torrent_params p) {
        if (atp == null) {
            p.set_ti(ti);
        } else {
            libtorrent.add_torrent_file_ex(p, atp);
        }
    }

    /**
     * The files of the torrent and how its pieces map to them.
     * <p>
     * In libtorrent 2.1 this is the torrent's own, immutable layout ({@code torrent_info::layout()}):
     * file renames live in the torrent ({@link TorrentHandle#renameFile(int, String)}), not here.
     *
     * @return the files storage
     */
    public FileStorage files() {
        return new FileStorage(ti.layout(), ti);
    }

    /**
     * Same as {@link #files()}: libtorrent 2.1 no longer renames files in a torrent_info, so the
     * original and current file storage are the same.
     *
     * @return the file storage
     * @deprecated use {@link #files()}
     */
    @Deprecated
    public FileStorage origFiles() {
        return files();
    }

    /**
     * Adds a tracker to the announce-list.
     *
     * @param url the tracker url
     */
    public void addTracker(String url) {
        addTracker(url, 0);
    }

    /**
     * Adds a tracker to the announce-list. The {@code tier} determines the order in
     * which the trackers are to be tried.
     *
     * @param url  the tracker url
     * @param tier the tracker tier
     */
    public void addTracker(String url, int tier) {
        add_torrent_params p = swigParams();
        string_vector urls = p.get_trackers();
        int_vector tiers = p.get_tracker_tiers();
        // tracker_tiers may be shorter than trackers: a missing tier is 0
        while (tiers.size() < urls.size()) {
            tiers.add(0);
        }
        urls.add(url);
        tiers.add(tier);
        p.set_trackers(urls);
        p.set_tracker_tiers(tiers);
    }

    /**
     * Will return a sorted list with the trackers of this torrent info.
     * <p>
     * Each announce entry contains a string, which is the tracker url, and a tier index. The
     * tier index is the high-level priority. No matter which trackers that works or not, the
     * ones with lower tier will always be tried before the one with higher tier number.
     *
     * @return the list of trackers
     */
    public ArrayList<AnnounceEntry> trackers() {
        ArrayList<AnnounceEntry> l = new ArrayList<>();
        if (atp == null) {
            return l;
        }
        string_vector urls = atp.get_trackers();
        int_vector tiers = atp.get_tracker_tiers();
        for (int i = 0; i < urls.size(); i++) {
            AnnounceEntry e = new AnnounceEntry(urls.get(i));
            e.tier((short) (i < tiers.size() ? tiers.get(i) : 0));
            l.add(e);
        }
        // sorted by tier, as torrent_info::trackers() was in libtorrent 2.0 (a stable sort)
        l.sort((x, y) -> Integer.compare(x.tier(), y.tier()));
        return l;
    }

    /**
     * This function is related to BEP38_ (mutable torrents). The
     * vector returned from this correspond to the "similar" in the
     * .torrent file. The info-hashes from within the info-dict
     * and from outside of it are included.
     * <p>
     * BEP38: http://www.bittorrent.org/beps/bep_0038.html
     *
     * @return
     */
    public ArrayList<Sha1Hash> similarTorrents() {
        //return Sha1Hash.convert(ti.similar_torrents ());
        sha1_hash_vector vec = ti.similar_torrents();
        ArrayList<Sha1Hash> result = new ArrayList<>(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            result.add(new Sha1Hash(vec.get(i)));
        }
        return result;
    }

    /**
     * This function is related to BEP38_ (mutable torrents). The
     * vector returned from this correspond to the "collections" keys
     * in the .torrent file. The collections from within the info-dict
     * and from outside of it are included.
     * <p>
     * BEP38: http://www.bittorrent.org/beps/bep_0038.html
     *
     * @return
     */
    public ArrayList<String> collections() {
        string_vector v = ti.collections();
        int size = (int) v.size();

        ArrayList<String> l = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    /**
     * Clear the internal list of trackers.
     */
    public void clearTrackers() {
        if (atp != null) {
            atp.set_trackers(new string_vector());
            atp.set_tracker_tiers(new int_vector());
        }
    }

    /**
     * Adds a web seed (BEP 19, "url-list"): an HTTP server with the torrent's files.
     * <p>
     * libtorrent 2.1 removed BEP 17 HTTP seeds and per-web-seed authentication and headers, so
     * the {@code addHttpSeed} methods and the {@code addUrlSeed} overloads taking them are gone.
     *
     * @param url the web seed's URL
     */
    public void addUrlSeed(String url) {
        add_torrent_params p = swigParams();
        string_vector v = p.get_url_seeds();
        if (!v.contains(url)) {
            v.add(url);
            p.set_url_seeds(v);
        }
    }

    /**
     * Returns the web seeds (BEP 19) of the torrent.
     *
     * @return the list of web seeds
     */
    public ArrayList<WebSeedEntry> webSeeds() {
        ArrayList<WebSeedEntry> l = new ArrayList<>();
        if (atp == null) {
            return l;
        }
        string_vector v = atp.get_url_seeds();
        for (int i = 0; i < v.size(); i++) {
            l.add(new WebSeedEntry(v.get(i)));
        }
        return l;
    }

    /**
     * Replaces all web seeds with the ones specified in the
     * {@code seeds} list.
     *
     * @param seeds the list of web seeds
     */
    public void setWebSeeds(List<WebSeedEntry> seeds) {
        string_vector v = new string_vector();
        for (WebSeedEntry e : seeds) {
            v.add(e.url());
        }
        swigParams().set_url_seeds(v);
    }

    /**
     * The total number of bytes the torrent-file represents (all the files in it).
     *
     * @return
     */
    public long totalSize() {
        return ti.total_size();
    }

    /**
     * The number of byte for each piece.
     * <p>
     * The difference between  and  is that
     *  takes the piece index as argument and gives you the exact size
     * of that piece. It will always be the same as  except in the case
     * of the last piece, which may be smaller.
     *
     * @return
     */
    public int pieceLength() {
        return ti.piece_length();
    }

    /**
     * The total number of pieces.
     *
     * @return
     */
    public int numPieces() {
        return ti.num_pieces();
    }

    /**
     * returns the SHA1 info-hash of the torrent.
     *
     * @return
     */
    public Sha1Hash infoHashV1() {
        info_hash_t infohashes = ti.info_hashes();

        if (!infohashes.has_v1()) {
            LOG.error("TorrentInfo.infoHashV1(): Torrent does not have v1 info-hash");
            return null; // no v1 info-hash
        }

        sha1_hash h = ti.info_hashes().getV1();
        if (h.is_all_zeros()) {
            LOG.error("TorrentInfo.infoHashV1(): Torrent v1 info-hash is all zeros");
            return null; // no v1 info-hash
        }

        return new Sha1Hash(h);
    }

    public Sha256Hash infoHashV2() {
        info_hash_t infohashes = ti.info_hashes();

        if (!infohashes.has_v2()) {
            LOG.error("TorrentInfo.infoHashV2(): Torrent does not have v2 info-hash");
            return null; // no v2 info-hash
        }

        sha256_hash h = ti.info_hashes().getV2();
        if (h.is_all_zeros()) {
            LOG.error("TorrentInfo.infoHashV2(): Torrent v2 info-hash is all zeros");
            return null; // no v2 info-hash
        }
        return new Sha256Hash(h);
    }

    public info_hash_t infoHashType() {
        return ti.info_hashes();
    }

    /**
     * If you need index-access to files you can use this method
     * to access files using indices.
     *
     * @return
     */
    public int numFiles() {
        return ti.num_files();
    }

    /**
     * This function will map a piece index, a byte offset within that piece and
     * a size (in bytes) into the corresponding files with offsets where that data
     * for that piece is supposed to be stored.
     *
     * @param piece
     * @param offset
     * @param size
     * @return
     * @see com.frostwire.jlibtorrent.FileSlice
     */
    public ArrayList<FileSlice> mapBlock(int piece, long offset, int size) {
        return FileStorage.mapBlock(ti.map_block(piece, offset, size));
    }

    /**
     * This function will map a range in a specific file into a range in the torrent.
     * The {@code offset} parameter is the offset in the file, given in bytes, where
     * 0 is the start of the file.
     * <p>
     * The input range is assumed to be valid within the torrent. {@code offset + size}
     * is not allowed to be greater than the file size. {@code index}
     * must refer to a valid file, i.e. it cannot be {@code >= numFiles()}.
     *
     * @param file
     * @param offset
     * @param size
     * @return
     * @see com.frostwire.jlibtorrent.PeerRequest
     */
    public PeerRequest mapFile(int file, long offset, int size) {
        return new PeerRequest(ti.map_file(file, offset, size));
    }

    /**
     * Returns the SSL root certificate for the torrent, if it is an SSL
     * torrent. Otherwise returns an empty string. The certificate is
     * the the public certificate in x509 format.
     *
     * @return the cert as a byte array
     */
    byte[] sslCert() {
        byte_vector v = Vectors.ascii2byte_vector(ti.get_ssl_cert());
        return Vectors.byte_vector2bytes(v);
    }

    /**
     * Returns true if this torrent_info object has a torrent loaded.
     * <p>
     * This is primarily used to determine if a magnet link has had its
     * metadata resolved yet or not.
     *
     * @return
     */
    public boolean isValid() {
        return ti.is_valid();
    }

    /**
     * Returns true if this torrent is private. i.e., it should not be
     * distributed on the trackerless network (the kademlia DHT).
     *
     * @return
     */
    public boolean isPrivate() {
        return ti.priv();
    }

    /**
     * Returns true if this is an i2p torrent. This is determined by whether
     * or not it has a tracker whose URL domain name ends with ".i2p". i2p
     * torrents disable the DHT and local peer discovery as well as talking
     * to peers over anything other than the i2p network.
     *
     * @return
     */
    public boolean isI2p() {
        return ti.is_i2p();
    }

    /**
     * Returns the size of the piece at the given index.
     * <p>
     * For most pieces this returns the same value as {@link #pieceLength()}, but the last piece
     * may be smaller. For v1 and hybrid torrents this is the authoritative piece size.
     *
     * @param index the piece index
     * @return the size of the specified piece in bytes
     * @see #pieceLength()
     */
    public int pieceSize(int index) {
        return ti.piece_size(index);
    }

    /**
     * Returns the effective piece size for the given piece index when making or validating
     * piece hash requests.
     * <p>
     * This method is the cornerstone of correct piece handling across all BitTorrent protocol
     * versions. For <b>v1</b> and <b>hybrid</b> torrents it returns the same value as
     * {@link #pieceSize(int)} — every byte in the piece is data and participates in the hash.
     * For <b>v2-only</b> torrents, however, the value may be <i>strictly smaller</i> because
     * <b>pad blocks</b> are excluded from piece hash requests.
     * <p>
     * <b>What are pad blocks and why do they matter?</b>
     * <p>
     * BitTorrent v2 introduces per-file piece alignment. When files do not end exactly on a
     * piece boundary, the remaining bytes of the final piece are filled with pad blocks —
     * zero-length placeholders that have no data but still occupy space in the nominal piece
     * layout. {@link #pieceSize(int)} counts these pad blocks, giving you the <i>storage</i>
     * size of the piece. {@code pieceSizeForReq()} omits them, giving you the <i>data</i>
     * size that must be read, hashed, or transmitted in a piece request. Using the wrong
     * size in a v2 torrent leads to incorrect hash validation, truncated reads, or inflated
     * progress bars.
     * <p>
     * <b>Comparison with {@link #pieceSize(int)}:</b>
     * <table border="1" cellpadding="4" cellspacing="0">
     *   <tr><th>Torrent type</th><th>{@code pieceSize(piece)}</th><th>{@code pieceSizeForReq(piece)}</th></tr>
     *   <tr><td>v1 only</td><td>Full piece size (data only)</td><td>Same as {@code pieceSize()}</td></tr>
     *   <tr><td>Hybrid (v1+v2)</td><td>Full piece size (data only)</td><td>Same as {@code pieceSize()}</td></tr>
     *   <tr><td>v2 only</td><td>Storage size incl. pad blocks</td><td>Data size <i>excl.</i> pad blocks</td></tr>
     * </table>
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>When allocating buffers for piece hash verification in a v2 torrent</li>
     *   <li>When computing aggregate download progress that must match the hash layer</li>
     *   <li>When building a cross-version BitTorrent client that must handle v1, hybrid, and v2 torrents correctly</li>
     *   <li>When reporting piece-level statistics to the user (e.g. "3.2 MB of data verified")</li>
     * </ul>
     * <p>
     * <b>Example 1 — Piece validation loop:</b>
     * <pre>{@code
     * TorrentInfo ti = new TorrentInfo(new File("download.torrent"));
     * int numPieces = ti.numPieces();
     *
     * for (int i = 0; i < numPieces; i++) {
     *     // For v2 torrents pieceSizeForReq() gives the exact bytes to hash.
     *     // For v1/hybrid torrents it is identical to pieceSize(), so the same
     *     // loop works for all protocol versions without branching.
     *     int bytesToHash = ti.pieceSizeForReq(i);
     *     byte[] pieceBuffer = new byte[bytesToHash];
     *
     *     // readPieceData(i, pieceBuffer, bytesToHash);  // app-specific I/O
     *     // validateHash(i, pieceBuffer);                  // app-specific hash check
     *
     *     System.out.println("Piece " + i + ": hashed " + bytesToHash + " bytes");
     * }
     * }</pre>
     * <p>
     * <b>Example 2 — Progress calculation (v1 vs v2):</b>
     * <pre>{@code
     * TorrentInfo ti = new TorrentInfo(new File("download.torrent"));
     * int numPieces = ti.numPieces();
     * boolean[] havePiece = new boolean[numPieces]; // filled by the download engine
     *
     * long dataBytesDownloaded = 0;
     * long storageBytesDownloaded = 0;
     *
     * for (int i = 0; i < numPieces; i++) {
     *     if (havePiece[i]) {
         *         dataBytesDownloaded    += ti.pieceSizeForReq(i); // real payload
         *         storageBytesDownloaded += ti.pieceSize(i);       // incl. padding
     *     }
     * }
     *
     * // User-facing progress should use dataBytesDownloaded / totalSize()
     * // because pad blocks are not user data. Internal disk accounting
     * // may still want storageBytesDownloaded.
     * double userProgress = (double) dataBytesDownloaded / ti.totalSize();
     * System.out.printf("User progress: %.2f%%%n", userProgress * 100.0);
     * }</pre>
     * <p>
     * <b>Example 3 — Buffer allocation in a cross-version client:</b>
     * <pre>{@code
     * TorrentInfo ti = new TorrentInfo(new File("download.torrent"));
     * info_hash_t hashes = ti.infoHashType();
     * boolean isV2Only = hashes.has_v2() && !hashes.has_v1();
     *
     * // Always use pieceSizeForReq() for network buffers.
     * // It is safe for all torrent versions and avoids over-allocation
     * // in v2-only torrents where pad blocks would waste memory.
     * int maxPieceReq = 0;
     * for (int i = 0; i < ti.numPieces(); i++) {
     *     maxPieceReq = Math.max(maxPieceReq, ti.pieceSizeForReq(i));
     * }
     *
     * ByteBuffer pieceBuffer = ByteBuffer.allocateDirect(maxPieceReq);
     *
     * if (isV2Only) {
     *     System.out.println("v2 torrent detected — max request buffer: " + maxPieceReq);
     * } else {
     *     System.out.println("v1/hybrid torrent — max request buffer: " + maxPieceReq);
     * }
     * }</pre>
     *
     * @param piece the piece index (must be a valid piece, i.e. {@code >= 0} and {@code < numPieces()})
     * @return the effective piece size for hash requests, in bytes; for v1 and hybrid torrents this is
     *         identical to {@link #pieceSize(int)}, while for v2-only torrents it excludes pad blocks
     * @see #pieceSize(int)
     * @see #pieceLength()
     * @see #numPieces()
     * @see #totalSize()
     * @see #infoHashType()
     * @since 2.0.12.9
     */
    public int pieceSizeForReq(int piece) {
        return ti.piece_size_for_req(piece);
    }

    /**
     * takes a piece-index and returns the 20-bytes sha1-hash for that
     * piece and ``info_hash()`` returns the 20-bytes sha1-hash for the info-section of the
     * torrent file.
     *
     * @param index
     * @return
     */
    public Sha1Hash hashForPiece(int index) {
        return new Sha1Hash(ti.hash_for_piece(index));
    }

    public boolean isLoaded() {
        return ti.is_loaded();
    }

    /**
     * Returns a copy to the merkle tree for this
     * torrent, if any.
     *
     * @return
     */


    /**
     * returns the name of the torrent.
     * <p>
     * the name is an UTF-8 encoded strings.
     *
     * @return
     */
    public String name() {
        return ti.name();
    }

    /**
     * Returns the creation date of he torrent as time_t (`posix time`_).
     * If there's no time stamp in the torrent file,
     * a value of zero is returned.
     *
     * @return the time
     */
    public long creationDate() {
        return atp != null ? atp.getCreation_date() : 0;
    }

    /**
     * Returns the creator string in the torrent. If there is no creator string
     * it will return an empty string.
     *
     * @return the creator
     */
    public String creator() {
        return atp != null ? atp.getCreated_by() : "";
    }

    /**
     * Returns the comment associated with the torrent. If there's no comment,
     * it will return an empty string.
     * <p>
     * The comment is an UTF-8 encoded strings.
     *
     * @return the comment
     */
    public String comment() {
        return atp != null ? atp.getComment() : "";
    }

    /**
     * If this torrent contains any DHT nodes, they are returned in
     * their original form (host name and port number).
     *
     * @return
     */
    public ArrayList<Pair<String, Integer>> nodes() {
        if (atp == null) {
            return new ArrayList<>(0);
        }
        string_int_pair_vector v = atp.get_dht_nodes();
        int size = (int) v.size();

        ArrayList<Pair<String, Integer>> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            string_int_pair p = v.get(i);
            l.add(new Pair<>(p.getFirst(), p.getSecond()));
        }

        return l;
    }

    /**
     * This is used when creating torrent. Use this to add a known DHT node.
     * It may be used, by the client, to bootstrap into the DHT network.
     *
     * @param host
     * @param port
     */
    public void addNode(String host, int port) {
        add_torrent_params p = swigParams();
        string_int_pair_vector v = p.get_dht_nodes();
        v.add(new string_int_pair(host, port));
        p.set_dht_nodes(v);
    }

    /**
     * Generates a magnet URI from the specified torrent. If the torrent
     * is invalid, null is returned.
     * <p>
     * For more information about magnet links, see magnet-links_.
     *
     */
    public String makeMagnetUri() {
        if (!ti.is_valid()) {
            return null;
        }
        add_torrent_params p = swigParams();
        // libtorrent 2.1 takes the link's display name (dn=) only from the params' name, not
        // from the torrent they carry, so a link made from set_ti() alone would have none
        if (p.getName().isEmpty()) {
            p.setName(ti.name());
        }
        return libtorrent.make_magnet_uri(p);
    }

    /**
     * Rebuilds the .torrent file's bencoded structure from this metadata, for example to save a
     * torrent whose metadata came from a magnet link or from a session.
     * <p>
     * libtorrent needs every piece hash to write a torrent. A v2 torrent also needs its piece
     * layers, which {@link TorrentHandle#torrentFile()} leaves out; use
     * {@link TorrentHandle#torrentFileWithHashes()} for metadata taken from a session.
     *
     * @return the torrent as an {@link Entry}
     * @throws IllegalStateException if libtorrent can't write it, typically because v2 piece layers
     *         are missing; the message is libtorrent's
     */
    public Entry toEntry() {
        return new Entry(writeTorrentFile());
    }

    private entry writeTorrentFile() {
        try {
            // DHT nodes too, as libtorrent 2.0's create_torrent(torrent_info) wrote them
            entry e = libtorrent.write_torrent_file(swigParams(), libtorrent.getInclude_dht_nodes());
            // libtorrent 2.1 writes "announce" only for a single tracker. Write the first one next
            // to "announce-list" as well, as 2.0 did, for clients that don't read announce-list
            ArrayList<AnnounceEntry> trackers = trackers();
            if (!trackers.isEmpty() && !e.dict().containsKey("announce")) {
                e.set("announce", trackers.get(0).url());
            }
            return e;
        } catch (IllegalStateException e) {
            throw e;
        } catch (RuntimeException e) {
            // write_torrent_file() reports a torrent it can't write by throwing, which SWIG
            // turns into a bare RuntimeException (frostwire/frostwire-jlibtorrent#333).
            throw new IllegalStateException("Can't write this torrent: " + e.getMessage(), e);
        }
    }

    /**
     * Writes this metadata as a .torrent file's bytes.
     * <pre>{@code
     * TorrentInfo ti = handle.torrentFileWithHashes();
     * Files.write(Paths.get(ti.name() + ".torrent"), ti.bencode());
     * }</pre>
     *
     * @return the bencoded .torrent
     * @throws IllegalStateException if libtorrent can't write it; see {@link #toEntry()}
     */
    public byte[] bencode() {
        return toEntry().bencode();
    }

    public static TorrentInfo bdecode(byte[] data) {
        return new TorrentInfo(bdecode0(data));
    }

    /**
     * Loads a .torrent file's bytes with libtorrent's default load limits.
     *
     * @param data the .torrent file
     * @param ec   set to the error, if any
     * @return the torrent: the info dictionary in its {@code ti}, without one on error
     */
    static add_torrent_params loadTorrentBuffer(byte[] data, error_code ec) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        try {
            return libtorrent.load_torrent_buffer_ex(buffer, ec);
        } finally {
            buffer.delete();
        }
    }

    // helper function
    static ArrayList<AnnounceEntry> trackers(announce_entry_vector v) {
        int size = v.size();
        ArrayList<AnnounceEntry> l = new ArrayList<>(size);

        for (announce_entry announceEntry : v) {
            l.add(new AnnounceEntry(announceEntry));
        }

        return l;
    }

    private static add_torrent_params bdecode0(File file) {
        try {
            byte[] data = Files.bytes(file);
            return bdecode0(data);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't decode data from file: " + file, e);
        }
    }

    private static add_torrent_params bdecode0(byte[] data) {
        error_code ec = new error_code();
        try {
            add_torrent_params p = loadTorrentBuffer(data, ec);
            if (ec.value() != 0) {
                throw new IllegalArgumentException("Can't decode data: " + ec.message());
            }
            return p;
        } finally {
            ec.delete();
        }
    }

    private static add_torrent_params load0(MappedByteBuffer buffer) {
        try {
            long ptr = libtorrent_jni.directBufferAddress(buffer);
            long size = libtorrent_jni.directBufferCapacity(buffer);
            error_code ec = new error_code();
            try {
                add_torrent_params p = libtorrent.load_torrent_buffer_ptr(ptr, (int) size, ec);
                if (ec.value() != 0) {
                    throw new IllegalArgumentException("Can't decode data: " + ec.message());
                }
                return p;
            } finally {
                ec.delete();
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Can't decode data mapped buffer: " + e.getMessage(), e);
        }
    }
}
