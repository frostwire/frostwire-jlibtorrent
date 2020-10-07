package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public enum AlertType {

    TORRENT_FINISHED(torrent_finished_alert.alert_type),
    TORRENT_REMOVED(torrent_removed_alert.alert_type),
    TORRENT_DELETED(torrent_deleted_alert.alert_type),
    TORRENT_PAUSED(torrent_paused_alert.alert_type),
    TORRENT_RESUMED(torrent_resumed_alert.alert_type),
    TORRENT_CHECKED(torrent_checked_alert.alert_type),
    TORRENT_ERROR(torrent_error_alert.alert_type),
    TORRENT_NEED_CERT(torrent_need_cert_alert.alert_type),
    INCOMING_CONNECTION(incoming_connection_alert.alert_type),
    ADD_TORRENT(add_torrent_alert.alert_type),
    SAVE_RESUME_DATA(save_resume_data_alert.alert_type),
    FASTRESUME_REJECTED(fastresume_rejected_alert.alert_type),
    BLOCK_FINISHED(block_finished_alert.alert_type),
    METADATA_RECEIVED(metadata_received_alert.alert_type),
    METADATA_FAILED(metadata_failed_alert.alert_type),
    FILE_COMPLETED(file_completed_alert.alert_type),
    FILE_RENAMED(file_renamed_alert.alert_type),
    FILE_RENAME_FAILED(file_rename_failed_alert.alert_type),
    FILE_ERROR(file_error_alert.alert_type),
    HASH_FAILED(hash_failed_alert.alert_type),
    PORTMAP(portmap_alert.alert_type),
    PORTMAP_ERROR(portmap_error_alert.alert_type),
    PORTMAP_LOG(portmap_log_alert.alert_type),
    TRACKER_ANNOUNCE(tracker_announce_alert.alert_type),
    TRACKER_REPLY(tracker_reply_alert.alert_type),
    TRACKER_WARNING(tracker_warning_alert.alert_type),
    TRACKER_ERROR(tracker_error_alert.alert_type),
    READ_PIECE(read_piece_alert.alert_type),
    STATE_CHANGED(state_changed_alert.alert_type),
    DHT_REPLY(dht_reply_alert.alert_type),
    DHT_BOOTSTRAP(dht_bootstrap_alert.alert_type),
    DHT_GET_PEERS(dht_get_peers_alert.alert_type),
    EXTERNAL_IP(external_ip_alert.alert_type),
    LISTEN_SUCCEEDED(listen_succeeded_alert.alert_type),
    STATE_UPDATE(state_update_alert.alert_type),
    SESSION_STATS(session_stats_alert.alert_type),
    SCRAPE_REPLY(scrape_reply_alert.alert_type),
    SCRAPE_FAILED(scrape_failed_alert.alert_type),
    LSD_PEER(lsd_peer_alert.alert_type),
    PEER_BLOCKED(peer_blocked_alert.alert_type),
    PERFORMANCE(performance_alert.alert_type),
    PIECE_FINISHED(piece_finished_alert.alert_type),
    SAVE_RESUME_DATA_FAILED(save_resume_data_failed_alert.alert_type),
    STATS(stats_alert.alert_type),
    STORAGE_MOVED(storage_moved_alert.alert_type),
    TORRENT_DELETE_FAILED(torrent_delete_failed_alert.alert_type),
    URL_SEED(url_seed_alert.alert_type),
    INVALID_REQUEST(invalid_request_alert.alert_type),
    LISTEN_FAILED(listen_failed_alert.alert_type),
    PEER_BAN(peer_ban_alert.alert_type),
    PEER_CONNECT(peer_connect_alert.alert_type),
    PEER_DISCONNECTED(peer_disconnected_alert.alert_type),
    PEER_ERROR(peer_error_alert.alert_type),
    PEER_SNUBBED(peer_snubbed_alert.alert_type),
    PEER_UNSNUBBED(peer_unsnubbed_alert.alert_type),
    REQUEST_DROPPED(request_dropped_alert.alert_type),
    UDP_ERROR(udp_error_alert.alert_type),
    BLOCK_DOWNLOADING(block_downloading_alert.alert_type),
    BLOCK_TIMEOUT(block_timeout_alert.alert_type),
    CACHE_FLUSHED(cache_flushed_alert.alert_type),
    DHT_ANNOUNCE(dht_announce_alert.alert_type),
    STORAGE_MOVED_FAILED(storage_moved_failed_alert.alert_type),
    TRACKERID(trackerid_alert.alert_type),
    UNWANTED_BLOCK(unwanted_block_alert.alert_type),
    DHT_ERROR(dht_error_alert.alert_type),
    DHT_PUT(dht_put_alert.alert_type),
    DHT_MUTABLE_ITEM(dht_mutable_item_alert.alert_type),
    DHT_IMMUTABLE_ITEM(dht_immutable_item_alert.alert_type),
    I2P(i2p_alert.alert_type),
    DHT_OUTGOING_GET_PEERS(dht_outgoing_get_peers_alert.alert_type),
    LOG(log_alert.alert_type),
    TORRENT_LOG(torrent_log_alert.alert_type),
    PEER_LOG(peer_log_alert.alert_type),
    LSD_ERROR(lsd_error_alert.alert_type),
    DHT_STATS(dht_stats_alert.alert_type),
    INCOMING_REQUEST(incoming_request_alert.alert_type),
    DHT_LOG(dht_log_alert.alert_type),
    DHT_PKT(dht_pkt_alert.alert_type),
    DHT_GET_PEERS_REPLY(dht_get_peers_reply_alert.alert_type),
    DHT_DIRECT_RESPONSE(dht_direct_response_alert.alert_type),
    PICKER_LOG(picker_log_alert.alert_type),
    SESSION_ERROR(session_error_alert.alert_type),
    DHT_LIVE_NODES(dht_live_nodes_alert.alert_type),
    SESSION_STATS_HEADER(session_stats_header_alert.alert_type),
    DHT_SAMPLE_INFOHASHES(dht_sample_infohashes_alert.alert_type),
    BLOCK_UPLOADED(block_uploaded_alert.alert_type),
    ALERTS_DROPPED(alerts_dropped_alert.alert_type),
    SOCKS5_ALERT(socks5_alert.alert_type),
    UNKNOWN(-1);

    private static final AlertType[] TABLE = buildTable();

    AlertType(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return the native swig value
     */
    public int swig() {
        return swigValue;
    }

    /**
     * @param swigValue the native swig value
     * @return the API enum alert type
     */
    public static AlertType fromSwig(int swigValue) {
        return TABLE[swigValue];
    }

    private static AlertType[] buildTable() {
        AlertType[] arr = new AlertType[Alerts.NUM_ALERT_TYPES];

        arr[0] = UNKNOWN;
        arr[1] = UNKNOWN;
        arr[2] = UNKNOWN;
        arr[3] = UNKNOWN;
        arr[4] = TORRENT_REMOVED;
        arr[5] = READ_PIECE;
        arr[6] = FILE_COMPLETED;
        arr[7] = FILE_RENAMED;
        arr[8] = FILE_RENAME_FAILED;
        arr[9] = PERFORMANCE;
        arr[10] = STATE_CHANGED;
        arr[11] = TRACKER_ERROR;
        arr[12] = TRACKER_WARNING;
        arr[13] = SCRAPE_REPLY;
        arr[14] = SCRAPE_FAILED;
        arr[15] = TRACKER_REPLY;
        arr[16] = DHT_REPLY;
        arr[17] = TRACKER_ANNOUNCE;
        arr[18] = HASH_FAILED;
        arr[19] = PEER_BAN;
        arr[20] = PEER_UNSNUBBED;
        arr[21] = PEER_SNUBBED;
        arr[22] = PEER_ERROR;
        arr[23] = PEER_CONNECT;
        arr[24] = PEER_DISCONNECTED;
        arr[25] = INVALID_REQUEST;
        arr[26] = TORRENT_FINISHED;
        arr[27] = PIECE_FINISHED;
        arr[28] = REQUEST_DROPPED;
        arr[29] = BLOCK_TIMEOUT;
        arr[30] = BLOCK_FINISHED;
        arr[31] = BLOCK_DOWNLOADING;
        arr[32] = UNWANTED_BLOCK;
        arr[33] = STORAGE_MOVED;
        arr[34] = STORAGE_MOVED_FAILED;
        arr[35] = TORRENT_DELETED;
        arr[36] = TORRENT_DELETE_FAILED;
        arr[37] = SAVE_RESUME_DATA;
        arr[38] = SAVE_RESUME_DATA_FAILED;
        arr[39] = TORRENT_PAUSED;
        arr[40] = TORRENT_RESUMED;
        arr[41] = TORRENT_CHECKED;
        arr[42] = URL_SEED;
        arr[43] = FILE_ERROR;
        arr[44] = METADATA_FAILED;
        arr[45] = METADATA_RECEIVED;
        arr[46] = UDP_ERROR;
        arr[47] = EXTERNAL_IP;
        arr[48] = LISTEN_FAILED;
        arr[49] = LISTEN_SUCCEEDED;
        arr[50] = PORTMAP_ERROR;
        arr[51] = PORTMAP;
        arr[52] = PORTMAP_LOG;
        arr[53] = FASTRESUME_REJECTED;
        arr[54] = PEER_BLOCKED;
        arr[55] = DHT_ANNOUNCE;
        arr[56] = DHT_GET_PEERS;
        arr[57] = STATS;
        arr[58] = CACHE_FLUSHED;
        arr[59] = UNKNOWN;
        arr[60] = LSD_PEER;
        arr[61] = TRACKERID;
        arr[62] = DHT_BOOTSTRAP;
        arr[63] = UNKNOWN;
        arr[64] = TORRENT_ERROR;
        arr[65] = TORRENT_NEED_CERT;
        arr[66] = INCOMING_CONNECTION;
        arr[67] = ADD_TORRENT;
        arr[68] = STATE_UPDATE;
        arr[69] = UNKNOWN;
        arr[70] = SESSION_STATS;
        arr[71] = UNKNOWN;
        arr[72] = UNKNOWN;
        arr[73] = DHT_ERROR;
        arr[74] = DHT_IMMUTABLE_ITEM;
        arr[75] = DHT_MUTABLE_ITEM;
        arr[76] = DHT_PUT;
        arr[77] = I2P;
        arr[78] = DHT_OUTGOING_GET_PEERS;
        arr[79] = LOG;
        arr[80] = TORRENT_LOG;
        arr[81] = PEER_LOG;
        arr[82] = LSD_ERROR;
        arr[83] = DHT_STATS;
        arr[84] = INCOMING_REQUEST;
        arr[85] = DHT_LOG;
        arr[86] = DHT_PKT;
        arr[87] = DHT_GET_PEERS_REPLY;
        arr[88] = DHT_DIRECT_RESPONSE;
        arr[89] = PICKER_LOG;
        arr[90] = SESSION_ERROR;
        arr[91] = DHT_LIVE_NODES;
        arr[92] = SESSION_STATS_HEADER;
        arr[93] = DHT_SAMPLE_INFOHASHES;
        arr[94] = BLOCK_UPLOADED;
        arr[95] = ALERTS_DROPPED;
        arr[96] = SOCKS5_ALERT;
        return arr;
    }
}
