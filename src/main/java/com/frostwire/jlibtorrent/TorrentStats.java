package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.StatsAlert;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_status;

/**
 * @author gubatron
 * @author aldenml
 * @author haperlot
 */
public final class TorrentStats {

    private final torrent_handle th;
    private final Series downloadRateSeries;
    private final Series uploadRateSeries;
    private final int MAX_SAMPLES;

    public TorrentStats(TorrentHandle th, long samplingIntervalInMs, long maxHistoryInMs) {
        this.th = th.swig(); // working with the swig types is for better performance
        this.MAX_SAMPLES = (int) (maxHistoryInMs / samplingIntervalInMs);
        this.downloadRateSeries = new Series(MAX_SAMPLES);
        this.uploadRateSeries = new Series(MAX_SAMPLES);
        // TODO: more code here
        throw new UnsupportedOperationException("to be implemented");
    }

    public Series series(SeriesMetric metric) {
        switch (metric) {
            case DOWNLOAD_RATE:
                return downloadRateSeries;
            case UPLOAD_RATE:
                return uploadRateSeries;
            default:
                throw new UnsupportedOperationException("metric type is not supported");
        }
    }

    public void update(StatsAlert alert) {
        if (!alert.swig().getHandle().op_eq(th)) {
            return; // not for us
        }

        if (!th.is_valid()) {
            return; // nothing we can do here
        }

        torrent_status st = th.status();

        if (st.getPaused()) {
            return; // just return for now, yes, quick return
        }

        // TODO: check if we are inside the sampling frequency
        // and assuming we are
        downloadRateSeries.add(st.getDownload_rate());
        uploadRateSeries.add(st.getUpload_rate());
    }

    public enum SeriesMetric {
        DOWNLOAD_RATE,
        UPLOAD_RATE
    }

    public static final class Series extends CircularArray {
        Series(int capacity) {
            super(capacity);
        }
        //return empty queue is empty
        return new int[limit];
    }
}
