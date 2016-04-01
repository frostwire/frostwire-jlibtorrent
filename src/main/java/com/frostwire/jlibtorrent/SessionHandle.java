package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.plugins.DhtStorageConstructor;
import com.frostwire.jlibtorrent.plugins.SwigDhtStorageConstructor;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionHandle {

    private static final Logger LOG = Logger.getLogger(SessionHandle.class);

    protected final session_handle s;

    private SwigDhtStorageConstructor dhtSC;

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
     * <p/>
     * The {@code flags} argument passed in to this method can be used to
     * filter which parts of the session state to save. By default, all state
     * is saved (except for the individual torrents).
     *
     * @return
     * @see {@link com.frostwire.jlibtorrent.swig.session_handle.save_state_flags_t}
     */
    public byte[] saveState(long flags) {
        entry e = new entry();
        s.save_state(e);
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
     * <p/>
     * {@link #loadState(byte[], long)} expects a byte array that it is a
     * bencoded buffer.
     * <p/>
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
        } else {
            LOG.error("failed to decode bencoded data: " + ec.message());
        }
    }

    /**
     * Looks for a torrent with the given info-hash. In case there is such
     * a torrent in the session, a {@link TorrentHandle} to that torrent
     * is returned.
     * <p/>
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
    public ArrayList<TorrentHandle> getTorrents() {
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
     * Set a dht custom storage constructor function
     * to be used internally when the dht is created.
     * <p>
     * Since the dht storage is a critical component for the dht behavior,
     * this function will only be effective the next time the dht is started.
     * If you never touch this feature, a default map-memory based storage
     * is used.
     * <p>
     * If you want to make sure the dht is initially created with your
     * custom storage, create a session with the setting
     * {@link com.frostwire.jlibtorrent.swig.settings_pack.bool_types#enable_dht}
     * to false, set your constructor function
     * and call {@link #applySettings(SettingsPack)} with
     * {@link com.frostwire.jlibtorrent.swig.settings_pack.bool_types#enable_dht}
     * to true.
     *
     * @param sc
     */
    public void setDhtStorage(DhtStorageConstructor sc) {
        dhtSC = new SwigDhtStorageConstructor(sc);
        s.set_swig_dht_storage(dhtSC);
    }

    /**
     * Applies the settings specified by the settings pack {@code sp}. This is an
     * asynchronous operation that will return immediately and actually apply
     * the settings to the main thread of libtorrent some time later.
     *
     * @param sp
     */
    public void applySettings(SettingsPack sp) {
        s.apply_settings(sp.getSwig());
    }
}
