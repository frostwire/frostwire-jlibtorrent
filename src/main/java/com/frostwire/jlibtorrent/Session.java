package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.alerts.DhtMutableItemAlert;
import com.frostwire.jlibtorrent.swig.session;

/**
 * The session holds all state that spans multiple torrents. Among other
 * things it runs the network loop and manages all torrents. Once it's
 * created, the session object will spawn the main thread that will do all
 * the work. The main thread will be idle as long it doesn't have any
 * torrents to participate in.
 * <p>
 * This class belongs to a middle logical layer of abstraction. It's a wrapper
 * of the underlying swig session object (from libtorrent), but it does not
 * expose all the raw features, not expose a very high level interface
 * like {@link com.frostwire.jlibtorrent.Dht} or
 * {@link com.frostwire.jlibtorrent.Downloader}.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Session extends SessionHandle {

    private final session s;

    public Session(session s) {
        super(s);
        this.s = s;
    }

    /**
     * @return
     */
    public session swig() {
        return s;
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
     * @return
     */
    public boolean isListening() {
        return s.is_listening();
    }

    /**
     * This functions instructs the session to post the state_update_alert,
     * containing the status of all torrents whose state changed since the
     * last time this function was called.
     * <p>
     * Only torrents who has the state subscription flag set will be
     * included. This flag is on by default. See add_torrent_params.
     * the ``flags`` argument is the same as for torrent_handle::status().
     * see torrent_handle::status_flags_t.
     *
     * @param flags
     */
    public void postTorrentUpdates(TorrentHandle.StatusFlags flags) {
        s.post_torrent_updates(flags.getSwig());
    }

    /**
     * This functions instructs the session to post the state_update_alert,
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
    public void postDHTStats() {
        s.post_dht_stats();
    }

    /**
     * takes a host name and port pair. That endpoint will be
     * pinged, and if a valid DHT reply is received, the node will be added to
     * the routing table.
     *
     * @param node
     */
    public void addDHTNode(Pair<String, Integer> node) {
        s.add_dht_node(node.to_string_int_pair());
    }

    /**
     * adds the given endpoint to a list of DHT router nodes.
     * If a search is ever made while the routing table is empty, those nodes will
     * be used as backups. Nodes in the router node list will also never be added
     * to the regular routing table, which effectively means they are only used
     * for bootstrapping, to keep the load off them.
     * <p>
     * An example routing node that you could typically add is
     * ``router.bittorrent.com``.
     *
     * @param node
     */
    public void addDHTRouter(Pair<String, Integer> node) {
        s.add_dht_router(node.to_string_int_pair());
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
     * up agan. It's just the sha-1 hash of the bencoded form of the
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
    // 	if there is currently no value stored. The functon is expected to
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

    public void dhtGetPeers(Sha1Hash infoHash) {
        s.dht_get_peers(infoHash.swig());
    }

    public void dhtAnnounce(Sha1Hash infoHash, int port, int flags) {
        s.dht_announce(infoHash.swig(), port, flags);
    }

    public void dhtAnnounce(Sha1Hash infoHash) {
        s.dht_announce(infoHash.swig());
    }

    public void dhtDirectRequest(UdpEndpoint endp, Entry entry) {
        s.dht_direct_request(endp.swig(), entry.swig());
    }

    public SettingsPack getSettingsPack() {
        return new SettingsPack(s.get_settings());
    }
}
