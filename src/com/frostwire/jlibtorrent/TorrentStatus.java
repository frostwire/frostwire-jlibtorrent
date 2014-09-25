package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentStatus {

    private final torrent_status ts;

    TorrentStatus(torrent_status ts) {
        this.ts = ts;
//    std::string name;
//    boost::intrusive_ptr<const torrent_info> torrent_file;
//    boost::posix_time::time_duration next_announce;
//    boost::posix_time::time_duration announce_interval;
//    std::string current_tracker;
        this.totalPayloadDownload = ts.getTotal_payload_download();
        this.totalPayloadUpload = ts.getTotal_payload_upload();
        this.totalFailedBytes = ts.getTotal_failed_bytes();
        this.totalRedundantBytes = ts.getTotal_redundant_bytes();
//    bitfield pieces;
//    bitfield verified_pieces;
        this.totalWantedDone = ts.getTotal_wanted_done();
        this.totalWanted = ts.getTotal_wanted();
        this.allTimeUpload = ts.getAll_time_upload();
        this.allTimeDownload = ts.getAll_time_download();
        this.numComplete = ts.getNum_complete();
        this.numIncomplete = ts.getNum_incomplete();
        this.listSeeds = ts.getList_seeds();
        this.listPeers = ts.getList_peers();
        this.connectCandidates = ts.getConnect_candidates();
        this.numPieces = ts.getNum_pieces();
        this.distributedFullCopies = ts.getDistributed_full_copies();
        this.distributedFraction = ts.getDistributed_fraction();
        this.distributedCopies = ts.getDistributed_copies();
        this.blockSize = ts.getBlock_size();
        this.numUploads = ts.getNum_uploads();
        this.numConnections = ts.getNum_connections();
        this.uploadsLimit = ts.getUploads_limit();
        this.connectionsLimit = ts.getConnections_limit();
        this.upBandwidthQueue = ts.getUp_bandwidth_queue();
        this.downBandwidthQueue = ts.getDown_bandwidth_queue();
        this.timeSinceUpload = ts.getTime_since_upload();
        this.timeSinceDownload = ts.getTime_since_download();
        this.activeTime = ts.getActive_time();
        this.finishedTime = ts.getFinished_time();
        this.seedingTime = ts.getSeeding_time();
        this.seedRank = ts.getSeed_rank();
        this.lastScrape = ts.getLast_scrape();
        this.sparseRegions = ts.getSparse_regions();
        this.priority = ts.getPriority();
        this.isNeedSaveResume = ts.getNeed_save_resume();
        this.isIpFilterApplies = ts.getIp_filter_applies();
        this.isUploadMode = ts.getUpload_mode();
        this.isShareMode = ts.getShare_mode();
        this.isSuperSeeding = ts.getSuper_seeding();
        this.isAutoManaged = ts.getAuto_managed();
        this.isSequentialDownload = ts.getSequential_download();
    }

    // a handle to the torrent whose status the object represents.
    public TorrentHandle getHandle() {
        return new TorrentHandle(ts.getHandle());
    }

    // may be set to an error message describing why the torrent
    // was paused, in case it was paused by an error. If the torrent
    // is not paused or if it's paused but not because of an error,
    // this string is empty.
    public String getError() {
        return ts.getError();
    }

//    std::string name;
//    boost::intrusive_ptr<const torrent_info> torrent_file;
//    boost::posix_time::time_duration next_announce;
//    boost::posix_time::time_duration announce_interval;
//    std::string current_tracker;

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long getTotalDownload() {
        return ts.getTotal_download();
    }

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long getTotalUpload() {
        return ts.getTotal_upload();
    }

    public final long totalPayloadDownload;
    public final long totalPayloadUpload;
    public final long totalFailedBytes;
    public final long totalRedundantBytes;
    //    bitfield pieces;
//    bitfield verified_pieces;

    /**
     * The total number of bytes of the file(s) that we have. All this does not necessarily
     * has to be downloaded during this session (that's total_payload_download).
     */
    public long getTotalDone() {
        return ts.getTotal_done();
    }

    /**
     * The number of bytes we have downloaded, only counting the pieces that we actually want
     * to download. i.e. excluding any pieces that we have but have priority 0 (i.e. not wanted).
     */
    public final long totalWantedDone;

    /**
     * The total number of bytes we want to download. This may be smaller than the total
     * torrent size in case any pieces are prioritized to 0, i.e. not wanted.
     */
    public final long totalWanted;

    /**
     * This is the accumulated upload payload byte counter. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public final long allTimeUpload;

    /**
     * This is the accumulated download payload byte counters. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public final long allTimeDownload;

    /**
     * The posix-time (in milliseconds) when this torrent was added. i.e. what time(NULL) returned at the time.
     */
    public long getAddedTime() {
        return time2millis(ts.getAdded_time());
    }

    /**
     * The posix-time (in milliseconds) when this torrent was finished. If the torrent is not yet finished, this is 0.
     */
    public long getCompletedTime() {
        return time2millis(ts.getCompleted_time());
    }

    /**
     * The time (in milliseconds) when we, or one of our peers, last saw a complete copy of this torrent.
     */
    public long lastSeenComplete() {
        return time2millis(ts.getLast_seen_complete());
    }

    /**
     * The allocation mode for the torrent. See storage_mode_t for the options.
     * For more information, see storage allocation.
     */
    public final StorageMode getStorageMode() {
        return StorageMode.fromSwig(ts.getStorage_mode());
    }

    /**
     * A value in the range [0, 1], that represents the progress of the torrent's
     * current task. It may be checking files or downloading.
     *
     * @return
     */
    public float getProgress() {
        return ts.getProgress();
    }

    // progress parts per million (progress * 1000000) when disabling
    // floating point operations, this is the only option to query progress

    // reflects the same value as ``progress``, but instead in a range [0,
    // 1000000] (ppm = parts per million). When floating point operations are
    // disabled, this is the only alternative to the floating point value in
    public int getProgressPpm() {
        return ts.getProgress_ppm();
    }

    // the position this torrent has in the download
    // queue. If the torrent is a seed or finished, this is -1.
    public int getQueuePosition() {
        return ts.getQueue_position();
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int getDownloadRate() {
        return ts.getDownload_rate();
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int getUploadRate() {
        return ts.getUpload_rate();
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int getDownloadPayloadRate() {
        return ts.getDownload_payload_rate();
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int getUploadPayloadRate() {
        return ts.getUpload_payload_rate();
    }

    /**
     * The number of peers that are seeding that this client is currently connected to.
     */
    public int getNumSeeds() {
        return ts.getNum_seeds();
    }

    /**
     * The number of peers this torrent currently is connected to. Peer connections that
     * are in the half-open state (is attempting to connect) or are queued for later
     * connection attempt do not count. Although they are visible in the peer list when
     * you call get_peer_info().
     */
    public int getNumPeers() {
        return ts.getNum_peers();
    }

    public final int numComplete;
    public final int numIncomplete;

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public final int listSeeds;

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public final int listPeers;

    public final int connectCandidates;
    public final int numPieces;
    public final int distributedFullCopies;
    public final int distributedFraction;
    public final float distributedCopies;
    public final int blockSize;
    public final int numUploads;
    public final int numConnections;
    public final int uploadsLimit;
    public final int connectionsLimit;
    public final int upBandwidthQueue;
    public final int downBandwidthQueue;
    public final int timeSinceUpload;
    public final int timeSinceDownload;
    public final int activeTime;
    public final int finishedTime;
    public final int seedingTime;
    public final int seedRank;
    public final int lastScrape;
    public final int sparseRegions;
    public final int priority;

    /**
     * The main state the torrent is in. See torrent_status::state_t.
     */
    public State getState() {
        return State.fromSwig(ts.getState());
    }

    public final boolean isNeedSaveResume;
    public final boolean isIpFilterApplies;
    public final boolean isUploadMode;
    public final boolean isShareMode;
    public final boolean isSuperSeeding;

    /**
     * set to true if the torrent is paused and false otherwise. It's only
     * true if the torrent itself is paused. If the torrent is not running
     * because the session is paused, this is still false. To know if a
     * torrent is active or not, you need to inspect both
     * ``torrent_status::paused`` and ``session::is_paused()``.
     *
     * @return
     */
    public boolean isPaused() {
        return ts.getPaused();
    }

    public final boolean isAutoManaged;
    public final boolean isSequentialDownload;

    /**
     * true if all pieces have been downloaded.
     *
     * @return
     */
    public boolean isSeeding() {
        return ts.getIs_seeding();
    }

    /**
     * true if all pieces that have a priority > 0 are downloaded. There is
     * only a distinction between finished and seeding if some pieces or
     * files have been set to priority 0, i.e. are not downloaded.
     *
     * @return
     */
    public boolean isFinished() {
        return ts.getIs_finished();
    }

    /**
     * true if this torrent has metadata (either it was started from a
     * .torrent file or the metadata has been downloaded). The only scenario
     * where this can be false is when the torrent was started torrent-less
     * (i.e. with just an info-hash and tracker ip, a magnet link for
     * instance).
     *
     * @return
     */
    public boolean hasMetadata() {
        return ts.getHas_metadata();
    }

    /**
     * true if there has ever been an incoming connection attempt to this
     * torrent.
     *
     * @return
     */
    public boolean hasIncoming() {
        return ts.getHas_incoming();
    }

    /**
     * true if the torrent is in seed_mode. If the torrent was started in
     * seed mode, it will leave seed mode once all pieces have been checked
     * or as soon as one piece fails the hash check.
     *
     * @return
     */
    public boolean isSeedMode() {
        return ts.getSeed_mode();
    }

    /**
     * this is true if this torrent's storage is currently being moved from
     * one location to another. This may potentially be a long operation
     * if a large file ends up being copied from one drive to another.
     *
     * @return
     */
    public boolean isMovingStorage() {
        return ts.getMoving_storage();
    }

    public Sha1Hash getInfoHash() {
        return new Sha1Hash(ts.getInfo_hash());
    }

    private static long time2millis(int time) {
        return ((long) time) * 1000;
    }

    public static enum State {

        QUEUED_FOR_CHECKING(torrent_status.state_t.queued_for_checking),
        CHECKING_FILES(torrent_status.state_t.checking_files),
        DOWNLOADING_METADATA(torrent_status.state_t.downloading_metadata),
        DOWNLOADING(torrent_status.state_t.downloading),
        FINISHED(torrent_status.state_t.finished),
        SEEDING(torrent_status.state_t.seeding),
        ALLOCATING(torrent_status.state_t.allocating),
        CHECKING_RESUME_DATA(torrent_status.state_t.checking_resume_data);

        private State(torrent_status.state_t swigObj) {
            this.swigObj = swigObj;
        }

        private final torrent_status.state_t swigObj;

        public torrent_status.state_t getSwig() {
            return swigObj;
        }

        public static State fromSwig(torrent_status.state_t swigObj) {
            State[] enumValues = State.class.getEnumConstants();
            for (State ev : enumValues) {
                if (ev.getSwig() == swigObj) {
                    return ev;
                }
            }
            throw new IllegalArgumentException("No enum " + State.class + " with swig value " + swigObj);
        }
    }
}
