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

    public TorrentStats(TorrentHandle th) {
        this.th = th.swig(); // working with the swig types is for better performance
        this.downloadRateSeries = new Series();
        this.uploadRateSeries = new Series();

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

    // TODO: to be implemented with a circular array
    // to avoid expensive boxing/unboxing of primitive types
    // the implementation should be backed by an array to allow
    // for compactness of memory. All the arrays can be pooled
    // from a single matrix to improve data locality at the time
    // of bulk insertion.
    // you can see that this class is immutable from the outside.
    public static final class Series {

        Series() {
            throw new UnsupportedOperationException("to be implemented");
        }

        /**
         * Adds a sample to the series.
         *
         * @param value sample's value
         */
        private void add(int value) {
            throw new UnsupportedOperationException("to be implemented");
        }

        /**
         * Returns the sample at index {@code index}.
         *
         * @param index the sample's index
         * @return the sample's value
         */
        public int get(int index) {
            throw new UnsupportedOperationException("to be implemented");
        }

        /**
         * Returns a tail based sub-sample of the series or the entire
         * series if count is bigger than {@link #size()}.
         * <p>
         * This is a potentially expensive memory operation. If memory
         * is a concern, consider iterating with an index.
         *
         * @param count the count of the desired sub-sample
         * @return array with values
         */
        public int[] tail(int count) {
            throw new UnsupportedOperationException("to be implemented");
        }

        /**
         * The amount of samples stored in the series.
         *
         * @return number of samples
         */
        public int size() {
            throw new UnsupportedOperationException("to be implemented");
        }
    }
}
