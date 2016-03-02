package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.close_reason_t;

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
    CLOSE_NO_REASON(close_reason_t.close_no_reason.swigValue()),

    /**
     * We're already connected to.
     */
    CLOSE_DUPLICATE_PEER_ID(close_reason_t.close_duplicate_peer_id.swigValue()),

    /**
     * This torrent has been removed, paused or stopped from this client.
     */
    CLOSE_TORRENT_REMOVED(close_reason_t.close_torrent_removed.swigValue()),

    /**
     * Client failed to allocate necessary memory for this peer connection.
     */
    CLOSE_NO_MEMORY(close_reason_t.close_no_memory.swigValue()),

    /**
     * The source port of this peer is blocked.
     */
    CLOSE_PORT_BLOCKED(close_reason_t.close_port_blocked.swigValue()),

    /**
     * The source IP has been blocked.
     */
    CLOSE_BLOCKED(close_reason_t.close_blocked.swigValue()),

    /**
     * Both ends of the connection are upload-only. Staying connected would
     * be redundant.
     */
    CLOSE_UPLOAD_TO_UPLOAD(close_reason_t.close_upload_to_upload.swigValue()),

    /**
     * Connection was closed because the other end is upload only and does
     * not have any pieces we're interested in.
     */
    CLOSE_NOT_INTERESTED_UPLOAD_ONLY(close_reason_t.close_not_interested_upload_only.swigValue()),

    /**
     * Peer connection timed out (generic timeout).
     */
    CLOSE_TIMEOUT(close_reason_t.close_timeout.swigValue()),

    /**
     * The peers have not been interested in each other for a very long time.
     * disconnect.
     */
    CLOSE_TIMED_OUT_INTEREST(close_reason_t.close_timed_out_interest.swigValue()),

    /**
     * The peer has not sent any message in a long time.
     */
    CLOSE_TIMED_OUT_ACTIVITY(close_reason_t.close_timed_out_activity.swigValue()),

    /**
     * The peer did not complete the handshake in too long.
     */
    CLOSE_TIMED_OUT_HANDSHAKE(close_reason_t.close_timed_out_handshake.swigValue()),

    /**
     * The peer sent an interested message, but did not send a request
     * after a very long time after being unchoked.
     */
    CLOSE_TIMED_OUT_REQUEST(close_reason_t.close_timed_out_request.swigValue()),

    /**
     * The encryption mode is blocked.
     */
    CLOSE_PROTOCOL_BLOCKED(close_reason_t.close_protocol_blocked.swigValue()),

    /**
     * The peer was disconnected in the hopes of finding a better peer
     * in the swarm.
     */
    CLOSE_PEER_CHURN(close_reason_t.close_peer_churn.swigValue()),

    /**
     * We have too many peers connected.
     */
    CLOSE_TOO_MANY_CONNECTIONS(close_reason_t.close_too_many_connections.swigValue()),

    /**
     * We have too many file-descriptors open.
     */
    CLOSE_TOO_MANY_FILES(close_reason_t.close_too_many_files.swigValue()),

    /**
     * The encryption handshake failed.
     */
    CLOSE_ENCRYPTION_ERROR(close_reason_t.close_encryption_error.swigValue()),

    /**
     * The info hash sent as part of the handshake was not what we expected.
     */
    CLOSE_INVALID_INFO_HASH(close_reason_t.close_invalid_info_hash.swigValue()),

    /**
     *
     */
    CLOSE_SELF_CONNECTION(close_reason_t.close_self_connection.swigValue()),

    /**
     * The metadata received matched the info-hash, but failed to parse.
     * this is either someone finding a SHA1 collision, or the author of
     * the magnet link creating it from an invalid torrent.
     */
    CLOSE_INVALID_METADATA(close_reason_t.close_invalid_metadata.swigValue()),

    /**
     * The advertised metadata size.
     */
    CLOSE_METADATA_TOO_BIG(close_reason_t.close_metadata_too_big.swigValue()),

    /**
     *
     */
    CLOSE_MESSAGE_TOO_BIG(close_reason_t.close_message_too_big.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_MESSAGE_ID(close_reason_t.close_invalid_message_id.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_MESSAGE(close_reason_t.close_invalid_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_PIECE_MESSAGE(close_reason_t.close_invalid_piece_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_HAVE_MESSAGE(close_reason_t.close_invalid_have_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_BITFIELD_MESSAGE(close_reason_t.close_invalid_bitfield_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_CHOKE_MESSAGE(close_reason_t.close_invalid_choke_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_UNCHOKE_MESSAGE(close_reason_t.close_invalid_unchoke_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_INTERESTED_MESSAGE(close_reason_t.close_invalid_interested_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_NOT_INTERESTED_MESSAGE(close_reason_t.close_invalid_not_interested_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_REQUEST_MESSAGE(close_reason_t.close_invalid_request_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_REJECT_MESSAGE(close_reason_t.close_invalid_reject_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_ALLOW_FAST_MESSAGE(close_reason_t.close_invalid_allow_fast_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_EXTENDED_MESSAGE(close_reason_t.close_invalid_extended_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_CANCEL_MESSAGE(close_reason_t.close_invalid_cancel_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_DHT_PORT_MESSAGE(close_reason_t.close_invalid_dht_port_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_SUGGEST_MESSAGE(close_reason_t.close_invalid_suggest_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_HAVE_ALL_MESSAGE(close_reason_t.close_invalid_have_all_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_DONT_HAVE_MESSAGE(close_reason_t.close_invalid_dont_have_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_HAVE_NONE_MESSAGE(close_reason_t.close_invalid_have_none_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_PEX_MESSAGE(close_reason_t.close_invalid_pex_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_METADATA_REQUEST_MESSAGE(close_reason_t.close_invalid_metadata_request_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_METADATA_MESSAGE(close_reason_t.close_invalid_metadata_message.swigValue()),

    /**
     *
     */
    CLOSE_INVALID_METADATA_OFFSET(close_reason_t.close_invalid_metadata_offset.swigValue()),

    /**
     * The peer sent a request while being choked.
     */
    CLOSE_REQUEST_WHEN_CHOKED(close_reason_t.close_request_when_choked.swigValue()),

    /**
     * The peer sent corrupt data.
     */
    CLOSE_CORRUPT_PIECES(close_reason_t.close_corrupt_pieces.swigValue()),

    /**
     *
     */
    CLOSE_PEX_MESSAGE_TOO_BIG(close_reason_t.close_pex_message_too_big.swigValue()),

    /**
     *
     */
    CLOSE_PEX_TOO_FREQUENT(close_reason_t.close_pex_too_frequent.swigValue()),

    /**
     *
     */
    UNKNOWN(-1);

    CloseReason(int swigValue) {
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
    public static CloseReason fromSwig(int swigValue) {
        CloseReason[] enumValues = CloseReason.class.getEnumConstants();
        for (CloseReason ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
