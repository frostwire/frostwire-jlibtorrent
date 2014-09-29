package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.GenericAlert;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;
import com.frostwire.jlibtorrent.swig.*;
import com.frostwire.jlibtorrent.swig.session.options_t;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * The session holds all state that spans multiple torrents. Among other
 * things it runs the network loop and manages all torrents. Once it's
 * created, the session object will spawn the main thread that will do all
 * the work. The main thread will be idle as long it doesn't have any
 * torrents to participate in.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Session {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private static final Logger LOG = Logger.getLogger(Session.class);

    private static final long REQUEST_STATUS_RESOLUTION_MILLIS = 500;
    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;

    private static final Map<Integer, CastAlertFunction> CAST_TABLE = buildCastAlertTable();

    private final session s;

    private long lastStatusRequestTime;
    private SessionStatus lastStatus;

    private final List<AlertListener> listeners;
    private boolean running;

    public Session(fingerprint fingerprint) {

        this.s = new session();

        s.set_alert_mask(alert.category_t.all_categories.swigValue());

        int_int_pair pr = new int_int_pair(6881, 6981);
        error_code ec = new error_code();
        s.listen_on(pr, ec);

        s.add_dht_router(new string_int_pair("router.bittorrent.com", 6881));

        this.listeners = new CopyOnWriteArrayList<AlertListener>();
        this.running = true;

        alertsLoop();
    }

    public Session() {
        this(new fingerprint("FW", 0, 0, 1, 1));
    }

    public session getSwig() {
        return s;
    }

    public void addListener(AlertListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(AlertListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * You add torrents through the add_torrent() function where you give an object with all the parameters. The add_torrent() overloads will block until the torrent has been added (or failed to be added) and returns an error code and a torrent_handle. In order to add torrents more efficiently, consider using async_add_torrent() which returns immediately, without waiting for the torrent to add. Notification of the torrent being added is sent as add_torrent_alert.
     * <p/>
     * The overload that does not take an error_code throws an exception on error and is not available when building without exception support. The torrent_handle returned by add_torrent() can be used to retrieve information about the torrent's progress, its peers etc. It is also used to abort a torrent.
     * <p/>
     * If the torrent you are trying to add already exists in the session (is either queued for checking, being checked or downloading) add_torrent() will throw libtorrent_exception which derives from std::exception unless duplicate_is_error is set to false. In that case, add_torrent() will return the handle to the existing torrent.
     * <p/>
     * all torrent_handles must be destructed before the session is destructed!
     *
     * @param torrentFile
     * @param saveDir
     * @return
     */
    public TorrentHandle addTorrent(File torrentFile, byte[] priorities, File saveDir) {
        TorrentInfo ti = new TorrentInfo(torrentFile);

        add_torrent_params p = add_torrent_params.create_instance();

        p.setTi(ti.getSwig());
        p.setSave_path(saveDir.getAbsolutePath());
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        if (priorities != null) {
            p.setFile_priorities(Vectors.bytes2unsigned_char_vector(priorities));
        }

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        p.setFlags(flags);

        torrent_handle th = s.add_torrent(p);

        return new TorrentHandle(th);
    }

    public TorrentHandle addTorrent(File torrentFile, File saveDir) {
        return addTorrent(torrentFile, null, saveDir);
    }

    public void asyncAddTorrent(File torrentFile, Priority[] priorities, File saveDir, File resumeFile) throws IOException {
        String torrentPath = torrentFile.getAbsolutePath();
        String savePath = saveDir.getAbsolutePath();

        torrent_info ti = new torrent_info(torrentPath);

        add_torrent_params p = add_torrent_params.create_instance();

        p.setTi(ti);
        p.setSave_path(savePath);
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        if (priorities != null) {
            byte[] arr = new byte[priorities.length];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte) priorities[i].getSwig();
            }
            p.setFile_priorities(Vectors.bytes2unsigned_char_vector(arr));
        }

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        if (resumeFile != null && resumeFile.exists()) {
            flags |= add_torrent_params.flags_t.flag_use_resume_save_path.swigValue();
            byte[] data = Utils.readFileToByteArray(resumeFile);
            p.setResume_data(Vectors.bytes2char_vector(data));
        }

        p.setFlags(flags);

        s.async_add_torrent(p);
    }

    public void asyncAddTorrent(File torrentFile, File saveDir, File resumeFile) throws IOException {
        asyncAddTorrent(torrentFile, null, saveDir, resumeFile);
    }

    public void asyncAddTorrent(File torrentFile, File saveDir) throws IOException {
        asyncAddTorrent(torrentFile, null, saveDir, null);
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
        s.remove_torrent(th.getSwig(), options.getSwig().swigValue());
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
     * @param uri
     * @param timeout in milliseconds
     * @return
     */
    public byte[] fetchMagnet(String uri, long timeout) {

        add_torrent_params p = add_torrent_params.create_instance_no_storage();
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, p, ec);

        p.setName("fetchMagnet - " + uri);

        long flags = p.getFlags();
        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
        p.setFlags(flags);

        final torrent_handle th = s.add_torrent(p);

        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener l = new TorrentAlertAdapter(new TorrentHandle(th)) {
            @Override
            public void metadataReceived(MetadataReceivedAlert alert) {
                signal.countDown();
            }
        };

        addListener(l);

        th.resume();

        try {
            signal.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        removeListener(l);

        try {
            byte[] data = null;

            torrent_info ti = th.torrent_file();
            if (ti != null) {
                create_torrent ct = new create_torrent(ti);
                data = Vectors.char_vector2bytes(ct.generate().bencode());
            }

            return data;

        } finally {
            s.remove_torrent(th);
        }
    }

    public void pause() {
        s.pause();
    }

    public void resume() {
        s.resume();
    }

    public boolean isPaused() {
        return s.is_paused();
    }

    public boolean isListening() {
        return s.is_listening();
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
        return new SessionProxy(s.abort());
    }

    /**
     * Returns session wide-statistics and status.
     * <p/>
     * It is important not to call this method for each field in the status
     * for performance reasons.
     *
     * @return
     */
    public SessionStatus getStatus(boolean force) {
        long now = System.currentTimeMillis();
        if (force || (now - lastStatusRequestTime) >= REQUEST_STATUS_RESOLUTION_MILLIS) {
            lastStatusRequestTime = now;
            lastStatus = new SessionStatus(s.status());
        }

        return lastStatus;
    }

    /**
     * Returns session wide-statistics and status.
     *
     * @return
     */
    public SessionStatus getStatus() {
        return this.getStatus(false);
    }

    /**
     * The session settings and the packet encryption settings
     * respectively. See session_settings and pe_settings for more
     * information on available options.
     *
     * @return
     */
    public SessionSettings getSettings() {
        return new SessionSettings(s.settings());
    }

    /**
     * Sets the session settings and the packet encryption settings
     * respectively. See session_settings and pe_settings for more
     * information on available options.
     *
     * @param settings
     */
    public void setSettings(SessionSettings settings) {
        s.set_settings(settings.getSwig());
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
        lazy_entry e = new lazy_entry();
        error_code ec = new error_code();
        int ret = lazy_entry.bdecode(buffer, e, ec);

        if (ret == 0) {
            s.load_state(e);
        } else {
            LOG.error("failed to decode torrent: " + ec.message());
        }
    }

    /**
     * Looks for a torrent with the given info-hash. In
     * case there is such a torrent in the session, a torrent_handle to that
     * torrent is returned.
     *
     * @param infoHash
     * @return
     */
    public TorrentHandle findTorrent(Sha1Hash infoHash) {
        torrent_handle th = s.find_torrent(infoHash.getSwig());

        return th.is_valid() ? new TorrentHandle(th) : null;
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

    /**
     * Query the DHT for a mutable item under the public key ``key``.
     * this is an ed25519 key. ``salt`` is optional and may be left
     * as an empty string if no salt is to be used.
     * if the item is found in the DHT, a dht_mutable_item_alert is
     * posted.
     *
     * @param key
     */
    public void getDhtItem(byte[] key) {
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
    public void getDhtItem(byte[] key, String salt) {
        s.dht_get_item(Vectors.bytes2char_vector(key), salt);
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

    @Override
    protected void finalize() throws Throwable {
        this.running = false;
        super.finalize();
    }

    private void alertsLoop() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                time_duration max_wait = libtorrent.milliseconds(ALERTS_LOOP_WAIT_MILLIS);
                while (running) {
                    alert ptr = s.wait_for_alert(max_wait);

                    alert_ptr_deque deque = new alert_ptr_deque();
                    if (ptr != null) {
                        s.pop_alerts(deque);
                    }

                    for (int i = 0; i < deque.size(); i++) {
                        Alert<?> a = castAlert(deque.getitem(i));
                        synchronized (listeners) {
                            for (AlertListener l : listeners) {
                                try {
                                    if (l.accept(a)) {
                                        l.alert(a);
                                    }
                                } catch (Throwable e) {
                                    LOG.warn("Error calling alert listener", e);
                                }
                            }
                        }
                    }
                }
            }
        };

        Thread t = new Thread(r, "LTEngine-alertsLoop");
        t.setDaemon(true);
        t.start();
    }

    private static Map<Integer, CastAlertFunction> buildCastAlertTable() {
        Map<Integer, CastAlertFunction> map = new HashMap<Integer, CastAlertFunction>();

        CAST_ALERT_METHOD(torrent_alert.class, map);
        CAST_ALERT_METHOD(peer_alert.class, map);
        CAST_ALERT_METHOD(tracker_alert.class, map);
        CAST_ALERT_METHOD(torrent_added_alert.class, map);
        CAST_ALERT_METHOD(torrent_removed_alert.class, map);
        CAST_ALERT_METHOD(read_piece_alert.class, map);
        CAST_ALERT_METHOD(file_completed_alert.class, map);
        CAST_ALERT_METHOD(file_renamed_alert.class, map);
        CAST_ALERT_METHOD(file_rename_failed_alert.class, map);
        CAST_ALERT_METHOD(performance_alert.class, map);
        CAST_ALERT_METHOD(state_changed_alert.class, map);
        CAST_ALERT_METHOD(tracker_error_alert.class, map);
        CAST_ALERT_METHOD(tracker_warning_alert.class, map);
        CAST_ALERT_METHOD(scrape_reply_alert.class, map);
        CAST_ALERT_METHOD(scrape_failed_alert.class, map);
        CAST_ALERT_METHOD(tracker_reply_alert.class, map);
        CAST_ALERT_METHOD(dht_reply_alert.class, map);
        CAST_ALERT_METHOD(tracker_announce_alert.class, map);
        CAST_ALERT_METHOD(hash_failed_alert.class, map);
        CAST_ALERT_METHOD(peer_ban_alert.class, map);
        CAST_ALERT_METHOD(peer_unsnubbed_alert.class, map);
        CAST_ALERT_METHOD(peer_snubbed_alert.class, map);
        CAST_ALERT_METHOD(peer_error_alert.class, map);
        CAST_ALERT_METHOD(peer_connect_alert.class, map);
        CAST_ALERT_METHOD(peer_disconnected_alert.class, map);
        CAST_ALERT_METHOD(invalid_request_alert.class, map);
        CAST_ALERT_METHOD(torrent_finished_alert.class, map);
        CAST_ALERT_METHOD(piece_finished_alert.class, map);
        CAST_ALERT_METHOD(request_dropped_alert.class, map);
        CAST_ALERT_METHOD(block_timeout_alert.class, map);
        CAST_ALERT_METHOD(block_finished_alert.class, map);
        CAST_ALERT_METHOD(block_downloading_alert.class, map);
        CAST_ALERT_METHOD(unwanted_block_alert.class, map);
        CAST_ALERT_METHOD(storage_moved_alert.class, map);
        CAST_ALERT_METHOD(storage_moved_failed_alert.class, map);
        CAST_ALERT_METHOD(torrent_deleted_alert.class, map);
        CAST_ALERT_METHOD(torrent_delete_failed_alert.class, map);
        CAST_ALERT_METHOD(save_resume_data_alert.class, map);
        CAST_ALERT_METHOD(save_resume_data_failed_alert.class, map);
        CAST_ALERT_METHOD(torrent_paused_alert.class, map);
        CAST_ALERT_METHOD(torrent_resumed_alert.class, map);
        CAST_ALERT_METHOD(torrent_checked_alert.class, map);
        CAST_ALERT_METHOD(url_seed_alert.class, map);
        CAST_ALERT_METHOD(file_error_alert.class, map);
        CAST_ALERT_METHOD(metadata_failed_alert.class, map);
        CAST_ALERT_METHOD(metadata_received_alert.class, map);
        CAST_ALERT_METHOD(udp_error_alert.class, map);
        CAST_ALERT_METHOD(external_ip_alert.class, map);
        CAST_ALERT_METHOD(listen_failed_alert.class, map);
        CAST_ALERT_METHOD(listen_succeeded_alert.class, map);
        CAST_ALERT_METHOD(portmap_error_alert.class, map);
        CAST_ALERT_METHOD(portmap_alert.class, map);
        CAST_ALERT_METHOD(portmap_log_alert.class, map);
        CAST_ALERT_METHOD(fastresume_rejected_alert.class, map);
        CAST_ALERT_METHOD(peer_blocked_alert.class, map);
        CAST_ALERT_METHOD(dht_announce_alert.class, map);
        CAST_ALERT_METHOD(dht_get_peers_alert.class, map);
        CAST_ALERT_METHOD(stats_alert.class, map);
        CAST_ALERT_METHOD(cache_flushed_alert.class, map);
        CAST_ALERT_METHOD(anonymous_mode_alert.class, map);
        CAST_ALERT_METHOD(lsd_peer_alert.class, map);
        CAST_ALERT_METHOD(trackerid_alert.class, map);
        CAST_ALERT_METHOD(dht_bootstrap_alert.class, map);
        CAST_ALERT_METHOD(rss_alert.class, map);
        CAST_ALERT_METHOD(torrent_error_alert.class, map);
        CAST_ALERT_METHOD(torrent_need_cert_alert.class, map);
        CAST_ALERT_METHOD(incoming_connection_alert.class, map);
        CAST_ALERT_METHOD(add_torrent_alert.class, map);
        CAST_ALERT_METHOD(state_update_alert.class, map);
        CAST_ALERT_METHOD(torrent_update_alert.class, map);
        CAST_ALERT_METHOD(rss_item_alert.class, map);
        CAST_ALERT_METHOD(dht_error_alert.class, map);
        CAST_ALERT_METHOD(dht_immutable_item_alert.class, map);
        CAST_ALERT_METHOD(dht_mutable_item_alert.class, map);
        CAST_ALERT_METHOD(dht_put_alert.class, map);
        CAST_ALERT_METHOD(i2p_alert.class, map);

        return Collections.unmodifiableMap(map);
    }

    private static void CAST_ALERT_METHOD(Class<? extends alert> clazz, Map<Integer, CastAlertFunction> map) {
        try {
            Field f = clazz.getDeclaredField("alert_type");
            int type = f.getInt(null);
            CastAlertFunction function = new CastAlertFunction(clazz);

            map.put(type, function);
        } catch (Throwable e) {
            LOG.warn(e.toString());
        }
    }

    private Alert<?> castAlert(alert a) {
        CastAlertFunction function = CAST_TABLE.get(a.type());

        Alert<?> r;

        if (function != null) {
            r = function.cast(a);
        } else {
            r = new GenericAlert(a);
        }

        return r;
    }

    /**
     * Flags to be passed in to remove_torrent().
     */
    public enum Options {

        /**
         * Delete the files belonging to the torrent from disk.
         */
        DELETE_FILES(options_t.delete_files);

        private Options(options_t swigObj) {
            this.swigObj = swigObj;
        }

        private final options_t swigObj;

        public options_t getSwig() {
            return swigObj;
        }
    }

    private static final class CastAlertFunction {

        private final Method method;
        private final Constructor<? extends Alert<?>> constructor;

        public CastAlertFunction(Class<? extends alert> swigClazz) throws NoSuchMethodException, ClassNotFoundException {

            String swigClazzName = swigClazz.getName().replace("com.frostwire.jlibtorrent.swig.", "");
            String libClazzName = "com.frostwire.jlibtorrent.alerts." + capitalizeAlertTypeName(swigClazzName);

            @SuppressWarnings("unchecked")
            Class<? extends Alert<?>> libClazz = (Class<? extends Alert<?>>) Class.forName(libClazzName);

            this.method = alert.class.getDeclaredMethod("cast_to_" + swigClazzName, alert.class);
            this.constructor = libClazz.getDeclaredConstructor(swigClazz);
        }

        public Alert<?> cast(alert a) {
            Alert<?> r;

            try {
                Object obj = method.invoke(null, a);
                r = constructor.newInstance(obj);
            } catch (Throwable e) {
                LOG.warn(e.toString());
                r = new GenericAlert(a);
            }

            return r;
        }

        private static String capitalizeAlertTypeName(String s) {
            StringBuilder sb = new StringBuilder(s.length());

            boolean capitalize = true;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (capitalize) {
                    sb.append(Character.toUpperCase(ch));
                    capitalize = false;
                } else if (ch == '_') {
                    capitalize = true;
                } else {
                    sb.append(ch);
                }
            }

            return sb.toString();
        }
    }
}
