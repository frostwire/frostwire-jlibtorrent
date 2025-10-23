package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * To be used in concert with .
 * <p>
 * The call to {@code SessionManager#postTorrentUpdates()} is done approximately
 * every second. This class holds a time series per metric and a time series for
 * the sampling time.
 *
 * @author gubatron
 * @author aldenml
 * @author haperlot
 */
public final class TorrentStats {

    private final Sha1Hash ih;

    private final int maxSamples;

    private final IntSeries time;
    private final IntSeries downloadRateSeries;
    private final IntSeries uploadRateSeries;

    private long totalDownload;
    private long totalUpload;
    private long totalPayloadDownload;
    private long totalPayloadUpload;
    private long totalDone;
    private long totalWantedDone;
    private long totalWanted;
    private long allTimeUpload;
    private long allTimeDownload;
    private float progress;
    private int progressPpm;
    private int downloadRate;
    private int uploadRate;
    private int downloadPayloadRate;
    private int uploadPayloadRate;
    private int numSeeds;
    private int numPeers;
    private int listSeeds;
    private int listPeers;
    private int numPieces;
    private int numConnections;
    private TorrentStatus.State state;
    private boolean needSaveResume;
    private boolean isPaused;
    private boolean isSequentialDownload;
    private boolean isSeeding;
    private boolean isFinished;

    public TorrentStats(Sha1Hash infoHash, int maxSamples) {
        this.ih = infoHash.clone();

        this.maxSamples = maxSamples;

        this.time = new IntSeries(maxSamples);
        this.downloadRateSeries = new IntSeries(maxSamples);
        this.uploadRateSeries = new IntSeries(maxSamples);
    }

    public int maxSamples() {
        return maxSamples;
    }

    public IntSeries series(SeriesMetric metric) {
        switch (metric) {
            case TIME:
                return time;
            case DOWNLOAD_RATE:
                return downloadRateSeries;
            case UPLOAD_RATE:
                return uploadRateSeries;
            default:
                throw new UnsupportedOperationException("metric type not supported");
        }
    }

    public long last(SeriesMetric metric) {
        return series(metric).last();
    }

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long totalDownload() {
        return totalDownload;
    }

    /**
     * The number of bytes downloaded and uploaded to all peers, accumulated, this session
     * only. The session is considered to restart when a torrent is paused and restarted
     * again. When a torrent is paused, these counters are reset to 0. If you want complete,
     * persistent, stats, see allTimeUpload and allTimeDownload.
     */
    public long totalUpload() {
        return totalUpload;
    }

    /**
     * Counts the amount of bytes received this session, but only
     * the actual payload data (i.e the interesting data), these counters
     * ignore any protocol overhead.
     *
     * @return
     */
    public long totalPayloadDownload() {
        return totalPayloadDownload;
    }

    /**
     * Counts the amount of bytes send this session, but only
     * the actual payload data (i.e the interesting data), these counters
     * ignore any protocol overhead.
     *
     * @return
     */
    public long totalPayloadUpload() {
        return totalPayloadUpload;
    }

    /**
     * The total number of bytes of the file(s) that we have. All this does not necessarily
     * has to be downloaded during this session (that's total_payload_download).
     *
     * @return the value
     */
    public long totalDone() {
        return totalDone;
    }

    /**
     * The number of bytes we have downloaded, only counting the pieces that we actually want
     * to download. i.e. excluding any pieces that we have but have priority 0 (i.e. not wanted).
     */
    public long totalWantedDone() {
        return totalWantedDone;
    }

    /**
     * The total number of bytes we want to download. This may be smaller than the total
     * torrent size in case any pieces are prioritized to 0, i.e. not wanted.
     */
    public long totalWanted() {
        return totalWanted;
    }

    /**
     * This is the accumulated upload payload byte counter. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public long allTimeUpload() {
        return allTimeUpload;
    }

    /**
     * This is the accumulated download payload byte counters. They are saved in and restored
     * from resume data to keep totals across sessions.
     */
    public long allTimeDownload() {
        return allTimeDownload;
    }

    /**
     * A value in the range [0, 1], that represents the progress of the torrent's
     * current task. It may be checking files or downloading.
     *
     * @return the progress in [0, 1]
     */
    public float progress() {
        return progress;
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
        return progressPpm;
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int downloadRate() {
        return downloadRate;
    }

    /**
     * The total rates for all peers for this torrent. These will usually have better
     * precision than summing the rates from all peers. The rates are given as the
     * number of bytes per second.
     */
    public int uploadRate() {
        return uploadRate;
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int downloadPayloadRate() {
        return downloadPayloadRate;
    }

    /**
     * The total transfer rate of payload only, not counting protocol chatter.
     * This might be slightly smaller than the other rates, but if projected over
     * a long time (e.g. when calculating ETA:s) the difference may be noticeable.
     */
    public int uploadPayloadRate() {
        return uploadPayloadRate;
    }

    /**
     * The number of peers that are seeding that this client is currently connected to.
     */
    public int numSeeds() {
        return numSeeds;
    }

    /**
     * The number of peers this torrent currently is connected to. Peer connections that
     * are in the half-open state (is attempting to connect) or are queued for later
     * connection attempt do not count. Although they are visible in the peer list when
     * you call get_peer_info().
     */
    public int numPeers() {
        return numPeers;
    }

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public int listSeeds() {
        return listSeeds;
    }

    /**
     * The number of seeds in our peer list and the total number of peers (including seeds).
     * We are not necessarily connected to all the peers in our peer list. This is the number
     * of peers we know of in total, including banned peers and peers that we have failed to
     * connect to.
     */
    public int listPeers() {
        return listPeers;
    }

    /**
     * Returns the number of pieces that has been downloaded so you don't have
     * to count yourself. This can be used to see if anything has updated
     * since last time if you want to keep a graph of the pieces up to date.
     *
     * @return the number of pieces that has been downloaded
     */
    public int numPieces() {
        return numPieces;
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
        return numConnections;
    }

    /**
     * The main state the torrent is in. See torrent_status::state_t.
     *
     * @return the state
     */
    public TorrentStatus.State state() {
        return state;
    }

    /**
     * true if this torrent has unsaved changes
     * to its download state and statistics since the last resume data
     * was saved.
     *
     * @return
     */
    public boolean needSaveResume() {
        return needSaveResume;
    }

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
        return isPaused;
    }

    /**
     * true when the torrent is in sequential download mode. In this mode
     * pieces are downloaded in order rather than rarest first.
     *
     * @return
     */
    public boolean isSequentialDownload() {
        return isSequentialDownload;
    }

    /**
     * true if all pieces have been downloaded.
     *
     * @return
     */
    public boolean isSeeding() {
        return isSeeding;
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
        return isFinished;
    }

    public void update(TorrentStatus status) {
        if (!ih.equals(status.infoHash())) {
            return; // not for us
        }

        time.add(System.currentTimeMillis());

        torrent_status st = status.swig();

        downloadRateSeries.add(st.getDownload_rate());
        uploadRateSeries.add(st.getUpload_rate());

        totalDownload = st.getTotal_download();
        totalUpload = st.getTotal_upload();
        totalPayloadDownload = st.getTotal_payload_download();
        totalPayloadUpload = st.getTotal_payload_upload();
        totalDone = st.getTotal_done();
        totalWantedDone = st.getTotal_wanted_done();
        totalWanted = st.getTotal_wanted();
        allTimeUpload = st.getAll_time_upload();
        allTimeDownload = st.getAll_time_download();
        progress = st.getProgress();
        progressPpm = st.getProgress_ppm();
        downloadRate = st.getDownload_rate();
        uploadRate = st.getUpload_rate();
        downloadPayloadRate = st.getDownload_payload_rate();
        uploadPayloadRate = st.getUpload_payload_rate();
        numSeeds = st.getNum_seeds();
        numPeers = st.getNum_peers();
        listSeeds = st.getList_seeds();
        listPeers = st.getList_peers();
        numPieces = st.getNum_pieces();
        numConnections = st.getNum_connections();
        state = status.state();
        needSaveResume = st.getNeed_save_resume();
        isPaused = st.getFlags().and_(TorrentFlags.PAUSED).nonZero();
        isSequentialDownload = st.getFlags().and_(TorrentFlags.SEQUENTIAL_DOWNLOAD).nonZero();
        isSeeding = st.getIs_seeding();
        isFinished = st.getIs_finished();
    }

    /**
     * Time-series metrics available in {@code TorrentStats}.
     * <p>
     * {@code SeriesMetric} identifies which time-series data to retrieve from a
     * {@code TorrentStats} instance. Each metric maintains a rolling window of
     * samples collected at regular intervals (approximately every second).
     * <p>
     * <b>Available Metrics:</b>
     * <ul>
     *   <li><b>TIME:</b> Timestamps of each sample (seconds since epoch)</li>
     *   <li><b>DOWNLOAD_RATE:</b> Instantaneous download speed in bytes/second</li>
     *   <li><b>UPLOAD_RATE:</b> Instantaneous upload speed in bytes/second</li>
     * </ul>
     * <p>
     * <b>Accessing Time Series Data:</b>
     * <pre>
     * TorrentStats stats = ...; // From SessionManager or TorrentHandle
     *
     * // Get the download rate series (sliding window of samples)
     * IntSeries downloadRates = stats.series(TorrentStats.SeriesMetric.DOWNLOAD_RATE);
     *
     * // Get most recent sample
     * long latestDownloadRate = downloadRates.last();
     * System.out.println(\"Current speed: \" + latestDownloadRate + \" B/s\");
     *
     * // Get all samples in the window
     * for (int i = 0; i &lt; downloadRates.size(); i++) {
     *     long rate = downloadRates.get(i);
     *     System.out.println(\"Sample \" + i + \": \" + rate + \" B/s\");
     * }
     * </pre>
     * <p>
     * <b>Calculating Statistics from Series:</b>
     * <pre>
     * IntSeries uploadRates = stats.series(TorrentStats.SeriesMetric.UPLOAD_RATE);
     *
     * // Calculate average speed over time window
     * long total = 0;
     * for (int i = 0; i &lt; uploadRates.size(); i++) {
     *     total += uploadRates.get(i);
     * }
     * long avgSpeed = uploadRates.size() > 0 ? total / uploadRates.size() : 0;
     * System.out.println(\"Average upload speed: \" + avgSpeed + \" B/s\");
     *
     * // Get maximum and minimum speeds
     * long maxSpeed = 0, minSpeed = Long.MAX_VALUE;
     * for (int i = 0; i &lt; uploadRates.size(); i++) {
     *     long rate = uploadRates.get(i);
     *     maxSpeed = Math.max(maxSpeed, rate);
     *     minSpeed = Math.min(minSpeed, rate);
     * }
     * System.out.println(\"Max upload: \" + maxSpeed + \" B/s\");
     * System.out.println(\"Min upload: \" + (minSpeed == Long.MAX_VALUE ? 0 : minSpeed) + \" B/s\");
     * </pre>
     * <p>
     * <b>Time Series Window:</b>
     * <p>
     * The series maintains a fixed-size circular buffer (typically 60 samples). Once the
     * buffer fills, new samples overwrite the oldest data. The actual time span depends
     * on the sampling interval and buffer size.
     * <p>
     * <b>Using TIME Metric:</b>
     * <pre>
     * // Correlate speed samples with timestamps
     * IntSeries times = stats.series(TorrentStats.SeriesMetric.TIME);
     * IntSeries downloadRates = stats.series(TorrentStats.SeriesMetric.DOWNLOAD_RATE);
     *
     * for (int i = 0; i &lt; times.size(); i++) {
     *     long timestamp = times.get(i);
     *     long speed = downloadRates.get(i);
     *     System.out.println(\"At \" + new Date(timestamp * 1000) + \": \" + speed + \" B/s\");
     * }
     * </pre>
     * <p>
     * <b>Performance Notes:</b>
     * <ul>
     *   <li>Series data is sampled approximately once per second</li>
     *   <li>Buffer size is fixed; new samples overwrite oldest data</li>
     *   <li>Access is O(1) for random and sequential access</li>
     *   <li>No synchronization; read from same thread as sampling thread if possible</li>
     * </ul>
     */
    public enum SeriesMetric {
        /**
         * Timestamp series - when each sample was collected (UNIX time in seconds).
         */
        TIME,

        /**
         * Download rate series - instantaneous download speed in bytes per second.
         * This is total downloaded data, including protocol overhead.
         */
        DOWNLOAD_RATE,

        /**
         * Upload rate series - instantaneous upload speed in bytes per second.
         * This is total uploaded data, including protocol overhead.
         */
        UPLOAD_RATE
    }
}
