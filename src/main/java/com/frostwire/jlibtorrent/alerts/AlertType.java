package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public enum AlertType {

    TORRENT(torrent_alert.alert_type),
    PEER(peer_alert.alert_type),
    TRACKER(tracker_alert.alert_type),
    TORRENT_ADDED(torrent_added_alert.alert_type),
    TORRENT_FINISHED(torrent_finished_alert.alert_type),
    TORRENT_REMOVED(torrent_removed_alert.alert_type),
    TORRENT_UPDATE(torrent_update_alert.alert_type),
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
    MMAP_CACHE(mmap_cache_alert.alert_type),
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
    ANONYMOUS_MODE(anonymous_mode_alert.alert_type),
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
    UNKNOWN(-1),
    TORRENT_PRIORITIZE(-2);

    AlertType(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int getSwig() {
        return swigValue;
    }

    public static AlertType fromSwig(int swigValue) {
        switch (swigValue) {
            case 0:
                return TORRENT;
            case 1:
                return PEER;
            case 2:
                return TRACKER;
            case 3:
                return TORRENT_ADDED;
            case 4:
                return TORRENT_REMOVED;
            case 5:
                return READ_PIECE;
            case 6:
                return FILE_COMPLETED;
            case 7:
                return FILE_RENAMED;
            case 8:
                return FILE_RENAME_FAILED;
            case 9:
                return PERFORMANCE;
            case 10:
                return STATE_CHANGED;
            case 11:
                return TRACKER_ERROR;
            case 12:
                return TRACKER_WARNING;
            case 13:
                return SCRAPE_REPLY;
            case 14:
                return SCRAPE_FAILED;
            case 15:
                return TRACKER_REPLY;
            case 16:
                return DHT_REPLY;
            case 17:
                return TRACKER_ANNOUNCE;
            case 18:
                return HASH_FAILED;
            case 19:
                return PEER_BAN;
            case 20:
                return PEER_UNSNUBBED;
            case 21:
                return PEER_SNUBBED;
            case 22:
                return PEER_ERROR;
            case 23:
                return PEER_CONNECT;
            case 24:
                return PEER_DISCONNECTED;
            case 25:
                return INVALID_REQUEST;
            case 26:
                return TORRENT_FINISHED;
            case 27:
                return PIECE_FINISHED;
            case 28:
                return REQUEST_DROPPED;
            case 29:
                return BLOCK_TIMEOUT;
            case 30:
                return BLOCK_FINISHED;
            case 31:
                return BLOCK_DOWNLOADING;
            case 32:
                return UNWANTED_BLOCK;
            case 33:
                return STORAGE_MOVED;
            case 34:
                return STORAGE_MOVED_FAILED;
            case 35:
                return TORRENT_DELETED;
            case 36:
                return TORRENT_DELETE_FAILED;
            case 37:
                return SAVE_RESUME_DATA;
            case 38:
                return SAVE_RESUME_DATA_FAILED;
            case 39:
                return TORRENT_PAUSED;
            case 40:
                return TORRENT_RESUMED;
            case 41:
                return TORRENT_CHECKED;
            case 42:
                return URL_SEED;
            case 43:
                return FILE_ERROR;
            case 44:
                return METADATA_FAILED;
            case 45:
                return METADATA_RECEIVED;
            case 46:
                return UDP_ERROR;
            case 47:
                return EXTERNAL_IP;
            case 48:
                return LISTEN_FAILED;
            case 49:
                return LISTEN_SUCCEEDED;
            case 50:
                return PORTMAP_ERROR;
            case 51:
                return PORTMAP;
            case 52:
                return PORTMAP_LOG;
            case 53:
                return FASTRESUME_REJECTED;
            case 54:
                return PEER_BLOCKED;
            case 55:
                return DHT_ANNOUNCE;
            case 56:
                return DHT_GET_PEERS;
            case 57:
                return STATS;
            case 58:
                return CACHE_FLUSHED;
            case 59:
                return ANONYMOUS_MODE;
            case 60:
                return LSD_PEER;
            case 61:
                return TRACKERID;
            case 62:
                return DHT_BOOTSTRAP;
            case 63:
                return UNKNOWN;
            case 64:
                return TORRENT_ERROR;
            case 65:
                return TORRENT_NEED_CERT;
            case 66:
                return INCOMING_CONNECTION;
            case 67:
                return ADD_TORRENT;
            case 68:
                return STATE_UPDATE;
            case 69:
                return MMAP_CACHE;
            case 70:
                return SESSION_STATS;
            case 71:
                return TORRENT_UPDATE;
            case 72:
                return UNKNOWN;
            case 73:
                return DHT_ERROR;
            case 74:
                return DHT_IMMUTABLE_ITEM;
            case 75:
                return DHT_MUTABLE_ITEM;
            case 76:
                return DHT_PUT;
            case 77:
                return I2P;
            case 78:
                return DHT_OUTGOING_GET_PEERS;
            case 79:
                return LOG;
            case 80:
                return TORRENT_LOG;
            case 81:
                return PEER_LOG;
            case 82:
                return LSD_ERROR;
            case 83:
                return DHT_STATS;
            case 84:
                return INCOMING_REQUEST;
            case 85:
                return DHT_LOG;
            case 86:
                return DHT_PKT;
            case 87:
                return DHT_GET_PEERS_REPLY;
            case 88:
                return DHT_DIRECT_RESPONSE;
            case 89:
                return PICKER_LOG;
            default:
                return UNKNOWN;
        }
    }
}
