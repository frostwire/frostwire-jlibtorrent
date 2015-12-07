package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.libtorrent;

import static com.frostwire.jlibtorrent.swig.alert.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Alerts {

    private static CastLambda[] TABLE = buildTable();

    private Alerts() {
    }

    public static Alert cast(alert a) {
        return TABLE[a.type()].cast(a);
    }

    private static CastLambda[] buildTable() {
        CastLambda[] arr = new CastLambda[libtorrent.num_alert_types];

        arr[0] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentAlert(cast_to_torrent_alert(a));
            }
        };
        arr[1] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerAlert(cast_to_peer_alert(a));
            }
        };
        arr[2] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackerAlert(cast_to_tracker_alert(a));
            }
        };
        arr[3] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentAddedAlert(cast_to_torrent_added_alert(a));
            }
        };
        arr[4] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentRemovedAlert(cast_to_torrent_removed_alert(a));
            }
        };
        arr[5] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ReadPieceAlert(cast_to_read_piece_alert(a));
            }
        };
        arr[6] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new FileCompletedAlert(cast_to_file_completed_alert(a));
            }
        };
        arr[7] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new FileRenamedAlert(cast_to_file_renamed_alert(a));
            }
        };
        arr[8] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new FileRenameFailedAlert(cast_to_file_rename_failed_alert(a));
            }
        };
        arr[9] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PerformanceAlert(cast_to_performance_alert(a));
            }
        };
        arr[10] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new StateChangedAlert(cast_to_state_changed_alert(a));
            }
        };
        arr[11] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackerErrorAlert(cast_to_tracker_error_alert(a));
            }
        };
        arr[12] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackerWarningAlert(cast_to_tracker_warning_alert(a));
            }
        };
        arr[13] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ScrapeReplyAlert(cast_to_scrape_reply_alert(a));
            }
        };
        arr[14] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ScrapeFailedAlert(cast_to_scrape_failed_alert(a));
            }
        };
        arr[15] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackerReplyAlert(cast_to_tracker_reply_alert(a));
            }
        };
        arr[16] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtReplyAlert(cast_to_dht_reply_alert(a));
            }
        };
        arr[17] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackerAnnounceAlert(cast_to_tracker_announce_alert(a));
            }
        };
        arr[18] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new HashFailedAlert(cast_to_hash_failed_alert(a));
            }
        };
        arr[19] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerBanAlert(cast_to_peer_ban_alert(a));
            }
        };
        arr[20] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerUnsnubbedAlert(cast_to_peer_unsnubbed_alert(a));
            }
        };
        arr[21] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerSnubbedAlert(cast_to_peer_snubbed_alert(a));
            }
        };
        arr[22] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerErrorAlert(cast_to_peer_error_alert(a));
            }
        };
        arr[23] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerConnectAlert(cast_to_peer_connect_alert(a));
            }
        };
        arr[24] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerDisconnectedAlert(cast_to_peer_disconnected_alert(a));
            }
        };
        arr[25] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new InvalidRequestAlert(cast_to_invalid_request_alert(a));
            }
        };
        arr[26] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentFinishedAlert(cast_to_torrent_finished_alert(a));
            }
        };
        arr[27] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PieceFinishedAlert(cast_to_piece_finished_alert(a));
            }
        };
        arr[28] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new RequestDroppedAlert(cast_to_request_dropped_alert(a));
            }
        };
        arr[29] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new BlockTimeoutAlert(cast_to_block_timeout_alert(a));
            }
        };
        arr[30] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new BlockFinishedAlert(cast_to_block_finished_alert(a));
            }
        };
        arr[31] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new BlockDownloadingAlert(cast_to_block_downloading_alert(a));
            }
        };
        arr[32] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new UnwantedBlockAlert(cast_to_unwanted_block_alert(a));
            }
        };
        arr[33] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new StorageMovedAlert(cast_to_storage_moved_alert(a));
            }
        };
        arr[34] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new StorageMovedFailedAlert(cast_to_storage_moved_failed_alert(a));
            }
        };
        arr[35] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentDeletedAlert(cast_to_torrent_deleted_alert(a));
            }
        };
        arr[36] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentDeleteFailedAlert(cast_to_torrent_delete_failed_alert(a));
            }
        };
        arr[37] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new SaveResumeDataAlert(cast_to_save_resume_data_alert(a));
            }
        };
        arr[38] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new SaveResumeDataFailedAlert(cast_to_save_resume_data_failed_alert(a));
            }
        };
        arr[39] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentPausedAlert(cast_to_torrent_paused_alert(a));
            }
        };
        arr[40] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentResumedAlert(cast_to_torrent_resumed_alert(a));
            }
        };
        arr[41] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentCheckedAlert(cast_to_torrent_checked_alert(a));
            }
        };
        arr[42] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new UrlSeedAlert(cast_to_url_seed_alert(a));
            }
        };
        arr[43] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new FileErrorAlert(cast_to_file_error_alert(a));
            }
        };
        arr[44] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new MetadataFailedAlert(cast_to_metadata_failed_alert(a));
            }
        };
        arr[45] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new MetadataReceivedAlert(cast_to_metadata_received_alert(a));
            }
        };
        arr[46] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new UdpErrorAlert(cast_to_udp_error_alert(a));
            }
        };
        arr[47] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ExternalIpAlert(cast_to_external_ip_alert(a));
            }
        };
        arr[48] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ListenFailedAlert(cast_to_listen_failed_alert(a));
            }
        };
        arr[49] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new ListenSucceededAlert(cast_to_listen_succeeded_alert(a));
            }
        };
        arr[50] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PortmapErrorAlert(cast_to_portmap_error_alert(a));
            }
        };
        arr[51] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PortmapAlert(cast_to_portmap_alert(a));
            }
        };
        arr[52] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PortmapLogAlert(cast_to_portmap_log_alert(a));
            }
        };
        arr[53] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new FastresumeRejectedAlert(cast_to_fastresume_rejected_alert(a));
            }
        };
        arr[54] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerBlockedAlert(cast_to_peer_blocked_alert(a));
            }
        };
        arr[55] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtAnnounceAlert(cast_to_dht_announce_alert(a));
            }
        };
        arr[56] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtGetPeersAlert(cast_to_dht_get_peers_alert(a));
            }
        };
        arr[57] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new StatsAlert(cast_to_stats_alert(a));
            }
        };
        arr[58] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new CacheFlushedAlert(cast_to_cache_flushed_alert(a));
            }
        };
        arr[59] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new AnonymousModeAlert(cast_to_anonymous_mode_alert(a));
            }
        };
        arr[60] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new LsdPeerAlert(cast_to_lsd_peer_alert(a));
            }
        };
        arr[61] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TrackeridAlert(cast_to_trackerid_alert(a));
            }
        };
        arr[62] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtBootstrapAlert(cast_to_dht_bootstrap_alert(a));
            }
        };
        arr[63] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new GenericAlert(a);
            }
        };
        arr[64] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentErrorAlert(cast_to_torrent_error_alert(a));
            }
        };
        arr[65] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentNeedCertAlert(cast_to_torrent_need_cert_alert(a));
            }
        };
        arr[66] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new IncomingConnectionAlert(cast_to_incoming_connection_alert(a));
            }
        };
        arr[67] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new AddTorrentAlert(cast_to_add_torrent_alert(a));
            }
        };
        arr[68] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new StateUpdateAlert(cast_to_state_update_alert(a));
            }
        };
        arr[69] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new MmapCacheAlert(cast_to_mmap_cache_alert(a));
            }
        };
        arr[70] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new SessionStatsAlert(cast_to_session_stats_alert(a));
            }
        };
        arr[71] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentUpdateAlert(cast_to_torrent_update_alert(a));
            }
        };
        arr[72] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new GenericAlert(a);
            }
        };
        arr[73] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtErrorAlert(cast_to_dht_error_alert(a));
            }
        };
        arr[74] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtImmutableItemAlert(cast_to_dht_immutable_item_alert(a));
            }
        };
        arr[75] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtMutableItemAlert(cast_to_dht_mutable_item_alert(a));
            }
        };
        arr[76] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtPutAlert(cast_to_dht_put_alert(a));
            }
        };
        arr[77] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new I2pAlert(cast_to_i2p_alert(a));
            }
        };
        arr[78] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtOutgoingGetPeersAlert(cast_to_dht_outgoing_get_peers_alert(a));
            }
        };
        arr[79] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new LogAlert(cast_to_log_alert(a));
            }
        };
        arr[80] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new TorrentLogAlert(cast_to_torrent_log_alert(a));
            }
        };
        arr[81] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PeerLogAlert(cast_to_peer_log_alert(a));
            }
        };
        arr[82] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new LsdErrorAlert(cast_to_lsd_error_alert(a));
            }
        };
        arr[83] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtStatsAlert(cast_to_dht_stats_alert(a));
            }
        };
        arr[84] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new IncomingRequestAlert(cast_to_incoming_request_alert(a));
            }
        };
        arr[85] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtLogAlert(cast_to_dht_log_alert(a));
            }
        };
        arr[86] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtPktAlert(cast_to_dht_pkt_alert(a));
            }
        };
        arr[87] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtGetPeersReplyAlert(cast_to_dht_get_peers_reply_alert(a));
            }
        };
        arr[88] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new DhtDirectResponseAlert(cast_to_dht_direct_response_alert(a));
            }
        };
        arr[89] = new CastLambda() {
            @Override
            public Alert cast(alert a) {
                return new PickerLogAlert(cast_to_picker_log_alert(a));
            }
        };

        return arr;
    }

    private interface CastLambda {
        Alert cast(alert a);
    }
}
