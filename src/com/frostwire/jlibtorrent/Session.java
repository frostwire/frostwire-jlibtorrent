/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.GenericAlert;
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
 * @author gubatron
 * @author aldenml
 */
// The session holds all state that spans multiple torrents. Among other
// things it runs the network loop and manages all torrents. Once it's
// created, the session object will spawn the main thread that will do all
// the work. The main thread will be idle as long it doesn't have any
// torrents to participate in.
public final class Session {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private static final Logger LOG = Logger.getLogger(Session.class);

    private static final long ALERTS_LOOP_WAIT_MILLIS = 500;

    private static final Map<Integer, CastAlertFunction> CAST_TABLE = buildAlertCastTable();

    private final session s;

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
            p.setFile_priorities(LibTorrent.bytes2unsigned_char_vector(priorities));
        }

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        p.setFlags(flags);

        torrent_handle th = s.add_torrent(p);

        return new TorrentHandle(th);
    }

    public void asyncAddTorrent(File torrentFile, File saveDir, File resumeFile) throws IOException {
        String torrentPath = torrentFile.getAbsolutePath();
        String savePath = saveDir.getAbsolutePath();

        torrent_info ti = new torrent_info(torrentPath);

        add_torrent_params p = add_torrent_params.create_instance();

        p.setTi(ti);
        p.setSave_path(savePath);
        p.setStorage_mode(storage_mode_t.storage_mode_sparse);

        long flags = p.getFlags();

        flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();

        if (resumeFile != null && resumeFile.exists()) {
            flags |= add_torrent_params.flags_t.flag_use_resume_save_path.swigValue();
            byte[] data = Utils.readFileToByteArray(resumeFile);
            p.setResume_data(LibTorrent.bytes2char_vector(data));
        }

        p.setFlags(flags);

        s.async_add_torrent(p);
    }

    public TorrentHandle addTorrent(File torrentFile, File saveDir) {
        return addTorrent(torrentFile, null, saveDir);
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
        s.remove_torrent(th.getSwig());
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

        final torrent_handle th = s.add_torrent(p);
        th.auto_managed(false);

        final CountDownLatch signal = new CountDownLatch(1);

        AlertListener l = new AlertListener() {
            @Override
            public boolean accept(Alert<?> alert) {
                if (!(alert.getSwig() instanceof metadata_received_alert)) {
                    return false;
                }

                metadata_received_alert mra = (metadata_received_alert) alert.getSwig();

                return mra.getHandle().op_eq(th);
            }

            @Override
            public void onAlert(Alert<?> alert) {
                // we are here only if we received the corresponding metadata_received_alert
                signal.countDown();
            }
        };

        addListener(l);

        try {
            signal.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        removeListener(l);

        byte[] data = null;

        torrent_info ti = th.torrent_file();
        if (ti != null) {
            create_torrent ct = new create_torrent(ti);
            data = LibTorrent.char_vector2bytes(ct.generate().bencode());
        }

        s.remove_torrent(th);

        return data;
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

    // In case you want to destruct the session asynchrounously, you can
    // request a session destruction proxy. If you don't do this, the
    // destructor of the session object will block while the trackers are
    // contacted. If you keep one ``session_proxy`` to the session when
    // destructing it, the destructor will not block, but start to close down
    // the session, the destructor of the proxy will then synchronize the
    // threads. So, the destruction of the session is performed from the
    // ``session`` destructor call until the ``session_proxy`` destructor
    // call. The ``session_proxy`` does not have any operations on it (since
    // the session is being closed down, no operations are allowed on it).
    // The only valid operation is calling the destructor::
    //
    // 	class session_proxy
    // 	{
    // 	public:
    // 		session_proxy();
    // 		~session_proxy()
    // 	};
    public SessionProxy abort() {
        return new SessionProxy(s.abort());
    }

    // returns session wide-statistics and status. For more information, see
    // the ``session_status`` struct.
    public SessionStatus getStatus() {
        return new SessionStatus(s.status());
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

                    List<Alert<?>> alerts = new ArrayList<Alert<?>>((int) deque.size());

                    for (int i = 0; i < deque.size(); i++) {
                        Alert<?> a = castAlert(deque.getitem(i));
                        synchronized (listeners) {
                            for (AlertListener l : listeners) {
                                try {
                                    if (l.accept(a)) {
                                        l.onAlert(a);
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

    private static Map<Integer, CastAlertFunction> buildAlertCastTable() {
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

    public enum Options {
        DELETE_FILES(options_t.delete_files);

        private Options(options_t swigObj) {
            this.swigObj = swigObj;
        }

        private final options_t swigObj;

        public options_t getSwig() {
            return swigObj;
        }
    }

    private static class CastAlertFunction {

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
