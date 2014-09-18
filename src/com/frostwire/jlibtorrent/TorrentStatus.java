/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
//    torrent_handle handle;
//    std::string error;
//    std::string name;
//    boost::intrusive_ptr<const torrent_info> torrent_file;
//    boost::posix_time::time_duration next_announce;
//    boost::posix_time::time_duration announce_interval;
//    std::string current_tracker;
        this.totalDownload = ts.getTotal_download();
        this.totalUpload = ts.getTotal_upload();
        this.totalPayloadDownload = ts.getTotal_payload_download();
        this.totalPayloadUpload = ts.getTotal_payload_upload();
        this.totalFailedBytes = ts.getTotal_failed_bytes();
        this.totalRedundantBytes = ts.getTotal_redundant_bytes();
//    bitfield pieces;
//    bitfield verified_pieces;
        this.totalDone = ts.getTotal_done();
        this.totalWantedDone = ts.getTotal_wanted_done();
        this.totalWanted = ts.getTotal_wanted();
        this.allTimeUpload = ts.getAll_time_upload();
        this.allTimeDownload = ts.getAll_time_download();
        this.addedTime = LibTorrent.time2millis(ts.getAdded_time());
        this.completedTime = LibTorrent.time2millis(ts.getCompleted_time());
        this.lastSeenComplete = LibTorrent.time2millis(ts.getLast_seen_complete());
        this.storage_mode = StorageMode.fromSwig(ts.getStorage_mode());
        this.progress = ts.getProgress();
        this.progressPpm = ts.getProgress_ppm();
        this.queuePosition = ts.getQueue_position();
        this.downloadRate = ts.getDownload_rate();
        this.uploadRate = ts.getUpload_rate();
        this.downloadPayloadRate = ts.getDownload_payload_rate();
        this.uploadPayloadRate = ts.getUpload_payload_rate();
        this.numSeeds = ts.getNum_seeds();
        this.numPeers = ts.getNum_peers();
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
        this.state = State.fromSwig(ts.getState());
        this.isNeedSaveResume = ts.getNeed_save_resume();
        this.isIpFilterApplies = ts.getIp_filter_applies();
        this.isUploadMode = ts.getUpload_mode();
        this.isShareMode = ts.getShare_mode();
        this.isSuperSeeding = ts.getSuper_seeding();
        this.isPaused = ts.getPaused();
        this.isAutoManaged = ts.getAuto_managed();
        this.isSequentialDownload = ts.getSequential_download();
        this.isSeeding = ts.getIs_seeding();
        this.isFinished = ts.getIs_finished();
        this.hasMetadata = ts.getHas_metadata();
        this.hasIncoming = ts.getHas_incoming();
        this.isSeedMode = ts.getSeed_mode();
        this.isMovingStorage = ts.getMoving_storage();
//    sha1_hash info_hash;
    }

    //    torrent_handle handle;
//    std::string error;

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
    public final long totalDownload;

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public final long totalUpload;

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
    public final long totalDone;

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
    public final long addedTime;

    /**
     * The posix-time (in milliseconds) when this torrent was finished. If the torrent is not yet finished, this is 0.
     */
    public final long completedTime;

    /**
     * The time (in milliseconds) when we, or one of our peers, last saw a complete copy of this torrent.
     */
    public final long lastSeenComplete;

    /**
     * The allocation mode for the torrent. See storage_mode_t for the options.
     * For more information, see storage allocation.
     */
    public final StorageMode storage_mode;

    /**
     * A value in the range [0, 1], that represents the progress of the torrent's
     * current task. It may be checking files or downloading.
     *
     * @return
     */
    public final float progress;
    public final int progressPpm;
    public final int queuePosition;

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public final int downloadRate;

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public final int uploadRate;

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public final int downloadPayloadRate;

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public final int uploadPayloadRate;

    /**
     * The number of peers that are seeding that this client is currently connected to.
     */
    public final int numSeeds;

    /**
     * The number of peers this torrent currently is connected to. Peer connections that
     * are in the half-open state (is attempting to connect) or are queued for later
     * connection attempt do not count. Although they are visible in the peer list when
     * you call get_peer_info().
     */
    public final int numPeers;

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
                    throw new IllegalArgumentException("Enum value not supported");
            }
        }
    }
}
