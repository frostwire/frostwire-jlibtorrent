package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the information stored in a .torrent file
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentInfo {

    private final torrent_info ti;

    public TorrentInfo(torrent_info ti) {
        this.ti = ti;
    }

    /**
     * Load the torrent file and decode it inside the constructor, for convenience.
     * <p>
     * This might not be the most suitable for applications that
     * want to be able to report detailed errors on what might go wrong.
     *
     * @param torrent
     */
    public TorrentInfo(File torrent) {
        this(bdecode0(torrent));
    }

    public TorrentInfo(MappedByteBuffer buffer) {
        try {
            long ptr = libtorrent_jni.directBufferAddress(buffer);
            long size = libtorrent_jni.directBufferCapacity(buffer);

            error_code ec = new error_code();
            this.ti = new torrent_info(ptr, (int) size, ec);

            if (ec.value() != 0) {
                throw new IllegalArgumentException("Can't decode data: " + ec.message());
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Can't decode data mapped buffer: " + e.getMessage(), e);
        }
    }

    public torrent_info swig() {
        return this.ti;
    }

    /**
     * The {@link com.frostwire.jlibtorrent.FileStorage} object contains the information on
     * how to map the pieces to files.
     * <p>
     * It is separated from the {@link TorrentInfo} object because when creating torrents
     * a storage object needs to be created without having a torrent file. When renaming files
     * in a storage, the storage needs to make its own copy of the {@link FileStorage} in order
     * to make its mapping differ from the one in the torrent file.
     *
     * @return
     */
    public FileStorage files() {
        return new FileStorage(ti, ti.files());
    }

    /**
     * Returns the original (unmodified) file storage for this torrent. This
     * is used by the web server connection, which needs to request files with the original
     * names. Filename may be changed using {@link #renameFile(int, String)}.
     *
     * @return
     */
    public FileStorage origFiles() {
        return new FileStorage(ti, ti.orig_files());
    }

    /**
     * Renames a the file with the specified index to the new name. The new
     * filename is reflected by the {@link FileStorage} returned by {@link #files()}
     * but not by the one returned by {@link #origFiles()}.
     * <p>
     * If you want to rename the base name of the torrent (for a multifile
     * torrent), you can copy the {@code FileStorage} (see {@link #files()} and
     * {@link #origFiles()} ), change the name, and then use
     * {@link #remapFiles(FileStorage)}.
     * <p>
     * The {@code newFilename} can both be a relative path, in which case the
     * file name is relative to the {@code savePath} of the torrent. If the
     * {@code newFilename} is an absolute path (i.e. "is_complete(newFilename)
     * == true"), then the file is detached from the {@code savePath} of the
     * torrent. In this case the file is not moved when
     * {@link TorrentHandle#moveStorage(String, int)} is invoked.
     *
     * @param index
     * @param newFilename
     */
    public void renameFile(int index, String newFilename) {
        ti.rename_file(index, newFilename);
    }

    /**
     * Remaps the file storage to a new file layout. This can be used to, for
     * instance, download all data in a torrent to a single file, or to a
     * number of fixed size sector aligned files, regardless of the number
     * and sizes of the files in the torrent.
     * <p>
     * The new specified {@link FileStorage} must have the exact same size as
     * the current one.
     *
     * @param f
     */
    public void remapFiles(FileStorage f) {
        ti.remap_files(f.swig());
    }

    /**
     * Adds a tracker to the announce-list.
     *
     * @param url
     */
    public void addTracker(String url) {
        ti.add_tracker(url);
    }

    /**
     * Adds a tracker to the announce-list. The ``tier`` determines the order in
     * which the trackers are to be tried.
     *
     * @param url
     * @param tier
     */
    public void addTracker(String url, int tier) {
        ti.add_tracker(url, tier);
    }

    /**
     * Will return a sorted list with the trackers of this torrent info.
     * <p>
     * Each announce entry contains a string, which is the tracker url, and a tier index. The
     * tier index is the high-level priority. No matter which trackers that works or not, the
     * ones with lower tier will always be tried before the one with higher tier number.
     *
     * @return
     */
    public ArrayList<AnnounceEntry> trackers() {
        return trackers(ti.trackers());
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
        return Sha1Hash.convert(ti.similar_torrents());
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
        ti.trackers().clear();
    }

    /**
     * Adds one url to the list of url seeds. Currently, the only transport protocol
     * supported for the url is http.
     *
     * @param url
     * @see #addHttpSeed(String, String)
     */
    public void addUrlSeed(String url) {
        ti.add_url_seed(url);
    }

    /**
     * Adds one url to the list of url seeds. Currently, the only transport protocol
     * supported for the url is http.
     * <p>
     * The {@code externAuth} argument can be used for other authorization schemes than
     * basic HTTP authorization. If set, it will override any username and password
     * found in the URL itself. The string will be sent as the HTTP authorization header's
     * value (without specifying "Basic").
     * <p>
     * This is the same as calling {@link #addUrlSeed(String, String, List)} with an
     * empty list.
     *
     * @param url
     * @param externAuth
     */
    public void addUrlSeed(String url, String externAuth) {
        ti.add_url_seed(url, externAuth);
    }

    /**
     * Adds one url to the list of url seeds. Currently, the only transport protocol
     * supported for the url is http.
     * <p>
     * The {@code externAuth} argument can be used for other authorization schemes than
     * basic HTTP authorization. If set, it will override any username and password
     * found in the URL itself. The string will be sent as the HTTP authorization header's
     * value (without specifying "Basic").
     * <p>
     * The {@code extraHeaders} argument can be used to insert custom HTTP headers
     * in the requests to a specific web seed.
     *
     * @param url
     * @param externAuth
     * @param extraHeaders
     */
    public void addUrlSeed(String url, String externAuth, List<Pair<String, String>> extraHeaders) {
        string_string_pair_vector v = new string_string_pair_vector();

        for (Pair<String, String> p : extraHeaders) {
            v.push_back(p.to_string_string_pair());
        }

        ti.add_url_seed(url, externAuth, v);
    }

    /**
     * Adds one url to the list of http seeds. Currently, the only transport protocol supported for the url
     * is http.
     *
     * @param url
     */
    public void addHttpSeed(String url) {
        ti.add_url_seed(url);
    }

    /**
     * Adds one url to the list of http seeds. Currently, the only transport protocol supported for the url
     * is http.
     * <p>
     * The {@code externAuth} argument can be used for other authorization schemes than
     * basic HTTP authorization. If set, it will override any username and password
     * found in the URL itself. The string will be sent as the HTTP authorization header's
     * value (without specifying "Basic").
     *
     * @param url
     * @param externAuth
     */
    public void addHttpSeed(String url, String externAuth) {
        ti.add_url_seed(url, externAuth);
    }

    /**
     * Adds one url to the list of http seeds. Currently, the only transport protocol supported
     * for the url is http.
     * <p>
     * The {@code externAuth} argument can be used for other authorization schemes than
     * basic HTTP authorization. If set, it will override any username and password
     * found in the URL itself. The string will be sent as the HTTP authorization header's
     * value (without specifying "Basic").
     * <p>
     * The {@code extraHeaders} argument defaults to an empty list, but can be used to
     * insert custom HTTP headers in the requests to a specific web seed.
     *
     * @param url
     * @param externAuth
     * @param extraHeaders
     */
    public void addHttpSeed(String url, String externAuth, List<Pair<String, String>> extraHeaders) {
        string_string_pair_vector v = new string_string_pair_vector();

        for (Pair<String, String> p : extraHeaders) {
            v.push_back(p.to_string_string_pair());
        }

        ti.add_url_seed(url, externAuth, v);
    }

    /**
     * Returns all url seeds and http seeds in the torrent. Each entry
     * is a {@link WebSeedEntry} and may refer to either a url seed or http seed.
     *
     * @return the list of web seeds
     */
    public ArrayList<WebSeedEntry> webSeeds() {
        web_seed_entry_vector v = ti.web_seeds();
        int size = (int) v.size();

        ArrayList<WebSeedEntry> l = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
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
        web_seed_entry_vector v = new web_seed_entry_vector();

        for (WebSeedEntry e : seeds) {
            v.push_back(e.swig());
        }

        ti.set_web_seeds(v);
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
     * The difference between {@link #pieceSize(int)} and {@link #pieceLength()} is that
     * {@link #pieceSize(int)} takes the piece index as argument and gives you the exact size
     * of that piece. It will always be the same as {@link #pieceLength()} except in the case
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
     * returns the info-hash of the torrent.
     *
     * @return
     */
    public Sha1Hash infoHash() {
        return new Sha1Hash(ti.info_hash());
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

    public int pieceSize(int index) {
        return ti.piece_size(index);
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
    public ArrayList<Sha1Hash> merkleTree() {
        return Sha1Hash.convert(ti.merkle_tree());
    }

    /**
     * Copies the passed in merkle tree into the torrent info object.
     * <p>
     * You need to set the merkle tree for a torrent that you've just created
     * (as a merkle torrent). The merkle tree is retrieved from the
     * {@link #merkleTree()} function, and need to be saved
     * separately from the torrent file itself. Once it's added to
     * libtorrent, the merkle tree will be persisted in the resume data.
     *
     * @param tree
     */
    public void merkleTree(List<Sha1Hash> tree) {
        sha1_hash_vector v = new sha1_hash_vector();

        for (Sha1Hash h : tree) {
            v.push_back(h.swig());
        }

        ti.set_merkle_tree(v);
    }

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
     * returns the creation date of
     * the torrent as time_t (`posix time`_). If there's no time stamp in the torrent file,
     * a value of zero is returned.
     *
     * @return
     */
    public int creationDate() {
        return ti.creation_date();
    }

    /**
     * returns the creator string in the torrent. If there is no creator string
     * it will return an empty string.
     *
     * @return
     */
    public String creator() {
        return ti.creator();
    }

    /**
     * returns the comment associated with the torrent. If there's no comment,
     * it will return an empty string.
     * <p>
     * the comment is an UTF-8 encoded strings.
     *
     * @return
     */
    public String comment() {
        return ti.comment();
    }

    /**
     * If this torrent contains any DHT nodes, they are returned in
     * their original form (host name and port number).
     *
     * @return
     */
    public ArrayList<Pair<String, Integer>> nodes() {
        string_int_pair_vector v = ti.nodes();
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
        ti.add_node(new string_int_pair(host, port));
    }

    /**
     * This function looks up keys from the info-dictionary of the loaded
     * torrent file. It can be used to access extension values put in the
     * .torrent file. If the specified key cannot be found, it returns NULL.
     *
     * @param key
     * @return
     */
    public bdecode_node info(String key) {
        return ti.info(key);
    }

    /**
     * Returns whether or not this is a merkle torrent.
     * See BEP30: http://bittorrent.org/beps/bep_0030.html
     *
     * @return
     */
    public boolean isMerkleTorrent() {
        return ti.is_merkle_torrent();
    }

    /**
     * Generates a magnet URI from the specified torrent. If the torrent
     * is invalid, null is returned.
     * <p>
     * For more information about magnet links, see magnet-links_.
     *
     * @return
     */
    public String makeMagnetUri() {
        return ti.is_valid() ? libtorrent.make_magnet_uri(ti) : null;
    }

    public Entry toEntry() {
        return new Entry(new create_torrent(ti).generate());
    }

    public byte[] bencode() {
        return toEntry().bencode();
    }

    public static TorrentInfo bdecode(byte[] data) {
        return new TorrentInfo(bdecode0(data));
    }

    // helper function
    static ArrayList<AnnounceEntry> trackers(announce_entry_vector v) {
        int size = (int) v.size();
        ArrayList<AnnounceEntry> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new AnnounceEntry(v.get(i)));
        }

        return l;
    }

    private static torrent_info bdecode0(File file) {
        try {
            byte[] data = Files.bytes(file);
            return bdecode0(data);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't decode data from file: " + file, e);
        }
    }

    private static torrent_info bdecode0(byte[] data) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            ec.clear();
            torrent_info ti = new torrent_info(n, ec);
            buffer.clear(); // prevents GC
            if (ec.value() != 0) {
                throw new IllegalArgumentException("Can't decode data: " + ec.message());
            }
            return ti;
        } else {
            throw new IllegalArgumentException("Can't decode data: " + ec.message());
        }
    }
}
