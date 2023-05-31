package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.close_reason_t;

import java.util.HashMap;
import java.util.Map;

/**
 * These are all the reasons to disconnect a peer
 * all reasons caused by the peer sending unexpected data.
 *
 * @author gubatron
 * @author aldenml
 */
public enum CloseReason {

    /**
     * No reason specified. Generic close.
     */
    NONE(close_reason_t.none.swigValue()),

    /**
     * We're already connected to.
     */
    DUPLICATE_PEER_ID(close_reason_t.duplicate_peer_id.swigValue()),

    /**
     * This torrent has been removed, paused or stopped from this client.
     */
    TORRENT_REMOVED(close_reason_t.torrent_removed.swigValue()),

    /**
     * Client failed to allocate necessary memory for this peer connection.
     */
    NO_MEMORY(close_reason_t.no_memory.swigValue()),

    /**
     * The source port of this peer is blocked.
     */
    PORT_BLOCKED(close_reason_t.port_blocked.swigValue()),

    /**
     * The source IP has been blocked.
     */
    BLOCKED(close_reason_t.blocked.swigValue()),

    /**
     * Both ends of the connection are upload-only. Staying connected would
     * be redundant.
     */
    UPLOAD_TO_UPLOAD(close_reason_t.upload_to_upload.swigValue()),

    /**
     * Connection was closed because the other end is upload only and does
     * not have any pieces we're interested in.
     */
    NOT_INTERESTED_UPLOAD_ONLY(close_reason_t.not_interested_upload_only.swigValue()),

    /**
     * Peer connection timed out (generic timeout).
     */
    TIMEOUT(close_reason_t.timeout.swigValue()),

    /**
     * The peers have not been interested in each other for a very long time.
     * disconnect.
     */
    TIMED_OUT_INTEREST(close_reason_t.timed_out_interest.swigValue()),

    /**
     * The peer has not sent any message in a long time.
     */
    TIMED_OUT_ACTIVITY(close_reason_t.timed_out_activity.swigValue()),

    /**
     * The peer did not complete the handshake in too long.
     */
    TIMED_OUT_HANDSHAKE(close_reason_t.timed_out_handshake.swigValue()),

    /**
     * The peer sent an interested message, but did not send a request
     * after a very long time after being unchoked.
     */
    TIMED_OUT_REQUEST(close_reason_t.timed_out_request.swigValue()),

    /**
     * The encryption mode is blocked.
     */
    PROTOCOL_BLOCKED(close_reason_t.protocol_blocked.swigValue()),

    /**
     * The peer was disconnected in the hopes of finding a better peer
     * in the swarm.
     */
    PEER_CHURN(close_reason_t.peer_churn.swigValue()),

    /**
     * We have too many peers connected.
     */
    TOO_MANY_CONNECTIONS(close_reason_t.too_many_connections.swigValue()),

    /**
     * We have too many file-descriptors open.
     */
    TOO_MANY_FILES(close_reason_t.too_many_files.swigValue()),

    /**
     * The encryption handshake failed.
     */
    ENCRYPTION_ERROR(close_reason_t.encryption_error.swigValue()),

    /**
     * The info hash sent as part of the handshake was not what we expected.
     */
    INVALID_INFO_HASH(close_reason_t.invalid_info_hash.swigValue()),

    /**
     *
     */
    SELF_CONNECTION(close_reason_t.self_connection.swigValue()),

    /**
     * The metadata received matched the info-hash, but failed to parse.
     * this is either someone finding a SHA1 collision, or the author of
     * the magnet link creating it from an invalid torrent.
     */
    INVALID_METADATA(close_reason_t.invalid_metadata.swigValue()),

    /**
     * The advertised metadata size.
     */
    METADATA_TOO_BIG(close_reason_t.metadata_too_big.swigValue()),

    /**
     *
     */
    MESSAGE_TOO_BIG(close_reason_t.message_too_big.swigValue()),

    /**
     *
     */
    INVALID_MESSAGE_ID(close_reason_t.invalid_message_id.swigValue()),

    /**
     *
     */
    INVALID_MESSAGE(close_reason_t.invalid_message.swigValue()),

    /**
     *
     */
    INVALID_PIECE_MESSAGE(close_reason_t.invalid_piece_message.swigValue()),

    /**
     *
     */
    INVALID_HAVE_MESSAGE(close_reason_t.invalid_have_message.swigValue()),

    /**
     *
     */
    INVALID_BITFIELD_MESSAGE(close_reason_t.invalid_bitfield_message.swigValue()),

    /**
     *
     */
    INVALID_CHOKE_MESSAGE(close_reason_t.invalid_choke_message.swigValue()),

    /**
     *
     */
    INVALID_UNCHOKE_MESSAGE(close_reason_t.invalid_unchoke_message.swigValue()),

    /**
     *
     */
    INVALID_INTERESTED_MESSAGE(close_reason_t.invalid_interested_message.swigValue()),

    /**
     *
     */
    INVALID_NOT_INTERESTED_MESSAGE(close_reason_t.invalid_not_interested_message.swigValue()),

    /**
     *
     */
    INVALID_REQUEST_MESSAGE(close_reason_t.invalid_request_message.swigValue()),

    /**
     *
     */
    INVALID_REJECT_MESSAGE(close_reason_t.invalid_reject_message.swigValue()),

    /**
     *
     */
    INVALID_ALLOW_FAST_MESSAGE(close_reason_t.invalid_allow_fast_message.swigValue()),

    /**
     *
     */
    NVALID_EXTENDED_MESSAGE(close_reason_t.invalid_extended_message.swigValue()),

    /**
     *
     */
    INVALID_CANCEL_MESSAGE(close_reason_t.invalid_cancel_message.swigValue()),

    /**
     *
     */
    INVALID_DHT_PORT_MESSAGE(close_reason_t.invalid_dht_port_message.swigValue()),

    /**
     *
     */
    INVALID_SUGGEST_MESSAGE(close_reason_t.invalid_suggest_message.swigValue()),

    /**
     *
     */
    INVALID_HAVE_ALL_MESSAGE(close_reason_t.invalid_have_all_message.swigValue()),

    /**
     *
     */
    INVALID_DONT_HAVE_MESSAGE(close_reason_t.invalid_dont_have_message.swigValue()),

    /**
     *
     */
    INVALID_HAVE_NONE_MESSAGE(close_reason_t.invalid_have_none_message.swigValue()),

    /**
     *
     */
    INVALID_PEX_MESSAGE(close_reason_t.invalid_pex_message.swigValue()),

    /**
     *
     */
    INVALID_METADATA_REQUEST_MESSAGE(close_reason_t.invalid_metadata_request_message.swigValue()),

    /**
     *
     */
    INVALID_METADATA_MESSAGE(close_reason_t.invalid_metadata_message.swigValue()),

    /**
     *
     */
    INVALID_METADATA_OFFSET(close_reason_t.invalid_metadata_offset.swigValue()),

    /**
     * The peer sent a request while being choked.
     */
    REQUEST_WHEN_CHOKED(close_reason_t.request_when_choked.swigValue()),

    /**
     * The peer sent corrupt data.
     */
    CORRUPT_PIECES(close_reason_t.corrupt_pieces.swigValue()),

    /**
     *
     */
    PEX_MESSAGE_TOO_BIG(close_reason_t.pex_message_too_big.swigValue()),

    /**
     *
     */
    PEX_TOO_FREQUENT(close_reason_t.pex_too_frequent.swigValue()),

    /**
     *
     */
    UNKNOWN(-1);

    CloseReason(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return the native value
     */
    public int swig() {
        return swigValue;
    }

    private static Map<Integer, CloseReason> swigToCloseReason;
    private final static Object swigToCloseReasonLock = new Object();

    /**
     * Converted method, it's public in order to be used in other
     * internal packages.
     *
     * @param swigValue the native value
     * @return the enum value
     */
    public static CloseReason fromSwig(int swigValue) {
        if (swigToCloseReason == null) {
            synchronized (swigToCloseReasonLock) {
                swigToCloseReason = new HashMap<>();
                for (CloseReason r : values()) {
                    swigToCloseReason.put(r.swig(), r);
                }
            }
        }
        try {
            return swigToCloseReason.get(swigValue);
        } catch (ArrayIndexOutOfBoundsException e) {
            return UNKNOWN;
        }
    }
}
