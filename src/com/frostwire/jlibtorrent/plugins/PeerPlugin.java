package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.lazy_entry;

/**
 * peer plugins are associated with a specific peer. A peer could be
 * // both a regular bittorrent peer (``bt_peer_connection``) or one of the
 * // web seed connections (``web_peer_connection`` or ``http_seed_connection``).
 * // In order to only attach to certain peers, make your
 * // torrent_plugin::new_connection only return a plugin for certain peer
 * // connection types.
 *
 * @author gubatron
 * @author aldenml
 */
public interface PeerPlugin {

    boolean handleOperation(Operation op);

    /**
     * This function is expected to return the name of
     * // the plugin.
     *
     * @return
     */
    String type();

    /**
     * can add entries to the extension handshake
     * // this is not called for web seeds.
     *
     * @param e
     */
    void addHandshake(Entry e);

    /**
     * called when the peer is being disconnected.
     *
     * @param ec
     */
    void onDisconnect(ErrorCode ec);

    /**
     * called when the peer is successfully connected. Note that
     * // incoming connections will have been connected by the time
     * // the peer plugin is attached to it, and won't have this hook
     * // called.
     */
    void onConnected();

    // throwing an exception from any of the handlers (except add_handshake)
    // closes the connection

    /**
     * this is called when the initial BT handshake is received. Returning false
     * // means that the other end doesn't support this extension and will remove
     * // it from the list of plugins.
     * // this is not called for web seeds
     *
     * @param reservedBits
     * @return
     */
    boolean onHandshake(byte[] reservedBits);

    /**
     * called when the extension handshake from the other end is received
     * // if this returns false, it means that this extension isn't
     * // supported by this peer. It will result in this peer_plugin
     * // being removed from the peer_connection and destructed.
     * // this is not called for web seeds.
     *
     * @param n
     * @return
     */
    boolean onExtensionHandshake(lazy_entry n);

    // returning true from any of the message handlers
    // indicates that the plugin has handeled the message.
    // it will break the plugin chain traversing and not let
    // anyone else handle the message, including the default
    // handler.

    boolean onChoke();

    boolean onUnchoke();

    boolean onInterested();

    boolean onNotInterested();

    boolean onHave(int index);

    boolean onDontHave(int index);

    boolean onBitfield(Bitfield bitfield);

    boolean onHaveAll();

    boolean onHaveNone();

    boolean onAllowedFast(int index);

    boolean onRequest(PeerRequest r);

    boolean onPiece(PeerRequest piece, DiskBufferHolder data);

    boolean onCancel(PeerRequest r);

    boolean onReject(PeerRequest r);

    boolean onSuggest(int index);

    /**
     * called after a choke message has been sent to the peer.
     */
    void sentUnchoke();

    /**
     * called after piece data has been sent to the peer
     * // this can be used for stats book keeping.
     *
     * @param bytes
     */
    void sentPayload(int bytes);

    /**
     * called when libtorrent think this peer should be disconnected.
     * // if the plugin returns false, the peer will not be disconnected.
     *
     * @param ec
     * @return
     */
    boolean canDisconnect(ErrorCode ec);

    /**
     * called when a piece that this peer participated in either
     * // fails or passes the hash_check.
     *
     * @param index
     */
    void onPiecePass(int index);

    /**
     * called when a piece that this peer participated in either
     * // fails or passes the hash_check.
     *
     * @param index
     */
    void onPieceFailed(int index);

    /**
     * called aproximately once every second.
     */
    void tick();

    /**
     * called each time a request message is to be sent. If true
     * // is returned, the original request message won't be sent and
     * // no other plugin will have this function called.
     *
     * @param r
     * @return
     */
    boolean writeRequest(PeerRequest r);

    enum Operation {
        ADD_HANDSHAKE,
        ON_DISCONNECT,
        ON_HANDSHAKE,
        ON_EXTENSION_HANDSHAKE,
        ON_BITFIELD,
        ON_REQUEST,
        ON_PIECE,
        ON_CANCEL,
        ON_REJECT,
        CAN_DISCONNECT,
        WRITE_REQUEST
    }
}
