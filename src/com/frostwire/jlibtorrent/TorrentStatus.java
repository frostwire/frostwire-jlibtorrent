package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_status;

public final class TorrentStatus {

    TorrentStatus(torrent_status status) {
//    torrent_handle handle;
//    std::string error;
//    std::string save_path;
//    std::string name;
//    boost::intrusive_ptr<const torrent_info> torrent_file;
//    boost::posix_time::time_duration next_announce;
//    boost::posix_time::time_duration announce_interval;
//    std::string current_tracker;
        this.totalDownload = status.getTotal_download();
        this.totalUpload = status.getTotal_upload();
        this.totalPayloadDownload = status.getTotal_payload_download();
        this.totalPayloadUpload = status.getTotal_payload_upload();
        this.totalFailedBytes = status.getTotal_failed_bytes();
        this.totalRedundantBytes = status.getTotal_redundant_bytes();
//    bitfield pieces;
//    bitfield verified_pieces;
//    size_type total_done;
//    size_type total_wanted_done;
//    size_type total_wanted;
//    size_type all_time_upload;
//    size_type all_time_download;
//    time_t added_time;
//    time_t completed_time;
//    time_t last_seen_complete;
//    storage_mode_t storage_mode;
//    float progress;
//    int progress_ppm;
//    int queue_position;
//    int download_rate;
//    int upload_rate;
//    int download_payload_rate;
//    int upload_payload_rate;
//    int num_seeds;
//    int num_peers;
//    int num_complete;
//    int num_incomplete;
//    int list_seeds;
//    int list_peers;
//    int connect_candidates;
//    int num_pieces;
//    int distributed_full_copies;
//    int distributed_fraction;
//    float distributed_copies;
//    int block_size;
//    int num_uploads;
//    int num_connections;
//    int uploads_limit;
//    int connections_limit;
//    int up_bandwidth_queue;
//    int down_bandwidth_queue;
//    int time_since_upload;
//    int time_since_download;
//    int active_time;
//    int finished_time;
//    int seeding_time;
//    int seed_rank;
//    int last_scrape;
//    int sparse_regions;
//    int priority;
        this.state = State.fromSwig(status.getState());
        this.isNeedSaveResume = status.getNeed_save_resume();
        this.isIpFilterApplies = status.getIp_filter_applies();
        this.isUploadMode = status.getUpload_mode();
        this.isShareMode = status.getShare_mode();
        this.isSuperSeeding = status.getSuper_seeding();
        this.isPaused = status.getPaused();
        this.isAutoManaged = status.getAuto_managed();
        this.isSequentialDownload = status.getSequential_download();
        this.isSeeding = status.getIs_seeding();
        this.isFinished = status.getIs_finished();
        this.hasMetadata = status.getHas_metadata();
        this.hasIncoming = status.getHas_incoming();
        this.isSeedMode = status.getSeed_mode();
        this.isMovingStorage = status.getMoving_storage();
//    sha1_hash info_hash;
    }

    //    torrent_handle handle;
//    std::string error;
//    std::string save_path;
//    std::string name;
//    boost::intrusive_ptr<const torrent_info> torrent_file;
//    boost::posix_time::time_duration next_announce;
//    boost::posix_time::time_duration announce_interval;
//    std::string current_tracker;
    public final long totalDownload;
    public final long totalUpload;
    public final long totalPayloadDownload;
    public final long totalPayloadUpload;
    public final long totalFailedBytes;
    public final long totalRedundantBytes;
    //    bitfield pieces;
//    bitfield verified_pieces;
//    size_type total_done;
//    size_type total_wanted_done;
//    size_type total_wanted;
//    size_type all_time_upload;
//    size_type all_time_download;
//    time_t added_time;
//    time_t completed_time;
//    time_t last_seen_complete;
//    storage_mode_t storage_mode;
//    float progress;
//    int progress_ppm;
//    int queue_position;
//    int download_rate;
//    int upload_rate;
//    int download_payload_rate;
//    int upload_payload_rate;
//    int num_seeds;
//    int num_peers;
//    int num_complete;
//    int num_incomplete;
//    int list_seeds;
//    int list_peers;
//    int connect_candidates;
//    int num_pieces;
//    int distributed_full_copies;
//    int distributed_fraction;
//    float distributed_copies;
//    int block_size;
//    int num_uploads;
//    int num_connections;
//    int uploads_limit;
//    int connections_limit;
//    int up_bandwidth_queue;
//    int down_bandwidth_queue;
//    int time_since_upload;
//    int time_since_download;
//    int active_time;
//    int finished_time;
//    int seeding_time;
//    int seed_rank;
//    int last_scrape;
//    int sparse_regions;
//    int priority;
    public final State state;
    public final boolean isNeedSaveResume;
    public final boolean isIpFilterApplies;
    public final boolean isUploadMode;
    public final boolean isShareMode;
    public final boolean isSuperSeeding;
    public final boolean isPaused;
    public final boolean isAutoManaged;
    public final boolean isSequentialDownload;
    public final boolean isSeeding;
    public final boolean isFinished;
    public final boolean hasMetadata;
    public final boolean hasIncoming;
    public final boolean isSeedMode;
    public final boolean isMovingStorage;
//    sha1_hash info_hash;

    public static enum State {
        QUEUED_FOR_CHECKING,
        CHECKING_FILES,
        DOWNLOADING_METADATA,
        DOWNLOADING,
        FINISHED,
        SEEDING,
        ALLOCATING,
        CHECKING_RESUME_DATA;

        public static State fromSwig(torrent_status.state_t state) {
            switch (state) {
                case queued_for_checking:
                    return QUEUED_FOR_CHECKING;
                case checking_files:
                    return CHECKING_FILES;
                case downloading_metadata:
                    return DOWNLOADING_METADATA;
                case downloading:
                    return DOWNLOADING;
                case finished:
                    return FINISHED;
                case seeding:
                    return SEEDING;
                case allocating:
                    return ALLOCATING;
                case checking_resume_data:
                    return CHECKING_RESUME_DATA;
                default:
                    throw new IllegalArgumentException("No enum value supported");
            }
        }
    }
}
