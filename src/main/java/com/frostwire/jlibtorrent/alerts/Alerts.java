package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;

import static com.frostwire.jlibtorrent.swig.alert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Alerts {

    private Alerts() {
    }

    public static Alert cast(alert a) {
        switch (a.type()) {
            case 0:
                return new TorrentAlert(cast_to_torrent_alert(a));
            case 1:
                return new PeerAlert(cast_to_peer_alert(a));
            case 2:
                return new TrackerAlert(cast_to_tracker_alert(a));
            case 3:
                return new TorrentAddedAlert(cast_to_torrent_added_alert(a));
            case 4:
                return new TorrentRemovedAlert(cast_to_torrent_removed_alert(a));
            case 5:
                return new ReadPieceAlert(cast_to_read_piece_alert(a));
            case 6:
                return new FileCompletedAlert(cast_to_file_completed_alert(a));
            case 7:
                return new FileRenamedAlert(cast_to_file_renamed_alert(a));
            case 8:
                return new FileRenameFailedAlert(cast_to_file_rename_failed_alert(a));
            case 9:
                return new PerformanceAlert(cast_to_performance_alert(a));
            case 10:
                return new StateChangedAlert(cast_to_state_changed_alert(a));
            case 11:
                return new TrackerErrorAlert(cast_to_tracker_error_alert(a));
            case 12:
                return new TrackerWarningAlert(cast_to_tracker_warning_alert(a));
            case 13:
                return new ScrapeReplyAlert(cast_to_scrape_reply_alert(a));
            case 14:
                return new ScrapeFailedAlert(cast_to_scrape_failed_alert(a));
            case 15:
                return new TrackerReplyAlert(cast_to_tracker_reply_alert(a));
            case 16:
                return new DhtReplyAlert(cast_to_dht_reply_alert(a));
            case 17:
                return new TrackerAnnounceAlert(cast_to_tracker_announce_alert(a));
            case 18:
                return new HashFailedAlert(cast_to_hash_failed_alert(a));
            case 19:
                return new PeerBanAlert(cast_to_peer_ban_alert(a));
            case 20:
                return new PeerUnsnubbedAlert(cast_to_peer_unsnubbed_alert(a));
            case 21:
                return new PeerSnubbedAlert(cast_to_peer_snubbed_alert(a));
            case 22:
                return new PeerErrorAlert(cast_to_peer_error_alert(a));
            case 23:
                return new PeerConnectAlert(cast_to_peer_connect_alert(a));
            case 24:
                return new PeerDisconnectedAlert(cast_to_peer_disconnected_alert(a));
            case 25:
                return new InvalidRequestAlert(cast_to_invalid_request_alert(a));
            case 26:
                return new TorrentFinishedAlert(cast_to_torrent_finished_alert(a));
            case 27:
                return new PieceFinishedAlert(cast_to_piece_finished_alert(a));
            case 28:
                return new RequestDroppedAlert(cast_to_request_dropped_alert(a));
            case 29:
                return new BlockTimeoutAlert(cast_to_block_timeout_alert(a));
            case 30:
                return new BlockFinishedAlert(cast_to_block_finished_alert(a));
            case 31:
                return new BlockDownloadingAlert(cast_to_block_downloading_alert(a));
            case 32:
                return new UnwantedBlockAlert(cast_to_unwanted_block_alert(a));
            case 33:
                return new StorageMovedAlert(cast_to_storage_moved_alert(a));
            case 34:
                return new StorageMovedFailedAlert(cast_to_storage_moved_failed_alert(a));
            case 35:
                return new TorrentDeletedAlert(cast_to_torrent_deleted_alert(a));
            case 36:
                return new TorrentDeleteFailedAlert(cast_to_torrent_delete_failed_alert(a));
            case 37:
                return new SaveResumeDataAlert(cast_to_save_resume_data_alert(a));
            case 38:
                return new SaveResumeDataFailedAlert(cast_to_save_resume_data_failed_alert(a));
            case 39:
                return new TorrentPausedAlert(cast_to_torrent_paused_alert(a));
            case 40:
                return new TorrentResumedAlert(cast_to_torrent_resumed_alert(a));
            case 41:
                return new TorrentCheckedAlert(cast_to_torrent_checked_alert(a));
            case 42:
                return new UrlSeedAlert(cast_to_url_seed_alert(a));
            case 43:
                return new FileErrorAlert(cast_to_file_error_alert(a));
            case 44:
                return new MetadataFailedAlert(cast_to_metadata_failed_alert(a));
            case 45:
                return new MetadataReceivedAlert(cast_to_metadata_received_alert(a));
            case 46:
                return new UdpErrorAlert(cast_to_udp_error_alert(a));
            case 47:
                return new ExternalIpAlert(cast_to_external_ip_alert(a));
            case 48:
                return new ListenFailedAlert(cast_to_listen_failed_alert(a));
            case 49:
                return new ListenSucceededAlert(cast_to_listen_succeeded_alert(a));
            case 50:
                return new PortmapErrorAlert(cast_to_portmap_error_alert(a));
            case 51:
                return new PortmapAlert(cast_to_portmap_alert(a));
            case 52:
                return new PortmapLogAlert(cast_to_portmap_log_alert(a));
            case 53:
                return new FastresumeRejectedAlert(cast_to_fastresume_rejected_alert(a));
            case 54:
                return new PeerBlockedAlert(cast_to_peer_blocked_alert(a));
            case 55:
                return new DhtAnnounceAlert(cast_to_dht_announce_alert(a));
            case 56:
                return new DhtGetPeersAlert(cast_to_dht_get_peers_alert(a));
            case 57:
                return new StatsAlert(cast_to_stats_alert(a));
            case 58:
                return new CacheFlushedAlert(cast_to_cache_flushed_alert(a));
            case 59:
                return new AnonymousModeAlert(cast_to_anonymous_mode_alert(a));
            case 60:
                return new LsdPeerAlert(cast_to_lsd_peer_alert(a));
            case 61:
                return new TrackeridAlert(cast_to_trackerid_alert(a));
            case 62:
                return new DhtBootstrapAlert(cast_to_dht_bootstrap_alert(a));
            case 63:
                return new GenericAlert(a);
            case 64:
                return new TorrentErrorAlert(cast_to_torrent_error_alert(a));
            case 65:
                return new TorrentNeedCertAlert(cast_to_torrent_need_cert_alert(a));
            case 66:
                return new IncomingConnectionAlert(cast_to_incoming_connection_alert(a));
            case 67:
                return new AddTorrentAlert(cast_to_add_torrent_alert(a));
            case 68:
                return new StateUpdateAlert(cast_to_state_update_alert(a));
            case 69:
                return new MmapCacheAlert(cast_to_mmap_cache_alert(a));
            case 70:
                return new SessionStatsAlert(cast_to_session_stats_alert(a));
            case 71:
                return new TorrentUpdateAlert(cast_to_torrent_update_alert(a));
            case 72:
                return new GenericAlert(a);
            case 73:
                return new DhtErrorAlert(cast_to_dht_error_alert(a));
            case 74:
                return new DhtImmutableItemAlert(cast_to_dht_immutable_item_alert(a));
            case 75:
                return new DhtMutableItemAlert(cast_to_dht_mutable_item_alert(a));
            case 76:
                return new DhtPutAlert(cast_to_dht_put_alert(a));
            case 77:
                return new I2pAlert(cast_to_i2p_alert(a));
            case 78:
                return new DhtOutgoingGetPeersAlert(cast_to_dht_outgoing_get_peers_alert(a));
            case 79:
                return new LogAlert(cast_to_log_alert(a));
            case 80:
                return new TorrentLogAlert(cast_to_torrent_log_alert(a));
            case 81:
                return new PeerLogAlert(cast_to_peer_log_alert(a));
            case 82:
                return new LsdErrorAlert(cast_to_lsd_error_alert(a));
            case 83:
                return new DhtStatsAlert(cast_to_dht_stats_alert(a));
            case 84:
                return new IncomingRequestAlert(cast_to_incoming_request_alert(a));
            case 85:
                return new DhtLogAlert(cast_to_dht_log_alert(a));
            case 86:
                return new DhtPktAlert(cast_to_dht_pkt_alert(a));
            case 87:
                return new DhtGetPeersReplyAlert(cast_to_dht_get_peers_reply_alert(a));
            case 88:
                return new DhtDirectResponseAlert(cast_to_dht_direct_response_alert(a));
            case 89:
                return new PickerLogAlert(cast_to_picker_log_alert(a));
            default:
                return new GenericAlert(a);
        }
    }
}
