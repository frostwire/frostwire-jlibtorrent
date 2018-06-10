package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.alerts.DhtMutableItemAlert;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The session holds all state that spans multiple torrents. Among other
 * things it runs the network loop and manages all torrents. Once it's
 * created, the session object will spawn the main thread that will do all
 * the work. The main thread will be idle as long it doesn't have any
 * torrents to participate in.
 * <p>
 * This class belongs to a middle logical layer of abstraction. It's a wrapper
 * of the underlying swig session object (from libtorrent), but it does not
 * expose all the raw features.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionHandle {

    private static final Logger LOG = Logger.getLogger(SessionHandle.class);

    protected final session_handle s;

    /**
     * @param s the native object
     */
    public SessionHandle(session_handle s) {
        this.s = s;
    }

    /**
     * @return the native object
     */
    public session_handle swig() {
        return s;
    }

    public boolean isValid() {
        return s.is_valid();
    }

    /**
     * Saves settings (i.e. the {@link SettingsPack}).
     */
    public static final save_state_flags_t SAVE_SETTINGS = session_handle.save_settings;

    /**
     * Saves {@link DhtSettings}.
     */
    public static final save_state_flags_t SAVE_DHT_SETTINGS = session_handle.save_dht_settings;

    /**
     * Saves dht state such as nodes and node-id, possibly accelerating
     * joining the DHT if provided at next session startup.
     */
    public static final save_state_flags_t SAVE_DHT_STATE = session_handle.save_dht_state;

    /**
     * Loads and saves all session settings, including dht settings,
     * encryption settings and proxy settings. This method
     * internally writes all keys to an {@link entry} that is returned
     * as a bencoded byte array.
     * <p>
     * The {@code flags} argument passed in to this method can be used to
     * filter which parts of the session state to save. By default, all state
     * is saved (except for the individual torrents).
     *
     * @return the bencoded byte array
     */
    public byte[] saveState(save_state_flags_t flags) {
        entry e = new entry();
        s.save_state(e, flags);
        return Vectors.byte_vector2bytes(e.bencode());
    }

    /**
     * Same as calling {@link #saveState(save_state_flags_t)} with all save state flags.
     *
     * @return the bencoded byte array
     */
    public byte[] saveState() {
        entry e = new entry();
        s.save_state(e);
        return Vectors.byte_vector2bytes(e.bencode());
    }

    /**
     * Loads all session settings, including DHT settings,
     * encryption settings and proxy settings.
     * <p>
     * This method expects a byte array that it is a
     * bencoded buffer.
     * <p>
     * The {@code flags} argument passed in to this method can be used to
     * filter which parts of the session state to load. By default, all state
     * is restored (except for the individual torrents).
     *
     * @param data the bencoded byte array
     */
    public void loadState(byte[] data, save_state_flags_t flags) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            s.load_state(n, flags);
            buffer.clear(); // prevents GC
        } else {
            LOG.error("failed to decode bencoded data: " + ec.message());
        }
    }

    /**
     * Same as calling {@link #loadState(byte[], save_state_flags_t)} with all
     * save state flags.
     */
    public void loadState(byte[] data) {
        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            s.load_state(n);
            buffer.clear(); // prevents GC
        } else {
            LOG.error("failed to decode bencoded data: " + ec.message());
        }
    }

    /**
     * This functions instructs the session to post the
     * {@link com.frostwire.jlibtorrent.alerts.StateUpdateAlert},
     * containing the status of all torrents whose state changed since the
     * last time this function was called.
     * <p>
     * Only torrents who has the state subscription flag set will be
     * included. This flag is on by default. See {@link AddTorrentParams}.
     * the {@code flags} argument is the same as for torrent_handle::status().
     *
     * @param flags or-combination of native values
     */
    public void postTorrentUpdates(status_flags_t flags) {
        s.post_torrent_updates(flags);
    }

    /**
     * This functions instructs the session to post the
     * {@link com.frostwire.jlibtorrent.alerts.StateUpdateAlert},
     * containing the status of all torrents whose state changed since the
     * last time this function was called.
     * <p>
     * Only torrents who has the state subscription flag set will be
     * included.
     */
    public void postTorrentUpdates() {
        s.post_torrent_updates();
    }

    /**
     * This function will post a {@link com.frostwire.jlibtorrent.alerts.SessionStatsAlert} object, containing a
     * snapshot of the performance counters from the internals of libtorrent.
     * To interpret these counters, query the session via
     * session_stats_metrics().
     */
    public void postSessionStats() {
        s.post_session_stats();
    }

    /**
     * This will cause a dht_stats_alert to be posted.
     */
    public void postDhtStats() {
        s.post_dht_stats();
    }

    /**
     * Looks for a torrent with the given info-hash. In case there is such
     * a torrent in the session, a {@link TorrentHandle} to that torrent
     * is returned.
     * <p>
     * In case the torrent cannot be found, a null is returned.
     *
     * @param infoHash
     * @return
     */
    public TorrentHandle findTorrent(Sha1Hash infoHash) {
        torrent_handle th = s.find_torrent(infoHash.swig());

        return th != null && th.is_valid() ? new TorrentHandle(th) : null;
    }

    /**
     * Returns a list of torrent handles to all the
     * torrents currently in the session.
     *
     * @return
     */
    public List<TorrentHandle> torrents() {
        torrent_handle_vector v = s.get_torrents();
        int size = (int) v.size();

        ArrayList<TorrentHandle> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new TorrentHandle(v.get(i)));
        }

        return l;
    }

    /**
     * You add torrents through the {@link #addTorrent(AddTorrentParams, ErrorCode)}
     * function where you give an object with all the parameters.
     * The {@code addTorrent} overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a {@link TorrentHandle}. In order to add torrents more
     * efficiently, consider using {@link #asyncAddTorrent(AddTorrentParams)}
     * which returns immediately, without waiting for the torrent to add.
     * Notification of the torrent being added is sent as
     * {@link com.frostwire.jlibtorrent.alerts.AddTorrentAlert}.
     * <p>
     * The {@link TorrentHandle} returned by this method can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * the error code will be set to {@link libtorrent_errors#duplicate_torrent}
     * unless {@code flag_duplicate_is_error}
     * is set to false. In that case, {@code addTorrent} will return the handle
     * to the existing torrent.
     * <p>
     * All torrent handles must be destructed before the session is destructed!
     *
     * @param params
     * @param ec
     * @return
     */
    public TorrentHandle addTorrent(AddTorrentParams params, ErrorCode ec) {
        return new TorrentHandle(s.add_torrent(params.swig(), ec.swig()));
    }

    public void asyncAddTorrent(AddTorrentParams params) {
        s.async_add_torrent(params.swig());
    }

    /**
     * Delete the files belonging to the torrent from disk,
     * including the part-file, if there is one.
     */
    public static final remove_flags_t DELETE_FILES = session_handle.delete_files;

    /**
     * Delete just the part-file associated with this torrent.
     */
    public static final remove_flags_t DELETE_PARTFILE = session_handle.delete_partfile;

    /**
     * This method will close all peer connections associated with the torrent and tell the
     * tracker that we've stopped participating in the swarm. This operation cannot fail.
     * When it completes, you will receive a torrent_removed_alert.
     * <p>
     * The optional second argument options can be used to delete all the files downloaded
     * by this torrent. To do so, pass in the value session::delete_files. The removal of
     * the torrent is asynchronous, there is no guarantee that adding the same torrent immediately
     * after it was removed will not throw a libtorrent_exception exception. Once the torrent
     * is deleted, a torrent_deleted_alert is posted.
     *
     * @param th the handle
     */
    public void removeTorrent(TorrentHandle th, remove_flags_t options) {
        if (th.isValid()) {
            s.remove_torrent(th.swig(), options);
        }
    }

    /**
     * This method will close all peer connections associated with the torrent and tell the
     * tracker that we've stopped participating in the swarm. This operation cannot fail.
     * When it completes, you will receive a torrent_removed_alert.
     *
     * @param th
     */
    public void removeTorrent(TorrentHandle th) {
        if (th.isValid()) {
            s.remove_torrent(th.swig());
        }
    }

    /**
     * Pausing the session has the same effect as pausing every torrent in
     * it, except that torrents will not be resumed by the auto-manage
     * mechanism.
     */
    public void pause() {
        s.pause();
    }

    /**
     * Resuming will restore the torrents to their previous paused
     * state. i.e. the session pause state is separate from the torrent pause
     * state. A torrent is inactive if it is paused or if the session is
     * paused.
     */
    public void resume() {
        s.resume();
    }

    public boolean isPaused() {
        return s.is_paused();
    }

    // starts/stops UPnP, NATPMP or LSD port mappers they are stopped by
    // default These functions are not available in case
    // ``TORRENT_DISABLE_DHT`` is defined. ``start_dht`` starts the dht node
    // and makes the trackerless service available to torrents. The startup
    // state is optional and can contain nodes and the node id from the
    // previous session. The dht node state is a bencoded dictionary with the
    // following entries:
    //
    // nodes
    // 	A list of strings, where each string is a node endpoint encoded in
    // 	binary. If the string is 6 bytes long, it is an IPv4 address of 4
    // 	bytes, encoded in network byte order (big endian), followed by a 2
    // 	byte port number (also network byte order). If the string is 18
    // 	bytes long, it is 16 bytes of IPv6 address followed by a 2 bytes
    // 	port number (also network byte order).
    //
    // node-id
    // 	The node id written as a readable string as a hexadecimal number.
    //
    // ``dht_state`` will return the current state of the dht node, this can
    // be used to start up the node again, passing this entry to
    // ``start_dht``. It is a good idea to save this to disk when the session
    // is closed, and read it up again when starting.
    //
    // If the port the DHT is supposed to listen on is already in use, and
    // exception is thrown, ``asio::error``.
    //
    // ``stop_dht`` stops the dht node.
    //
    // ``add_dht_node`` adds a node to the routing table. This can be used if
    // your client has its own source of bootstrapping nodes.
    //
    // ``set_dht_settings`` sets some parameters available to the dht node.
    // See dht_settings for more information.
    //
    // ``is_dht_running()`` returns true if the DHT support has been started
    // and false
    // otherwise.

    void setDhtSettings(DhtSettings settings) {
        s.set_dht_settings(settings.swig());
    }

    public boolean isDhtRunning() {
        return s.is_dht_running();
    }

    /**
     * takes a host name and port pair. That endpoint will be
     * pinged, and if a valid DHT reply is received, the node will be added to
     * the routing table.
     *
     * @param node
     */
    public void addDhtNode(Pair<String, Integer> node) {
        s.add_dht_node(node.to_string_int_pair());
    }

    /**
     * Applies the settings specified by the settings pack {@code sp}. This is an
     * asynchronous operation that will return immediately and actually apply
     * the settings to the main thread of libtorrent some time later.
     *
     * @param sp the settings
     */
    public void applySettings(SettingsPack sp) {
        s.apply_settings(sp.swig());
    }

    /**
     * @return a copy of the internal settings
     */
    public SettingsPack settings() {
        return new SettingsPack(s.get_settings());
    }

    /**
     * Adds a port forwarding on UPnP and/or NAT-PMP, using PCP if supported,
     * whichever is enabled. The return value is a handle referring to the
     * port mapping that was just created. Pass it to {@link #deletePortMapping}
     * to remove it.
     *
     * @param t            the mapping protocol
     * @param externalPort the external port
     * @param localPort    the local port
     * @return the array of port mapping ids
     */
    public int[] addPortMapping(PortmapProtocol t, int externalPort, int localPort) {
        port_mapping_t_vector v = s.add_port_mapping(portmap_protocol.swigToEnum(t.swig()), externalPort, localPort);

        int size = (int) v.size();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public void deletePortMapping(int handle) {
        s.delete_port_mapping(handle);
    }

    /**
     * This option indicates if the ports are mapped using natpmp
     * and UPnP. If mapping was already made, they are deleted and added
     * again. This only works if natpmp and/or upnp are configured to be
     * enable.
     */
    public static final reopen_network_flags_t REOPEN_MAP_PORTS = session_handle.reopen_map_ports;

    /**
     * Instructs the session to reopen all listen and outgoing sockets.
     * <p>
     * It's useful in the case your platform doesn't support the built in
     * IP notifier mechanism, or if you have a better more reliable way to
     * detect changes in the IP routing table.
     *
     * @param options the options
     */
    public void reopenNetworkSockets(reopen_network_flags_t options) {
        s.reopen_network_sockets(options);
    }

    /**
     * Instructs the session to reopen all listen and outgoing sockets.
     * <p>
     * It's useful in the case your platform doesn't support the built in
     * IP notifier mechanism, or if you have a better more reliable way to
     * detect changes in the IP routing table.
     */
    public void reopenNetworkSockets() {
        s.reopen_network_sockets();
    }

    /**
     * Query the DHT for an immutable item at the target hash.
     * the result is posted as a {@link DhtImmutableItemAlert}.
     *
     * @param target
     */
    public void dhtGetItem(Sha1Hash target) {
        s.dht_get_item(target.swig());
    }

    /**
     * Query the DHT for a mutable item under the public key {@code key}.
     * this is an ed25519 key. The {@code salt} argument is optional and may be left
     * as an empty string if no salt is to be used.
     * <p>
     * if the item is found in the DHT, a {@link DhtMutableItemAlert} is
     * posted.
     *
     * @param key
     * @param salt
     */
    public void dhtGetItem(byte[] key, byte[] salt) {
        s.dht_get_item(Vectors.bytes2byte_vector(key), Vectors.bytes2byte_vector(salt));
    }

    /**
     * Store the given bencoded data as an immutable item in the DHT.
     * the returned hash is the key that is to be used to look the item
     * up again. It's just the sha-1 hash of the bencoded form of the
     * structure.
     *
     * @param entry
     * @return
     */
    public Sha1Hash dhtPutItem(Entry entry) {
        return new Sha1Hash(s.dht_put_item(entry.swig()));
    }

    // store an immutable item. The ``key`` is the public key the blob is
    // to be stored under. The optional ``salt`` argument is a string that
    // is to be mixed in with the key when determining where in the DHT
    // the value is to be stored. The callback function is called from within
    // the libtorrent network thread once we've found where to store the blob,
    // possibly with the current value stored under the key.
    // The values passed to the callback functions are:
    //
    // entry& value
    // 	the current value stored under the key (may be empty). Also expected
    // 	to be set to the value to be stored by the function.
    //
    // boost::array<char,64>& signature
    // 	the signature authenticating the current value. This may be zeroes
    // 	if there is currently no value stored. The function is expected to
    // 	fill in this buffer with the signature of the new value to store.
    // 	To generate the signature, you may want to use the
    // 	``sign_mutable_item`` function.
    //
    // boost::uint64_t& seq
    // 	current sequence number. May be zero if there is no current value.
    // 	The function is expected to set this to the new sequence number of
    // 	the value that is to be stored. Sequence numbers must be monotonically
    // 	increasing. Attempting to overwrite a value with a lower or equal
    // 	sequence number will fail, even if the signature is correct.
    //
    // std::string const& salt
    // 	this is the salt that was used for this put call.
    //
    // Since the callback function ``cb`` is called from within libtorrent,
    // it is critical to not perform any blocking operations. Ideally not
    // even locking a mutex. Pass any data required for this function along
    // with the function object's context and make the function entirely
    // self-contained. The only reason data blobs' values are computed
    // via a function instead of just passing in the new value is to avoid
    // race conditions. If you want to *update* the value in the DHT, you
    // must first retrieve it, then modify it, then write it back. The way
    // the DHT works, it is natural to always do a lookup before storing and
    // calling the callback in between is convenient.
    public void dhtPutItem(byte[] publicKey, byte[] privateKey, Entry entry, byte[] salt) {
        s.dht_put_item(Vectors.bytes2byte_vector(publicKey),
                Vectors.bytes2byte_vector(privateKey),
                entry.swig(),
                Vectors.bytes2byte_vector(salt));
    }

    /**
     * @param infoHash
     */
    public void dhtGetPeers(Sha1Hash infoHash) {
        s.dht_get_peers(infoHash.swig());
    }

    /**
     * @param infoHash
     * @param port
     * @param flags
     */
    public void dhtAnnounce(Sha1Hash infoHash, int port, int flags) {
        s.dht_announce(infoHash.swig(), port, flags);
    }

    /**
     * @param infoHash
     */
    public void dhtAnnounce(Sha1Hash infoHash) {
        s.dht_announce(infoHash.swig());
    }

    /**
     * @param endp
     * @param entry
     * @param userdata
     */
    public void dhtDirectRequest(UdpEndpoint endp, Entry entry, long userdata) {
        s.dht_direct_request(endp.swig(), entry.swig(), userdata);
    }

    /**
     * @param endp
     * @param entry
     */
    public void dhtDirectRequest(UdpEndpoint endp, Entry entry) {
        s.dht_direct_request(endp.swig(), entry.swig());
    }

    /**
     * returns the port we ended up listening on. Since you
     * just pass a port-range to the constructor and to ``listen_on()``, to
     * know which port it ended up using, you have to ask the session using
     * this function.
     *
     * @return
     */
    public int getListenPort() {
        return s.listen_port();
    }

    public int getSslListenPort() {
        return s.ssl_listen_port();
    }

    /**
     * will tell you whether or not the session has
     * successfully opened a listening port. If it hasn't, this function will
     * return false, and then you can use ``listen_on()`` to make another
     * attempt.
     *
     * @return {@code true} if listening
     */
    public boolean isListening() {
        return s.is_listening();
    }

    /**
     *
     */
    public void addExtension(Plugin plugin) {
        SwigPlugin p = new SwigPlugin(plugin);
        s.add_extension(p);
        p.swigReleaseOwnership();
    }
}
