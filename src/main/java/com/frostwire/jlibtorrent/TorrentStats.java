package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * To be used in concert with {@link SessionManager}.
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
    private final IntSeries downloadRate;
    private final IntSeries uploadRate;

    public TorrentStats(Sha1Hash infoHash, int maxSamples) {
        this.ih = infoHash.clone();

        this.maxSamples = maxSamples;

        this.time = new IntSeries(maxSamples);
        this.downloadRate = new IntSeries(maxSamples);
        this.uploadRate = new IntSeries(maxSamples);
    }

    public int maxSamples() {
        return maxSamples;
    }

    public IntSeries series(SeriesMetric metric) {
        switch (metric) {
            case TIME:
                return time;
            case DOWNLOAD_RATE:
                return downloadRate;
            case UPLOAD_RATE:
                return uploadRate;
            default:
                throw new UnsupportedOperationException("metric type not supported");
        }
    }

    public long last(SeriesMetric metric) {
        return series(metric).last();
    }

    public void update(TorrentStatus status) {
        if (!ih.equals(status.infoHash())) {
            return; // not for us
        }

        time.add(System.currentTimeMillis());

        torrent_status st = status.swig();

        downloadRate.add(st.getDownload_rate());
        uploadRate.add(st.getUpload_rate());
    }

    public enum SeriesMetric {
        TIME,
        DOWNLOAD_RATE,
        UPLOAD_RATE
    }
}
