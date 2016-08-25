package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionHandle {

    private static final Logger LOG = Logger.getLogger(SessionHandle.class);

    protected final session_handle s;

    public SessionHandle(session_handle s) {
        this.s = s;
    }

    public session_handle swig() {
        return s;
    }

    public boolean isValid() {
        return s.is_valid();
    }

    /**
     * Loads and saves all session settings, including dht settings,
     * encryption settings and proxy settings. {@link #saveState(long)}
     * internally writes all keys to an {@link entry} that's passed in,
     * which needs to either not be initialized, or initialized as a dictionary.
     * <p>
     * The {@code flags} argument passed in to this method can be used to
     * filter which parts of the session state to save. By default, all state
     * is saved (except for the individual torrents).
     *
     * @return
     * @see {@link com.frostwire.jlibtorrent.swig.session_handle.save_state_flags_t}
     */
    public byte[] saveState(long flags) {
        entry e = new entry();
        s.save_state(e, flags);
        return Vectors.byte_vector2bytes(e.bencode());
    }

    /**
     * Same as calling {@link #saveState(long)} with all save state flags.
     *
     * @return
     * @see #saveState(long)
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
     * {@link #loadState(byte[], long)} expects a byte array that it is a
     * bencoded buffer.
     * <p>
     * The {@code flags} argument passed in to this method can be used to
     * filter which parts of the session state to load. By default, all state
     * is restored (except for the individual torrents).
     *
     * @param data
     * @see {@link com.frostwire.jlibtorrent.swig.session_handle.save_state_flags_t}
     */
    public void loadState(byte[] data, long flags) {
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
     * Same as calling {@link #loadState(byte[], long)} with all save state flags.
     *
     * @return
     * @see #loadState(byte[], long)
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
     * unless {@link com.frostwire.jlibtorrent.swig.add_torrent_params.flags_t#flag_duplicate_is_error}
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
     * @param th
     */
    public void removeTorrent(TorrentHandle th, Options options) {
        if (th.isValid()) {
            s.remove_torrent(th.swig(), options.swig());
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

    void setDHTSettings(DhtSettings settings) {
        s.set_dht_settings(settings.swig());
    }

    public boolean isDHTRunning() {
        return s.is_dht_running();
    }

    /**
     * Applies the settings specified by the settings pack {@code sp}. This is an
     * asynchronous operation that will return immediately and actually apply
     * the settings to the main thread of libtorrent some time later.
     *
     * @param sp
     */
    public void applySettings(SettingsPack sp) {
        s.apply_settings(sp.swig());
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
        return s.add_port_mapping(session_handle.protocol_type.swigToEnum(t.swig()), externalPort, localPort);
    }

    public void deletePortMapping(int handle) {
        s.delete_port_mapping(handle);
    }

    /**
     * Flags that determines which aspects of the session should be
     * saved when calling {@link #saveState(long)}
     */
    public enum SaveStateFlags {

        /**
         * Saves settings (i.e. the {@link SettingsPack}).
         */
        SAVE_SETTINGS(session_handle.save_state_flags_t.save_settings.swigValue()),

        /**
         * Saves {@link DhtSettings}.
         */
        SAVE_DHT_SETTINGS(session_handle.save_state_flags_t.save_dht_settings.swigValue()),

        /**
         * Saves dht state such as nodes and node-id, possibly accelerating
         * joining the DHT if provided at next session startup.
         */
        SAVE_DHT_STATE(session_handle.save_state_flags_t.save_dht_state.swigValue()),

        /**
         * Save pe_settings.
         */
        SAVE_ENCRYPTION_SETTINGS(session_handle.save_state_flags_t.save_encryption_settings.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        SaveStateFlags(int swigValue) {
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
        public static SaveStateFlags fromSwig(int swigValue) {
            SaveStateFlags[] enumValues = SaveStateFlags.class.getEnumConstants();
            for (SaveStateFlags ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Flags to be passed in to {@link #removeTorrent(TorrentHandle, Options)}.
     */
    public enum Options {

        /**
         * Delete the files belonging to the torrent from disk,
         * including the part-file, if there is one.
         */
        DELETE_FILES(session_handle.options_t.delete_files.swigValue()),

        /**
         * Delete just the part-file associated with this torrent.
         */
        DELETE_PARTFILE(session_handle.options_t.delete_partfile.swigValue());

        Options(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }
    }

    /**
     * protocols used by add_port_mapping().
     */
    public enum ProtocolType {

        /**
         *
         */
        UDP(session_handle.protocol_type.udp.swigValue()),

        /**
         *
         */
        TCP(session_handle.protocol_type.tcp.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        ProtocolType(int swigValue) {
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
        public static ProtocolType fromSwig(int swigValue) {
            ProtocolType[] enumValues = ProtocolType.class.getEnumConstants();
            for (ProtocolType ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
