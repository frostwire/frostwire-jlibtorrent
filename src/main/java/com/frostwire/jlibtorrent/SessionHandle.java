package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.plugins.DhtStorageConstructor;
import com.frostwire.jlibtorrent.plugins.SwigDhtStorageConstructor;
import com.frostwire.jlibtorrent.swig.*;

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

    public session_handle getSwig() {
        return s;
    }

    public void setDhtStorage(DhtStorageConstructor sc) {
        dhtSC = new SwigDhtStorageConstructor(sc);
        s.set_swig_dht_storage(dhtSC);
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
}
