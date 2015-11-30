package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.plugins.Plugin;
import com.frostwire.jlibtorrent.plugins.SwigPlugin;
import com.frostwire.jlibtorrent.swig.*;
import com.frostwire.jlibtorrent.swig.session_handle.options_t;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The session holds all state that spans multiple torrents. Among other
 * things it runs the network loop and manages all torrents. Once it's
 * created, the session object will spawn the main thread that will do all
 * the work. The main thread will be idle as long it doesn't have any
 * torrents to participate in.
 * <p/>
 * This class belongs to a middle logical layer of abstraction. It's a wrapper
 * of the underlying swig session object (from libtorrent), but it does not
 * expose all the raw features, not expose a very high level interface
 * like {@link com.frostwire.jlibtorrent.DHT DHT} or
 * {@link com.frostwire.jlibtorrent.Downloader Downloader}.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Session extends SessionHandle {

    private static final Logger LOG = Logger.getLogger(Session.class);

    private static final long REQUEST_STATS_RESOLUTION_MILLIS = 1000;
    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;

    private final session s;
    private final JavaStat stat;
    private final SessionStats stats;

    private long lastStatsRequestTime;
    private long lastStatSecondTick;

    private final SparseArray<ArrayList<AlertListener>> listeners;
    private final SparseArray<AlertListener[]> listenerSnapshots;
    private boolean running;

    private final LinkedList<SwigPlugin> plugins;

    /**
     * The flag alert_mask is always set to all_categories.
     *
     * @param settings
     * @param logging
     */
    public Session(SettingsPack settings, boolean logging) {
        super(createSession(settings, logging));

        this.s = (session) super.s;

        this.stat = new JavaStat();
        this.stats = new SessionStats(stat);

        this.listeners = new SparseArray<ArrayList<AlertListener>>();
        this.listenerSnapshots = new SparseArray<AlertListener[]>();
        this.running = true;

        alertsLoop();

        for (Pair<String, Integer> router : defaultRouters()) {
            s.add_dht_router(router.to_string_int_pair());
        }

        this.plugins = new LinkedList<SwigPlugin>();
    }

    @Deprecated
    public Session(Fingerprint print, Pair<Integer, Integer> prange, String iface, List<Pair<String, Integer>> routers, boolean logging) {
        super(createSessionDeprecated(print, prange, iface, routers, logging));

        this.s = (session) super.s;

        this.stat = new JavaStat();
        this.stats = new SessionStats(stat);

        this.listeners = new SparseArray<ArrayList<AlertListener>>();
        this.listenerSnapshots = new SparseArray<AlertListener[]>();
        this.running = true;

        alertsLoop();

        for (Pair<String, Integer> router : routers) {
            s.add_dht_router(router.to_string_int_pair());
        }

        this.plugins = new LinkedList<SwigPlugin>();
    }

    public Session(Fingerprint print, Pair<Integer, Integer> prange, String iface, List<Pair<String, Integer>> routers) {
        this(print, prange, iface, routers, false);
    }

    public Session(Fingerprint print, Pair<Integer, Integer> prange, String iface) {
        this(print, prange, iface, defaultRouters());
    }

    public Session(Fingerprint print) {
        this(print, new Pair<Integer, Integer>(0, 0), "0.0.0.0");
    }

    public Session(Pair<Integer, Integer> prange, String iface) {
        this(new Fingerprint(), prange, iface);
    }

    public Session() {
        this(new SettingsPack(), false);
    }

    public session getSwig() {
        return s;
    }

    public void addListener(AlertListener listener) {
        modifyListeners(true, listener);
    }

    public void removeListener(AlertListener listener) {
        modifyListeners(false, listener);
    }

    /**
     * You add torrents through the add_torrent() function where you give an
     * object with all the parameters. The add_torrent() overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a torrent_handle. In order to add torrents more
     * efficiently, consider using async_add_torrent() which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on
     * error and is not available when building without exception support.
     * The torrent_handle returned by add_torrent() can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param ti
     * @param saveDir
     * @param priorities
     * @param resumeFile
     * @return
     */
    public TorrentHandle addTorrent(TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile) {
        return addTorrentSupport(ti, saveDir, priorities, resumeFile, false);
    }

    /**
     * You add torrents through the add_torrent() function where you give an
     * object with all the parameters. The add_torrent() overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a torrent_handle. In order to add torrents more
     * efficiently, consider using async_add_torrent() which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on
     * error and is not available when building without exception support.
     * The torrent_handle returned by add_torrent() can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrent
     * @param saveDir
     * @param resumeFile
     * @return
     */
    public TorrentHandle addTorrent(File torrent, File saveDir, File resumeFile) {
        return addTorrent(new TorrentInfo(torrent), saveDir, null, resumeFile);
    }

    /**
     * You add torrents through the add_torrent() function where you give an
     * object with all the parameters. The add_torrent() overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a torrent_handle. In order to add torrents more
     * efficiently, consider using async_add_torrent() which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on
     * error and is not available when building without exception support.
     * The torrent_handle returned by add_torrent() can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrent
     * @param saveDir
     * @return
     */
    public TorrentHandle addTorrent(File torrent, File saveDir) {
        return addTorrent(torrent, saveDir, null);
    }

    /**
     * In order to add torrents more efficiently, consider using this which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as {@link com.frostwire.jlibtorrent.alerts.AddTorrentAlert}.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     *
     * @param ti
     * @param saveDir
     * @param priorities
     * @param resumeFile
     */
    public void asyncAddTorrent(TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile) {
        addTorrentSupport(ti, saveDir, priorities, resumeFile, true);
    }

    /**
     * You add torrents through the add_torrent() function where you give an
     * object with all the parameters. The add_torrent() overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a torrent_handle. In order to add torrents more
     * efficiently, consider using async_add_torrent() which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on
     * error and is not available when building without exception support.
     * The torrent_handle returned by add_torrent() can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrent
     * @param saveDir
     * @param resumeFile
     */
    public void asyncAddTorrent(File torrent, File saveDir, File resumeFile) {
        asyncAddTorrent(new TorrentInfo(torrent), saveDir, null, resumeFile);
    }

    /**
     * You add torrents through the add_torrent() function where you give an
     * object with all the parameters. The add_torrent() overloads will block
     * until the torrent has been added (or failed to be added) and returns
     * an error code and a torrent_handle. In order to add torrents more
     * efficiently, consider using async_add_torrent() which returns
     * immediately, without waiting for the torrent to add. Notification of
     * the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on
     * error and is not available when building without exception support.
     * The torrent_handle returned by add_torrent() can be used to retrieve
     * information about the torrent's progress, its peers etc. It is also
     * used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is
     * either queued for checking, being checked or downloading)
     * ``add_torrent()`` will throw libtorrent_exception which derives from
     * ``std::exception`` unless duplicate_is_error is set to false. In that
     * case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrent
     * @param saveDir
     */
    public void asyncAddTorrent(File torrent, File saveDir) {
        asyncAddTorrent(torrent, saveDir, null);
    }

    /**
     * This method will close all peer connections associated with the torrent and tell the
     * tracker that we've stopped participating in the swarm. This operation cannot fail.
     * When it completes, you will receive a torrent_removed_alert.
     * <p/>
     * The optional second argument options can be used to delete all the files downloaded
     * by this torrent. To do so, pass in the value session::delete_files. The removal of
     * the torrent is asyncronous, there is no guarantee that adding the same torrent immediately
     * after it was removed will not throw a libtorrent_exception exception. Once the torrent
     * is deleted, a torrent_deleted_alert is posted.
     *
     * @param th
     */
    public void removeTorrent(TorrentHandle th, Options options) {
        s.remove_torrent(th.getSwig(), options.getSwig());
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
            s.remove_torrent(th.getSwig());
        }
    }

    /**
     * Applies the settings specified by the settings_pack ``sp``. This is an
     * asynchronous operation that will return immediately and actually apply
     * the settings to the main thread of libtorrent some time later.
     *
     * @param sp
     */
    public void applySettings(SettingsPack sp) {
        s.apply_settings(sp.getSwig());
    }

    /**
     * In case you want to destruct the session asynchrounously, you can
     * request a session destruction proxy. If you don't do this, the
     * destructor of the session object will block while the trackers are
     * contacted. If you keep one ``session_proxy`` to the session when
     * destructing it, the destructor will not block, but start to close down
     * the session, the destructor of the proxy will then synchronize the
     * threads. So, the destruction of the session is performed from the
     * ``session`` destructor call until the ``session_proxy`` destructor
     * call. The ``session_proxy`` does not have any operations on it (since
     * the session is being closed down, no operations are allowed on it).
     * The only valid operation is calling the destructor::
     *
     * @return
     */
    public SessionProxy abort() {
        running = false;
        return new SessionProxy(s.abort());
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
     * Loads and saves all session settings, including dht_settings,
     * encryption settings and proxy settings. ``save_state`` writes all keys
     * to the ``entry`` that's passed in, which needs to either not be
     * initialized, or initialized as a dictionary.
     * <p/>
     * ``load_state`` expects a lazy_entry which can be built from a bencoded
     * buffer with lazy_bdecode().
     * <p/>
     * The ``flags`` arguments passed in to ``save_state`` can be used to
     * filter which parts of the session state to save. By default, all state
     * is saved (except for the individual torrents). see save_state_flags_t
     *
     * @return
     */
    public byte[] saveState() {
        entry e = new entry();
        s.save_state(e);
        return Vectors.char_vector2bytes(e.bencode());
    }

    /**
     * Loads and saves all session settings, including dht_settings,
     * encryption settings and proxy settings. ``save_state`` writes all keys
     * to the ``entry`` that's passed in, which needs to either not be
     * initialized, or initialized as a dictionary.
     * <p/>
     * ``load_state`` expects a lazy_entry which can be built from a bencoded
     * buffer with lazy_bdecode().
     * <p/>
     * The ``flags`` arguments passed in to ``save_state`` can be used to
     * filter which parts of the session state to save. By default, all state
     * is saved (except for the individual torrents). see save_state_flags_t
     *
     * @param data
     */
    public void loadState(byte[] data) {
        char_vector buffer = Vectors.bytes2char_vector(data);
        bdecode_node n = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, n, ec);

        if (ret == 0) {
            s.load_state(n);
        } else {
            LOG.error("failed to decode torrent: " + ec.message());
        }
    }

    /**
     * This functions instructs the session to post the state_update_alert,
     * containing the status of all torrents whose state changed since the
     * last time this function was called.
     * <p/>
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
     * <p/>
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
     * Looks for a torrent with the given info-hash. In
     * case there is such a torrent in the session, a torrent_handle to that
     * torrent is returned.
     * <p/>
     * In case the torrent cannot be found, a null is returned.
     *
     * @param infoHash
     * @return
     */
    public TorrentHandle findTorrent(Sha1Hash infoHash) {
        torrent_handle th = s.find_torrent(infoHash.getSwig());

        return th != null && th.is_valid() ? new TorrentHandle(th) : null;
    }

    /**
     * Returns a list of torrent handles to all the
     * torrents currently in the session.
     *
     * @return
     */
    public List<TorrentHandle> getTorrents() {
        torrent_handle_vector v = s.get_torrents();
        long size = v.size();

        List<TorrentHandle> l = new ArrayList<TorrentHandle>((int) size);

        for (int i = 0; i < size; i++) {
            l.add(new TorrentHandle(v.get(i)));
        }

        return l;
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
    // ``set_dht_settings`` sets some parameters availavle to the dht node.
    // See dht_settings for more information.
    //
    // ``is_dht_running()`` returns true if the DHT support has been started
    // and false
    // otherwise.

    void setDHTSettings(DHTSettings settings) {
        s.set_dht_settings(settings.getSwig());
    }

    public boolean isDHTRunning() {
        return s.is_dht_running();
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
     * <p/>
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
        s.dht_get_item(target.getSwig());
    }

    /**
     * Query the DHT for a mutable item under the public key ``key``.
     * this is an ed25519 key. ``salt`` is optional and may be left
     * as an empty string if no salt is to be used.
     * if the item is found in the DHT, a dht_mutable_item_alert is
     * posted.
     *
     * @param key
     */
    public void dhtGetItem(byte[] key) {
        s.dht_get_item(Vectors.bytes2char_vector(key));
    }

    /**
     * Query the DHT for a mutable item under the public key ``key``.
     * this is an ed25519 key. ``salt`` is optional and may be left
     * as an empty string if no salt is to be used.
     * if the item is found in the DHT, a dht_mutable_item_alert is
     * posted.
     *
     * @param key
     * @param salt
     */
    public void dhtGetItem(byte[] key, String salt) {
        s.dht_get_item(Vectors.bytes2char_vector(key), salt);
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
        return new Sha1Hash(s.dht_put_item(entry.getSwig()));
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
    public void dhtPutItem(byte[] publicKey, byte[] privateKey, Entry entry) {
        s.dht_put_item(Vectors.bytes2char_vector(publicKey), Vectors.bytes2char_vector(privateKey), entry.getSwig());
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
    public void dhtPutItem(byte[] publicKey, byte[] privateKey, Entry entry, String salt) {
        s.dht_put_item(Vectors.bytes2char_vector(publicKey), Vectors.bytes2char_vector(privateKey), entry.getSwig(), salt);
    }

    public void dhtGetPeers(Sha1Hash infoHash) {
        s.dht_get_peers(infoHash.getSwig());
    }

    public void dhtAnnounce(Sha1Hash infoHash, int port, int flags) {
        s.dht_announce(infoHash.getSwig(), port, flags);
    }

    public void dhtAnnounce(Sha1Hash infoHash) {
        s.dht_announce(infoHash.getSwig());
    }

    public void dhtDirectRequest(UdpEndpoint endp, Entry entry) {
        s.dht_direct_request(endp.getSwig(), entry.getSwig());
    }

    public void addExtension(Plugin p) {
        SwigPlugin sp = new SwigPlugin(p);
        s.add_swig_extension(sp);
        plugins.add(sp);
    }

    /**
     * add_port_mapping adds a port forwarding on UPnP and/or NAT-PMP,
     * whichever is enabled. The return value is a handle referring to the
     * port mapping that was just created. Pass it to delete_port_mapping()
     * to remove it.
     *
     * @param t
     * @param externalPort
     * @param localPort
     * @return
     */
    public int addPortMapping(ProtocolType t, int externalPort, int localPort) {
        return s.add_port_mapping(t.getSwig(), externalPort, localPort);
    }

    public void deletePortMapping(int handle) {
        s.delete_port_mapping(handle);
    }

    public SessionStats getStats() {
        return stats;
    }

    @Deprecated
    public SessionSettings getSettings() {
        return new SessionSettings(s.get_settings());
    }

    @Deprecated
    public ProxySettings getProxy() {
        return new ProxySettings(new settings_pack());
    }

    @Deprecated
    public void setProxy(ProxySettings s) {
        this.s.apply_settings(s.getSwig());
    }

    @Deprecated
    public void setSettings(SessionSettings s) {
        this.applySettings(s.toPack());
    }

    @Deprecated
    public SessionStatus getStatus() {
        return new SessionStatus(stats);
    }

    // You add torrents through the add_torrent() function where you give an
    // object with all the parameters. The add_torrent() overloads will block
    // until the torrent has been added (or failed to be added) and returns
    // an error code and a torrent_handle. In order to add torrents more
    // efficiently, consider using async_add_torrent() which returns
    // immediately, without waiting for the torrent to add. Notification of
    // the torrent being added is sent as add_torrent_alert.
    //
    // The overload that does not take an error_code throws an exception on
    // error and is not available when building without exception support.
    // The torrent_handle returned by add_torrent() can be used to retrieve
    // information about the torrent's progress, its peers etc. It is also
    // used to abort a torrent.
    //
    // If the torrent you are trying to add already exists in the session (is
    // either queued for checking, being checked or downloading)
    // ``add_torrent()`` will throw libtorrent_exception which derives from
    // ``std::exception`` unless duplicate_is_error is set to false. In that
    // case, add_torrent() will return the handle to the existing torrent.
    //
    // all torrent_handles must be destructed before the session is destructed!
    public TorrentHandle addTorrent(AddTorrentParams params) {
        return new TorrentHandle(s.add_torrent(params.getSwig()));
    }

    public TorrentHandle addTorrent(AddTorrentParams params, ErrorCode ec) {
        return new TorrentHandle(s.add_torrent(params.getSwig(), ec.getSwig()));
    }

    public void asyncAddTorrent(AddTorrentParams params) {
        s.async_add_torrent(params.getSwig());
    }

    @Override
    protected void finalize() throws Throwable {
        this.running = false;
        super.finalize();
    }

    void fireAlert(Alert<?> a) {
        int type = a.getSwig() != null ? a.getSwig().type() : a.getType().getSwig();
        fireAlert(a, type);
        fireAlert(a, -1);
    }

    private void fireAlert(Alert<?> a, int type) {
        AlertListener[] listeners = listenerSnapshots.get(type);
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                try {
                    listeners[i].alert(a);
                } catch (Throwable e) {
                    LOG.warn("Error calling alert listener", e);
                }
            }
        }
    }

    private TorrentHandle addTorrentSupport(TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile, boolean async) {

        String savePath = null;
        if (saveDir != null) {
            savePath = saveDir.getAbsolutePath();
        } else if (resumeFile == null) {
            throw new IllegalArgumentException("Both saveDir and resumeFile can't be null at the same time");
        }

        add_torrent_params p = add_torrent_params.create_instance();

        p.set_ti(ti.getSwig());
        if (savePath != null) {
            p.setSave_path(savePath);
        }

        if (priorities != null) {
            p.setFile_priorities(Vectors.priorities2unsigned_char_vector(priorities));
        }
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        if (resumeFile != null) {
            try {
                byte[] data = Files.bytes(resumeFile);
                p.setResume_data(Vectors.bytes2char_vector(data));

                flags |= add_torrent_params.flags_t.flag_use_resume_save_path.swigValue();
            } catch (Throwable e) {
                LOG.warn("Unable to set resume data", e);
            }
        }

        p.setFlags(flags);

        if (async) {
            s.async_add_torrent(p);
            return null;
        } else {
            torrent_handle th = s.add_torrent(p);
            return new TorrentHandle(th);
        }
    }

    private void alertsLoop() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                alert_ptr_vector vector = new alert_ptr_vector();

                high_resolution_clock.duration max_wait = libtorrent.to_milliseconds(ALERTS_LOOP_WAIT_MILLIS);

                while (running) {
                    alert ptr = s.wait_for_alert(max_wait);

                    if (ptr != null) {
                        s.pop_alerts(vector);
                        long size = vector.size();
                        for (int i = 0; i < size; i++) {
                            alert swigAlert = vector.get(i);
                            int type = swigAlert.type();

                            Alert<?> alert = null;

                            if (type == AlertType.SESSION_STATS.getSwig()) {
                                alert = Alerts.cast(swigAlert);
                                updateSessionStat((SessionStatsAlert) alert);
                            }

                            if (listeners.indexOfKey(type) >= 0) {
                                if (alert == null) {
                                    alert = Alerts.cast(swigAlert);
                                }
                                fireAlert(alert, type);
                            }

                            if (type != AlertType.SESSION_STATS.getSwig() &&
                                    listeners.indexOfKey(-1) >= 0) {
                                if (alert == null) {
                                    alert = Alerts.cast(swigAlert);
                                }
                                fireAlert(alert, -1);
                            }
                        }
                        vector.clear();
                    }

                    long now = System.currentTimeMillis();
                    if ((now - lastStatsRequestTime) >= REQUEST_STATS_RESOLUTION_MILLIS) {
                        lastStatsRequestTime = now;
                        postSessionStats();
                    }
                }
            }
        };

        Thread t = new Thread(r, "Session-alertsLoop");
        t.setDaemon(true);
        t.start();
    }

    private void modifyListeners(boolean adding, AlertListener listener) {
        if (listener != null) {
            int[] types = listener.types();

            //all alert-type including listener
            if (types == null) {
                modifyListeners(adding, -1, listener);
            } else {
                for (int i = 0; i < types.length; i++) {
                    if (types[i] == -1) {
                        throw new IllegalArgumentException("Type can't be the key of all (-1)");
                    }
                    modifyListeners(adding, types[i], listener);
                }
            }
        }
    }

    private void modifyListeners(boolean adding, int type, AlertListener listener) {
        ArrayList<AlertListener> l = listeners.get(type);
        if (l == null) {
            l = new ArrayList<AlertListener>();
            listeners.append(type, l);
        }

        if (adding) {
            l.add(listener);
        } else {
            l.remove(listener);
        }

        listenerSnapshots.append(type, l.toArray(new AlertListener[0]));
    }

    private static List<Pair<String, Integer>> defaultRouters() {
        List<Pair<String, Integer>> list = new LinkedList<Pair<String, Integer>>();

        list.add(new Pair<String, Integer>("router.bittorrent.com", 6881));
        list.add(new Pair<String, Integer>("dht.transmissionbt.com", 6881));

        return list;
    }

    private void updateSessionStat(SessionStatsAlert alert) {
        long now = System.currentTimeMillis();
        long tickIntervalMs = now - lastStatSecondTick;
        lastStatSecondTick = now;
        long received = alert.value(counters.stats_counter_t.recv_bytes.swigValue());
        long payload = alert.value(counters.stats_counter_t.recv_payload_bytes.swigValue());
        long protocol = received - payload;
        long ip = alert.value(counters.stats_counter_t.recv_ip_overhead_bytes.swigValue());

        payload -= stat.downloadPayload();
        protocol -= stat.downloadProtocol();
        ip -= stat.downloadIPProtocol();
        stat.received(payload, protocol, ip);

        long sent = alert.value(counters.stats_counter_t.sent_bytes.swigValue());
        payload = alert.value(counters.stats_counter_t.sent_payload_bytes.swigValue());
        protocol = sent - payload;
        ip = alert.value(counters.stats_counter_t.sent_ip_overhead_bytes.swigValue());

        payload -= stat.uploadPayload();
        protocol -= stat.uploadProtocol();
        ip -= stat.uploadIPProtocol();
        stat.sent(payload, protocol, ip);

        stat.secondTick(tickIntervalMs);
    }

    private static session createSession(SettingsPack settings, boolean logging) {
        settings_pack sp = settings.getSwig();

        int alert_mask = alert.category_t.all_categories.swigValue();
        if (!logging) {
            int log_mask = alert.category_t.session_log_notification.swigValue() |
                    alert.category_t.torrent_log_notification.swigValue() |
                    alert.category_t.peer_log_notification.swigValue() |
                    alert.category_t.dht_log_notification.swigValue() |
                    alert.category_t.port_mapping_log_notification.swigValue();
            alert_mask = alert_mask & ~log_mask;
        }

        // we always override alert_mask since we use it for our internal operations
        sp.set_int(settings_pack.int_types.alert_mask.swigValue(), alert_mask);

        return new session(sp);
    }

    private static session createSessionDeprecated(Fingerprint print, Pair<Integer, Integer> prange, String iface, List<Pair<String, Integer>> routers, boolean logging) {
        int alert_mask = alert.category_t.all_categories.swigValue();
        if (!logging) {
            int log_mask = alert.category_t.session_log_notification.swigValue() |
                    alert.category_t.torrent_log_notification.swigValue() |
                    alert.category_t.peer_log_notification.swigValue() |
                    alert.category_t.dht_log_notification.swigValue() |
                    alert.category_t.port_mapping_log_notification.swigValue();
            alert_mask = alert_mask & ~log_mask;
        }

        settings_pack sp = new settings_pack();

        sp.set_int(settings_pack.int_types.alert_mask.swigValue(), alert_mask);
        sp.set_int(settings_pack.int_types.max_retry_port_bind.swigValue(), prange.second - prange.first);
        sp.set_str(settings_pack.string_types.peer_fingerprint.swigValue(), print.toString());

        String if_string = String.format("%s:%d", iface, prange.first);
        sp.set_str(settings_pack.string_types.listen_interfaces.swigValue(), if_string);

        return new session(sp);
    }

    /**
     * Flags to be passed in to remove_torrent().
     */
    public enum Options {

        /**
         * Delete the files belonging to the torrent from disk.
         */
        DELETE_FILES(options_t.delete_files.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Options(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }
    }

    /**
     * protocols used by add_port_mapping().
     */
    public enum ProtocolType {

        UDP(session.protocol_type.udp),

        TCP(session.protocol_type.tcp);

        ProtocolType(session.protocol_type swigObj) {
            this.swigObj = swigObj;
        }

        private final session.protocol_type swigObj;

        public session.protocol_type getSwig() {
            return swigObj;
        }
    }
}
