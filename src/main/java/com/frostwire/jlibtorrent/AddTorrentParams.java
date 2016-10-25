package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link AddTorrentParams} is a parameter pack for adding torrents to a
 * session. The key fields when adding a torrent are:
 * <ul>
 * <li>ti - when you have a .torrent file</li>
 * <li>url - when you have a magnet link or http URL to the .torrent file</li>
 * <li>info_hash - when all you have is an info-hash (this is similar to a magnet link)</li>
 * </ul>
 * One of those fields need to be set. Another mandatory field is
 * {@link #savePath()}. The {@link AddTorrentParams} object is passed into one of the
 * {@link SessionHandle#addTorrent(AddTorrentParams, ErrorCode)} overloads or
 * {@link SessionHandle#asyncAddTorrent(AddTorrentParams)}.
 * <p>
 * If you only specify the info-hash, the torrent file will be downloaded
 * from peers, which requires them to support the metadata extension. It also
 * takes an optional {@link #name()} argument. This may be left empty in case no
 * name should be assigned to the torrent. In case it's not, the name is
 * used for the torrent as long as it doesn't have metadata.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AddTorrentParams {

    private final add_torrent_params p;

    /**
     * The native object
     *
     * @param p the native object
     */
    public AddTorrentParams(add_torrent_params p) {
        this.p = p;
    }

    /**
     * Creates an empty parameters object with the default storage.
     */
    public AddTorrentParams() {
        this(add_torrent_params.create_instance());
    }

    /**
     * @return the native object
     */
    public add_torrent_params swig() {
        return p;
    }

    /**
     * Filled in by the constructor. It is used for forward binary compatibility.
     *
     * @return the version
     */
    public int version() {
        return p.getVersion();
    }

    /**
     * {@link TorrentInfo} object with the torrent to add. Unless the {@link #url()}
     * or {@link #infoHash()} is set, this is required to be initialized.
     *
     * @return the torrent info or null if not set
     */
    public TorrentInfo torrentInfo() {
        torrent_info ti = p.ti_ptr();
        return ti != null && ti.is_valid() ? new TorrentInfo(ti) : null;
    }

    /**
     * {@link TorrentInfo} object with the torrent to add. Unless the {@link #url()}
     * or {@link #infoHash()} is set, this is required to be initialized.
     *
     * @param ti the torrent info
     */
    public void torrentInfo(TorrentInfo ti) {
        p.set_ti(ti.swig());
    }

    /**
     * If the torrent doesn't have a tracker, but relies on the DHT to find
     * peers, the {@link #trackers(List)} can specify tracker URLs for the torrent.
     *
     * @return the list of trackers
     */
    public ArrayList<String> trackers() {
        string_vector v = p.getTrackers();
        int size = (int) v.size();
        ArrayList<String> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    /**
     * If the torrent doesn't have a tracker, but relies on the DHT to find
     * peers, this method can specify tracker URLs for the torrent.
     *
     * @param value the list of trackers
     */
    public void trackers(List<String> value) {
        string_vector v = new string_vector();

        for (String s : value) {
            v.push_back(s);
        }

        p.setTrackers(v);
    }

    /**
     * The tiers the URLs in {@link #trackers()} belong to. Trackers belonging to
     * different tiers may be treated differently, as defined by the multi
     * tracker extension. This is optional, if not specified trackers are
     * assumed to be part of tier 0, or whichever the last tier was as
     * iterating over the trackers.
     *
     * @return the list of trackers tiers
     */
    public ArrayList<Integer> trackerTiers() {
        int_vector v = p.getTracker_tiers();
        int size = (int) v.size();
        ArrayList<Integer> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    /**
     * The tiers the URLs in {@link #trackers()} belong to. Trackers belonging to
     * different tiers may be treated differently, as defined by the multi
     * tracker extension. This is optional, if not specified trackers are
     * assumed to be part of tier 0, or whichever the last tier was as
     * iterating over the trackers.
     *
     * @param value the list of trackers tiers
     */
    public void trackerTiers(List<Integer> value) {
        int_vector v = new int_vector();

        for (Integer t : value) {
            v.push_back(t);
        }

        p.setTracker_tiers(v);
    }

    /**
     * A list of hostname and port pairs, representing DHT nodes to be added
     * to the session (if DHT is enabled). The hostname may be an IP address.
     *
     * @return the list of DHT nodes
     */
    public ArrayList<Pair<String, Integer>> dhtNodes() {
        string_int_pair_vector v = p.getDht_nodes();
        int size = (int) v.size();
        ArrayList<Pair<String, Integer>> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            string_int_pair n = v.get(i);
            l.add(new Pair<>(n.getFirst(), n.getSecond()));
        }

        return l;
    }

    /**
     * A list of hostname and port pairs, representing DHT nodes to be added
     * to the session (if DHT is enabled). The hostname may be an IP address.
     *
     * @param value the list of DHT nodes
     */
    public void dhtNodes(List<Pair<String, Integer>> value) {
        string_int_pair_vector v = new string_int_pair_vector();

        for (Pair<String, Integer> p : value) {
            v.push_back(p.to_string_int_pair());
        }

        p.setDht_nodes(v);
    }

    /**
     * @return the name
     */
    public String name() {
        return p.getName();
    }

    /**
     * @param value the name
     */
    public void name(String value) {
        p.setName(value);
    }

    /**
     * The path where the torrent is or will be stored. Note that this may
     * also be stored in resume data. If you want the save path saved in
     * the resume data to be used, you need to set the
     * flag_use_resume_save_path flag.
     * <p>
     * .. note::
     * On windows this path (and other paths) are interpreted as UNC
     * paths. This means they must use backslashes as directory separators
     *
     * @return the save path
     */
    public String savePath() {
        return p.getSave_path();
    }

    /**
     * The path where the torrent is or will be stored. Note that this may
     * also be stored in resume data. If you want the save path saved in
     * the resume data to be used, you need to set the
     * flag_use_resume_save_path flag.
     * <p>
     * .. note::
     * On windows this path (and other paths) are interpreted as UNC
     * paths. This means they must use backslashes as directory separators
     *
     * @param value the save path
     */
    public void savePath(String value) {
        p.setSave_path(value);
    }

    /**
     * @return the storage mode
     * @see StorageMode
     */
    public StorageMode storageMode() {
        return StorageMode.fromSwig(p.getStorage_mode().swigValue());
    }

    /**
     * @param value the storage mode
     * @see StorageMode
     */
    public void storageMode(StorageMode value) {
        p.setStorage_mode(storage_mode_t.swigToEnum(value.swig()));
    }

    /**
     * The default tracker id to be used when announcing to trackers. By
     * default this is empty, and no tracker ID is used, since this is an
     * optional argument. If a tracker returns a tracker ID, that ID is used
     * instead of this.
     *
     * @return the trackerid url parameter
     */
    public String trackerId() {
        return p.getTrackerid();
    }

    /**
     * The default tracker id to be used when announcing to trackers. By
     * default this is empty, and no tracker ID is used, since this is an
     * optional argument. If a tracker returns a tracker ID, that ID is used
     * instead of this.
     *
     * @param value the trackerid url parameter
     */
    public void trackerId(String value) {
        p.setTrackerid(value);
    }

    /**
     * If you specify a ``url``, the torrent will be set in
     * ``downloading_metadata`` state until the .torrent file has been
     * downloaded. If there's any error while downloading, the torrent will
     * be stopped and the torrent error state (``torrent_status::error``)
     * will indicate what went wrong. The ``url`` may refer to a magnet link
     * or a regular http URL.
     * <p>
     * If it refers to an HTTP URL, the info-hash for the added torrent will
     * not be the true info-hash of the .torrent. Instead a placeholder,
     * unique, info-hash is used which is later updated once the .torrent
     * file has been downloaded.
     *
     * @return the url
     */
    public String url() {
        return p.getUrl();
    }

    /**
     * If you specify a ``url``, the torrent will be set in
     * ``downloading_metadata`` state until the .torrent file has been
     * downloaded. If there's any error while downloading, the torrent will
     * be stopped and the torrent error state (``torrent_status::error``)
     * will indicate what went wrong. The ``url`` may refer to a magnet link
     * or a regular http URL.
     * <p>
     * If it refers to an HTTP URL, the info-hash for the added torrent will
     * not be the true info-hash of the .torrent. Instead a placeholder,
     * unique, info-hash is used which is later updated once the .torrent
     * file has been downloaded.
     *
     * @param value the url
     */
    public void url(String value) {
        p.setUrl(value);
    }

    /**
     * Set this to the info hash of the torrent to add in case the info-hash
     * is the only known property of the torrent. i.e. you don't have a
     * .torrent file nor a magnet link.
     *
     * @return the info-hash
     */
    public Sha1Hash infoHash() {
        return new Sha1Hash(p.getInfo_hash());
    }

    /**
     * Set this to the info hash of the torrent to add in case the info-hash
     * is the only known property of the torrent. i.e. you don't have a
     * .torrent file nor a magnet link.
     *
     * @param value the info-hash
     */
    public void infoHash(Sha1Hash value) {
        p.setInfo_hash(value.swig());
    }

    /**
     * @return max uploads limit
     */
    public int maxUploads() {
        return p.getMax_uploads();
    }

    /**
     * @param value max uploads limit
     */
    public void maxUploads(int value) {
        p.setMax_uploads(value);
    }

    public int maxConnections() {
        return p.getMax_connections();
    }

    public void maxConnections(int value) {
        p.setMax_connections(value);
    }

    public int uploadLimit() {
        return p.getUpload_limit();
    }

    public void uploadLimit(int value) {
        p.setUpload_limit(value);
    }

    public int downloadLimit() {
        return p.getDownload_limit();
    }

    public void downloadLimit(int value) {
        p.setDownload_limit(value);
    }

    /**
     * Flags controlling aspects of this torrent and how it's added. See
     * {@link com.frostwire.jlibtorrent.swig.add_torrent_params.flags_t} for details.
     *
     * @return
     */
    public long flags() {
        return p.getFlags();
    }

    /**
     * Flags controlling aspects of this torrent and how it's added. See
     * {@link com.frostwire.jlibtorrent.swig.add_torrent_params.flags_t} for details.
     *
     * @param flags
     */
    public void flags(long flags) {
        p.setFlags(flags);
    }

    /**
     * Url seeds to be added to the torrent (`BEP 17`_).
     *
     * @return the url seeds
     */
    public ArrayList<String> urlSeeds() {
        string_vector v = p.getUrl_seeds();
        int size = (int) v.size();
        ArrayList<String> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    /**
     * Url seeds to be added to the torrent (`BEP 17`_).
     *
     * @param value the url seeds
     */
    public void urlSeeds(List<String> value) {
        string_vector v = new string_vector();

        for (String s : value) {
            v.push_back(s);
        }

        p.setUrl_seeds(v);
    }

    /**
     * Can be set to control the initial file priorities when adding a
     * torrent. The semantics are the same as for
     * {@link TorrentHandle#prioritizeFiles(Priority[])}.
     *
     * @param priorities the priorities
     */
    public void filePriorities(Priority[] priorities) {
        p.setFile_priorities(Priority.array2byte_vector(priorities));
    }

    /**
     * This sets the priorities for each individual piece in the torrent. Each
     * element in the vector represent the piece with the same index. If you
     * set both file- and piece priorities, file priorities will take
     * precedence.
     *
     * @param priorities the priorities
     */
    public void piecePriorities(Priority[] priorities) {
        p.setPiece_priorities(Priority.array2byte_vector(priorities));
    }

    /**
     * Peers to add to the torrent, to be tried to be connected to as
     * bittorrent peers.
     *
     * @return the peers list
     */
    public ArrayList<TcpEndpoint> peers() {
        tcp_endpoint_vector v = p.getPeers();
        int size = (int) v.size();
        ArrayList<TcpEndpoint> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            l.add(new TcpEndpoint(v.get(i)));
        }

        return l;
    }

    /**
     * Peers to add to the torrent, to be tried to be connected to as
     * bittorrent peers.
     *
     * @param value the peers list
     */
    public void peers(List<TcpEndpoint> value) {
        tcp_endpoint_vector v = new tcp_endpoint_vector();

        for (TcpEndpoint endp : value) {
            v.push_back(endp.swig());
        }

        p.setPeers(v);
    }

    /**
     * Peers banned from this torrent. The will not be connected to.
     *
     * @return the peers list
     */
    public ArrayList<TcpEndpoint> bannedPeers() {
        tcp_endpoint_vector v = p.getBanned_peers();
        int size = (int) v.size();
        ArrayList<TcpEndpoint> l = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            l.add(new TcpEndpoint(v.get(i)));
        }

        return l;
    }

    /**
     * Peers banned from this torrent. The will not be connected to.
     *
     * @param value the peers list
     */
    public void bannedPeers(List<TcpEndpoint> value) {
        tcp_endpoint_vector v = new tcp_endpoint_vector();

        for (TcpEndpoint endp : value) {
            v.push_back(endp.swig());
        }

        p.setBanned_peers(v);
    }

    public static AddTorrentParams createInstance() {
        return new AddTorrentParams(add_torrent_params.create_instance());
    }

    public static AddTorrentParams createInstanceDisabledStorage() {
        return new AddTorrentParams(add_torrent_params.create_instance_disabled_storage());
    }

    public static AddTorrentParams createInstanceZeroStorage() {
        return new AddTorrentParams(add_torrent_params.create_instance_zero_storage());
    }

    /**
     * Helper function to parse a magnet uri and fill the parameters.
     *
     * @param uri    the magnet uri
     * @param params the parameters to fill
     * @return the same object as params to allow for fluently style
     */
    public static AddTorrentParams parseMagnetUri(String uri, AddTorrentParams params) {
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, params.swig(), ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException("Invalid magnet uri: " + ec.message());
        }
        return params;
    }
}
