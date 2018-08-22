package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * Holds a snapshot of the status of a torrent, as queried by
 * {@link TorrentHandle#status()}
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentStatus implements Cloneable {

    private final torrent_status ts;

    /**
     * Internal, don't use it in your code.
     *
     * @param ts
     */
    // this is public to make it available to StateUpdateAlert
    public TorrentStatus(torrent_status ts) {
        this.ts = ts;
    }

    /**
     * @return the native object
     */
    public torrent_status swig() {
        return ts;
    }

    /**
     * May be set to an error message describing why the torrent
     * was paused, in case it was paused by an error. If the torrent
     * is not paused or if it's paused but not because of an error,
     * this string is empty.
     *
     * @return the error code
     */
    public ErrorCode errorCode() {
        return new ErrorCode(ts.getErrc());
    }

    /**
     * Returns the name of the torrent. Typically this is derived from the
     * .torrent file. In case the torrent was started without metadata,
     * and hasn't completely received it yet, it returns the name given
     * to it when added to the session. See ``session::add_torrent``.
     * This field is only included if the torrent status is queried
     * with ``torrent_handle::query_name``.
     *
     * @return the name
     */
    public String name() {
        return ts.getName();
    }

    /**
     * The time until the torrent will announce itself to the tracker (in milliseconds).
     *
     * @return the next announce time
     */
    public long nextAnnounce() {
        return ts.get_next_announce();
    }

    /**
     * the URL of the last working tracker. If no tracker request has
     * been successful yet, it's set to an empty string.
     *
     * @return
     */
    public String currentTracker() {
        return ts.getCurrent_tracker();
    }

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long totalDownload() {
        return ts.getTotal_download();
    }

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long totalUpload() {
        return ts.getTotal_upload();
    }

    /**
     * Counts the amount of bytes received this session, but only
     * the actual payload data (i.e the interesting data), these counters
     * ignore any protocol overhead.
     *
     * @return
     */
    public long totalPayloadDownload() {
        return ts.getTotal_payload_download();
    }

    /**
     * Counts the amount of bytes send this session, but only
     * the actual payload data (i.e the interesting data), these counters
     * ignore any protocol overhead.
     *
     * @return
     */
    public long totalPayloadUpload() {
        return ts.getTotal_payload_upload();
    }

    /**
     * The number of bytes that has been downloaded and that has failed the
     * piece hash test. In other words, this is just how much crap that has
     * been downloaded.
     *
     * @return
     */
    public long totalFailedBytes() {
        return ts.getTotal_failed_bytes();
    }

    /**
     * The number of bytes that has been downloaded even though that data
     * already was downloaded. The reason for this is that in some situations
     * the same data can be downloaded by mistake. When libtorrent sends
     * requests to a peer, and the peer doesn't send a response within a
     * certain timeout, libtorrent will re-request that block. Another
     * situation when libtorrent may re-request blocks is when the requests
     * it sends out are not replied in FIFO-order (it will re-request blocks
     * that are skipped by an out of order block). This is supposed to be as
     * low as possible.
     *
     * @return the value
     */
    public long totalRedundantBytes() {
        return ts.getTotal_redundant_bytes();
    }

    /**
     * IMPORTANT: If you are not getting up to date information about pieces
     * remember that PieceIndexBitfield data is considered augmented data
     * for a torrentHandle.status() call, meaning, if you want to get the latest
     * piece data, you must use the TorrentHandle.QUERY_PIECES flag when invoking
     * torrentHandle.status(TorrentHandle.QUERY_PIECES). Keep in mind this is
     * an expensive call, therefore not part of the default flags.
     *
     * A bitmask that represents which pieces we have (set to true) and the
     * pieces we don't have. It's a pointer and may be set to 0 if the
     * torrent isn't downloading or seeding.
     *
     * @return the bitfield of pieces
     */
    public PieceIndexBitfield pieces() {
        return new PieceIndexBitfield(ts.getPieces(), ts);
    }

    /**
     * A bitmask representing which pieces has had their hash checked. This
     * only applies to torrents in *seed mode*. If the torrent is not in seed
     * mode, this bitmask may be empty.
     *
     * @return the bitfield of verified pieces
     */
    public PieceIndexBitfield verifiedPieces() {
        return new PieceIndexBitfield(ts.getVerified_pieces(), ts);
    }

    /**
     * The total number of bytes of the file(s) that we have. All this does not necessarily
     * has to be downloaded during this session (that's total_payload_download).
     *
     * @return the value
     */
    public long totalDone() {
        return ts.getTotal_done();
    }

    /**
     * The total number of bytes to download for this torrent. This
     * may be less than the size of the torrent in case there are
     * pad files. This number only counts bytes that will actually
     * be requested from peers.
     */
    public long total() {
        return ts.getTotal();
    }

    /**
     * The number of bytes we have downloaded, only counting the pieces that we actually want
     * to download. i.e. excluding any pieces that we have but have priority 0 (i.e. not wanted).
     */
    public long totalWantedDone() {
        return ts.getTotal_wanted_done();
    }

    /**
     * The total number of bytes we want to download. This may be smaller than the total
     * torrent size in case any pieces are prioritized to 0, i.e. not wanted.
     */
    public long totalWanted() {
        return ts.getTotal_wanted();
    }

    /**
     * This is the accumulated upload payload byte counter. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public long allTimeUpload() {
        return ts.getAll_time_upload();
    }

    /**
     * This is the accumulated download payload byte counters. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public long allTimeDownload() {
        return ts.getAll_time_download();
    }

    /**
     * The posix-time (in milliseconds) when this torrent was added. i.e. what time(NULL) returned at the time.
     */
    public long addedTime() {
        return time2millis(ts.getAdded_time());
    }

    /**
     * The posix-time (in milliseconds) when this torrent was finished. If the torrent is not yet finished, this is 0.
     */
    public long completedTime() {
        return time2millis(ts.getCompleted_time());
    }

    /**
     * The time (in milliseconds) when we, or one of our peers, last saw a complete copy of this torrent.
     */
    public long lastSeenComplete() {
        return time2millis(ts.getLast_seen_complete());
    }

    /**
     * The allocation mode for the torrent.
     *
     * @see StorageMode
     */
    public final StorageMode storageMode() {
        return StorageMode.fromSwig(ts.getStorage_mode().swigValue());
    }

    /**
     * A value in the range [0, 1], that represents the progress of the torrent's
     * current task. It may be checking files or downloading.
     *
     * @return the progress in [0, 1]
     */
    public float progress() {
        return ts.getProgress();
    }

    /**
     * progress parts per million (progress * 1000000) when disabling
     * floating point operations, this is the only option to query progress
     * <p>
     * reflects the same value as ``progress``, but instead in a range [0,
     * 1000000] (ppm = parts per million). When floating point operations are
     * disabled, this is the only alternative to the floating point value in.
     *
     * @return the progress in parts per million (progress * 1000000)
     */
    public int progressPpm() {
        return ts.getProgress_ppm();
    }

    /**
     * the position this torrent has in the download
     * queue. If the torrent is a seed or finished, this is -1.
     *
     * @return
     */
    public int queuePosition() {
        return ts.get_queue_position();
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int downloadRate() {
        return ts.getDownload_rate();
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int uploadRate() {
        return ts.getUpload_rate();
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int downloadPayloadRate() {
        return ts.getDownload_payload_rate();
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int uploadPayloadRate() {
        return ts.getUpload_payload_rate();
    }

    /**
     * The number of peers that are seeding that this client is currently connected to.
     */
    public int numSeeds() {
        return ts.getNum_seeds();
    }

    /**
     * The number of peers this torrent currently is connected to. Peer connections that
     * are in the half-open state (is attempting to connect) or are queued for later
     * connection attempt do not count. Although they are visible in the peer list when
     * you call get_peer_info().
     */
    public int numPeers() {
        return ts.getNum_peers();
    }

    /**
     * If the tracker sends scrape info in its announce reply, these fields
     * will be set to the total number of peers that have the whole file and
     * the total number of peers that are still downloading. set to -1 if the
     * tracker did not send any scrape data in its announce reply.
     *
     * @return
     */
    public int numComplete() {
        return ts.getNum_complete();
    }

    /**
     * If the tracker sends scrape info in its announce reply, these fields
     * will be set to the total number of peers that have the whole file and
     * the total number of peers that are still downloading. set to -1 if the
     * tracker did not send any scrape data in its announce reply.
     *
     * @return
     */
    public int numIncomplete() {
        return ts.getNum_incomplete();
    }

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public int listSeeds() {
        return ts.getList_seeds();
    }

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public int listPeers() {
        return ts.getList_peers();
    }

    /**
     * the number of peers in this torrent's peer list that is a candidate to
     * be connected to. i.e. It has fewer connect attempts than the max fail
     * count, it is not a seed if we are a seed, it is not banned etc. If
     * this is 0, it means we don't know of any more peers that we can try.
     *
     * @return
     */
    public int connectCandidates() {
        return ts.getConnect_candidates();
    }

    /**
     * Returns the number of pieces that has been downloaded so you don't have
     * to count yourself. This can be used to see if anything has updated
     * since last time if you want to keep a graph of the pieces up to date.
     *
     * @return the number of pieces that has been downloaded
     */
    public int numPieces() {
        return ts.getNum_pieces();
    }

    /**
     * the number of distributed copies of the torrent. Note that one copy
     * may be spread out among many peers. It tells how many copies there are
     * currently of the rarest piece(s) among the peers this client is
     * connected to.
     *
     * @return
     */
    public int distributedFullCopies() {
        return ts.getDistributed_full_copies();
    }

    /**
     * tells the share of pieces that have more copies than the rarest
     * // piece(s). Divide this number by 1000 to get the fraction.
     * //
     * // For example, if ``distributed_full_copies`` is 2 and
     * // ``distributed_fraction`` is 500, it means that the rarest pieces have
     * // only 2 copies among the peers this torrent is connected to, and that
     * // 50% of all the pieces have more than two copies.
     * //
     * // If we are a seed, the piece picker is deallocated as an optimization,
     * // and piece availability is no longer tracked. In this case the
     * // distributed copies members are set to -1.
     *
     * @return
     */
    public int distributedFraction() {
        return ts.getDistributed_fraction();
    }

    /**
     * the number of distributed copies of the file. note that one copy may
     * be spread out among many peers. This is a floating point
     * representation of the distributed copies.
     * <p>
     * the integer part tells how many copies
     * there are of the rarest piece(s)
     * <p>
     * the fractional part tells the fraction of pieces that
     * have more copies than the rarest piece(s).
     *
     * @return
     */
    public float distributedCopies() {
        return ts.getDistributed_copies();
    }

    /**
     * the size of a block, in bytes. A block is a sub piece, it is the
     * number of bytes that each piece request asks for and the number of
     * bytes that each bit in the ``partial_piece_info``'s bitset represents,
     * see get_download_queue(). This is typically 16 kB, but it may be
     * larger if the pieces are larger.
     *
     * @return
     */
    public int blockSize() {
        return ts.getBlock_size();
    }

    /**
     * the number of unchoked peers in this torrent.
     *
     * @return
     */
    public int numUploads() {
        return ts.getNum_uploads();
    }

    /**
     * Returns the number of peer connections this torrent has,
     * including half-open connections that hasn't completed the
     * bittorrent handshake yet.
     * <p>
     * This is always {@code >= num_peers}.
     *
     * @return the number of peer connections
     */
    public int numConnections() {
        return ts.getNum_connections();
    }

    /**
     * get limit of upload slots (unchoked peers) for this torrent.
     *
     * @return
     */
    public int uploadsLimit() {
        return ts.getUploads_limit();
    }

    /**
     * get limit of number of connections for this torrent.
     *
     * @return
     */
    public int connectionsLimit() {
        return ts.getConnections_limit();
    }

    /**
     * the number of peers in this torrent that are waiting for more bandwidth quota from the torrent rate limiter.
     * This can determine if the rate you get from this torrent is bound by the torrents limit or not.
     * If there is no limit set on this torrent, the peers might still be waiting for bandwidth quota from the global limiter,
     * but then they are counted in the ``session_status`` object.
     *
     * @return
     */
    public int upBandwidthQueue() {
        return ts.getUp_bandwidth_queue();
    }

    /**
     * the number of peers in this torrent that are waiting for more bandwidth quota from the torrent rate limiter.
     * This can determine if the rate you get from this torrent is bound by the torrents limit or not.
     * If there is no limit set on this torrent, the peers might still be waiting for bandwidth quota from the global limiter,
     * but then they are counted in the ``session_status`` object.
     *
     * @return
     */
    public int downBandwidthQueue() {
        return ts.getDown_bandwidth_queue();
    }

    /**
     * A rank of how important it is to seed the torrent, it is used to determine which torrents to seed and which to queue.
     * It is based on the peer to seed ratio from the tracker scrape. Higher value means more important to seed.
     *
     * @return the seed rank
     */
    public int seedRank() {
        return ts.getSeed_rank();
    }

    /**
     * The main state the torrent is in. See torrent_status::state_t.
     *
     * @return the state
     */
    public State state() {
        return State.fromSwig(ts.getState().swigValue());
    }

    /**
     * true if this torrent has unsaved changes
     * to its download state and statistics since the last resume data
     * was saved.
     *
     * @return
     */
    public boolean needSaveResume() {
        return ts.getNeed_save_resume();
    }

    /**
     * true if all pieces have been downloaded.
     *
     * @return
     */
    public boolean isSeeding() {
        return ts.getIs_seeding();
    }

    /**
     * Returns {@code true} if all pieces that have a
     * {@code priority > 0} are downloaded. There is
     * only a distinction between finished and seeding if some pieces or
     * files have been set to priority 0, i.e. are not downloaded.
     *
     * @return {@code true} if all pieces that have a {@code priority > 0} are downloaded.
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
     * this is true if this torrent's storage is currently being moved from
     * one location to another. This may potentially be a long operation
     * if a large file ends up being copied from one drive to another.
     *
     * @return
     */
    public boolean isMovingStorage() {
        return ts.getMoving_storage();
    }

    /**
     * These are set to true if this torrent is allowed to announce to the
     * respective peer source. Whether they are true or false is determined by
     * the queue logic/auto manager. Torrents that are not auto managed will
     * always be allowed to announce to all peer sources.
     *
     * @return
     */
    public boolean announcingToTrackers() {
        return ts.getAnnouncing_to_trackers();
    }

    /**
     * These are set to true if this torrent is allowed to announce to the
     * respective peer source. Whether they are true or false is determined by
     * the queue logic/auto manager. Torrents that are not auto managed will
     * always be allowed to announce to all peer sources.
     *
     * @return
     */
    public boolean announcingToLsd() {
        return ts.getAnnouncing_to_lsd();
    }

    /**
     * These are set to true if this torrent is allowed to announce to the
     * respective peer source. Whether they are true or false is determined by
     * the queue logic/auto manager. Torrents that are not auto managed will
     * always be allowed to announce to all peer sources.
     *
     * @return
     */
    public boolean announcingToDht() {
        return ts.getAnnouncing_to_dht();
    }

    /**
     * the info-hash for this torrent.
     *
     * @return
     */
    public Sha1Hash infoHash() {
        return new Sha1Hash(ts.getInfo_hash());
    }

    public long lastUpload() {
        return ts.get_last_upload();
    }

    public long lastDownload() {
        return ts.get_last_download();
    }

    public long activeDuration() {
        return ts.get_active_duration();
    }

    public long finishedDuration() {
        return ts.get_finished_duration();
    }

    public long seedingDuration() {
        return ts.get_seeding_duration();
    }

    public torrent_flags_t flags() {
        return ts.getFlags();
    }

    @Override
    protected TorrentStatus clone() {
        return new TorrentStatus(new torrent_status(ts));
    }

    private static long time2millis(long time) {
        return time * 1000;
    }

    /**
     * the different overall states a torrent can be in.
     */
    public enum State {

        /**
         * The torrent has not started its download yet, and is
         * currently checking existing files.
         */
        CHECKING_FILES(torrent_status.state_t.checking_files.swigValue()),

        /**
         * The torrent is trying to download metadata from peers.
         * This assumes the metadata_transfer extension is in use.
         */
        DOWNLOADING_METADATA(torrent_status.state_t.downloading_metadata.swigValue()),

        /**
         * The torrent is being downloaded. This is the state
         * most torrents will be in most of the time. The progress
         * meter will tell how much of the files that has been
         * downloaded.
         */
        DOWNLOADING(torrent_status.state_t.downloading.swigValue()),

        /**
         * In this state the torrent has finished downloading but
         * still doesn't have the entire torrent. i.e. some pieces
         * are filtered and won't get downloaded.
         */
        FINISHED(torrent_status.state_t.finished.swigValue()),

        /**
         * In this state the torrent has finished downloading and
         * is a pure seeder.
         */
        SEEDING(torrent_status.state_t.seeding.swigValue()),

        /**
         * If the torrent was started in full allocation mode, this
         * indicates that the (disk) storage for the torrent is
         * allocated.
         */
        ALLOCATING(torrent_status.state_t.allocating.swigValue()),

        /**
         * The torrent is currently checking the fastresume data and
         * comparing it to the files on disk. This is typically
         * completed in a fraction of a second, but if you add a
         * large number of torrents at once, they will queue up.
         */
        CHECKING_RESUME_DATA(torrent_status.state_t.checking_resume_data.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        State(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the native value
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue
         * @return
         */
        public static State fromSwig(int swigValue) {
            State[] enumValues = State.class.getEnumConstants();
            for (State ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
