package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.TorrentStatus;
import com.frostwire.jlibtorrent.swig.torrent_plugin;

/**
 * Torrent plugins are associated with a single torrent and have a number
 * of functions called at certain events. Many of its functions have the
 * ability to change or override the default libtorrent behavior.
 *
 * @author gubatron
 * @author aldenml
 */
public interface TorrentPlugin {

    boolean handleOperation(Operation op);

    /**
     * This function is called each time a new peer is connected to the torrent. You
     * // may choose to ignore this by just returning a default constructed
     * // ``shared_ptr`` (in which case you don't need to override this member
     * // function).
     * //
     * // If you need an extension to the peer connection (which most plugins do) you
     * // are supposed to return an instance of your peer_plugin class. Which in
     * // turn will have its hook functions called on event specific to that peer.
     * //
     * // The ``peer_connection`` will be valid as long as the ``shared_ptr`` is being
     * // held by the torrent object. So, it is generally a good idea to not keep a
     * // ``shared_ptr`` to your own peer_plugin. If you want to keep references to it,
     * // use ``weak_ptr``.
     * //
     * // If this function throws an exception, the connection will be closed.
     *
     * @param peerConnection
     * @return
     */
    PeerPlugin newPeerConnection(PeerConnection peerConnection);

    /**
     * These hooks are called when a piece passes the hash check or fails the hash
     * // check, respectively. The ``index`` is the piece index that was downloaded.
     * // It is possible to access the list of peers that participated in sending the
     * // piece through the ``torrent`` and the ``piece_picker``.
     *
     * @param index
     */
    void onPiecePass(int index);

    /**
     * These hooks are called when a piece passes the hash check or fails the hash
     * // check, respectively. The ``index`` is the piece index that was downloaded.
     * // It is possible to access the list of peers that participated in sending the
     * // piece through the ``torrent`` and the ``piece_picker``.
     *
     * @param index
     */
    void onPieceFailed(int index);

    /**
     * This hook is called approximately once per second. It is a way of making it
     * // easy for plugins to do timed events, for sending messages or whatever.
     */
    void tick();

    /**
     * These hooks are called when the torrent is paused and unpaused respectively.
     * // The return value indicates if the event was handled. A return value of
     * // ``true`` indicates that it was handled, and no other plugin after this one
     * // will have this hook function called, and the standard handler will also not be
     * // invoked. So, returning true effectively overrides the standard behavior of
     * // pause or unpause.
     * //
     * // Note that if you call ``pause()`` or ``resume()`` on the torrent from your
     * // handler it will recurse back into your handler, so in order to invoke the
     * // standard handler, you have to keep your own state on whether you want standard
     * // behavior or overridden behavior.
     *
     * @return
     */
    boolean onPause();

    /**
     * These hooks are called when the torrent is paused and unpaused respectively.
     * // The return value indicates if the event was handled. A return value of
     * // ``true`` indicates that it was handled, and no other plugin after this one
     * // will have this hook function called, and the standard handler will also not be
     * // invoked. So, returning true effectively overrides the standard behavior of
     * // pause or unpause.
     * //
     * // Note that if you call ``pause()`` or ``resume()`` on the torrent from your
     * // handler it will recurse back into your handler, so in order to invoke the
     * // standard handler, you have to keep your own state on whether you want standard
     * // behavior or overridden behavior.
     *
     * @return
     */
    boolean onResume();

    /**
     * This function is called when the initial files of the torrent have been
     * // checked. If there are no files to check, this function is called immediately.
     * //
     * // i.e. This function is always called when the torrent is in a state where it
     * // can start downloading.
     */
    void onFilesChecked();

    /**
     * called when the torrent changes state
     * // the state is one of torrent_status::state_t
     * // enum members
     *
     * @param s
     */
    void onState(TorrentStatus.State s);

    /**
     * called when the torrent is unloaded from RAM
     * // and loaded again, respectively
     * // unload is called right before the torrent is
     * // unloaded and load is called right after it's
     * // loaded. i.e. the full torrent state is available
     * // when these callbacks are called.
     */
    void onUnload();

    /**
     * called when the torrent is unloaded from RAM
     * // and loaded again, respectively
     * // unload is called right before the torrent is
     * // unloaded and load is called right after it's
     * // loaded. i.e. the full torrent state is available
     * // when these callbacks are called.
     */
    void onLoad();

    /**
     * called every time a new peer is added to the peer list.
     * // This is before the peer is connected to. For ``flags``, see
     * // torrent_plugin::flags_t. The ``source`` argument refers to
     * // the source where we learned about this peer from. It's a
     * // bitmask, because many sources may have told us about the same
     * // peer. For peer source flags, see peer_info::peer_source_flags.
     *
     * @param endp
     * @param src
     * @param flags
     */
    void onAddPeer(TcpEndpoint endp, int src, Flags flags);

    /**
     * Called every time policy::add_peer is called
     * src is a bitmask of which sources this peer
     * has been seen from. flags is a bitmask of:
     */
    enum Flags {

        /**
         * This is the first time we see this peer.
         */
        FIRST_TIME(torrent_plugin.flags_t.first_time.swigValue()),

        /**
         * This peer was not added because it was
         * filtered by the IP filter
         */
        FILTERED(torrent_plugin.flags_t.filtered.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Flags(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Flags fromSwig(int swigValue) {
            Flags[] enumValues = Flags.class.getEnumConstants();
            for (Flags ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }

    enum Operation {
        NEW_PEER_CONNECTION,
        ON_STATE,
        ON_ADD_PEER
    }
}
