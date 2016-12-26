package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.StatsAlert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_status;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.TorrentAlert;

import java.security.InvalidParameterException;

/**
 * @author gubatron
 * @author aldenml
 * @author haperlot
 */

public final class TorrentStats {

    private final SessionManager sessionManager;
    private final torrent_handle torrentHandle;
    private final long samplingIntervalInMs;
    private final int[] TYPES;
    private final int MAX_SAMPLES;
    private long tStart; //for collecting sampling interval time

    private final Series downloadRateSeries;
    private final Series uploadRateSeries;

    public enum Metric {
        UploadRate,
        DownloadRate
    }

    public TorrentStats(final SessionManager sessionManager, TorrentHandle th, long samplingIntervalInMs, long maxHistoryInMs) {
        this.sessionManager = sessionManager;
        this.torrentHandle = th.swig(); // working with the swig types is for better performance
        this.MAX_SAMPLES = (int) (maxHistoryInMs / samplingIntervalInMs);
        this.downloadRateSeries = new Series(MAX_SAMPLES);
        this.uploadRateSeries = new Series(MAX_SAMPLES);
        this.samplingIntervalInMs = samplingIntervalInMs;
        this.tStart = System.currentTimeMillis();
        this.TYPES = new int[]{AlertType.STATS.swig()};
        startAlertListener();

    }

    /**
     * It will start the alert event listener, as soon as the sampling interval
     * meets the requested time it will save the values on queues. If the queues
     * reach max size denoted by the maxHistory time, they will poll() the head
     * element, in our case the oldest inserted.
     */
    public void startAlertListener() {
        sessionManager.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {

                //if not eq to the torrentHandle return.
                if (!((TorrentAlert<?>) alert).handle().swig().op_eq(torrentHandle)) {
                    return;
                }

                //if !paused, it will take both stats when uploading / downloading
                final torrent_status status = torrentHandle.status();
                if (status != null && !status.getPaused()) {

                    //only true if samplingIntervalInMs
                    if ((System.currentTimeMillis() - tStart) >= samplingIntervalInMs) {
                        tStart = System.currentTimeMillis();

                        //it will keep maxsize since extends form CircularArray
                        downloadRateSeries.add(status.getDownload_rate());
                        uploadRateSeries.add(status.getUpload_rate());

                    }

                }
            }
        });
    }

    /**
     * Will return all available items on the queue, depending on the type selected
     *
     * @param type of metric data to retrieved
     * @return all the elements tracked by the event listener limited by the maxHistory
     * <p>
     * NOTE: since you have to do java acrobatics to create a T[] and native datatypes can't
     * be considered as generics, I'm naming this on purpose like this as a hint for future
     * getFloatSamples, getBooleanSamples, getLongSamples methods.
     */

    public int[] getIntSamples(Metric type) {
        if (type != Metric.DownloadRate && type != Metric.UploadRate) {
            throw new InvalidParameterException("TorrentStats.getIntSamples(" + type.toString() + "). Invalid metric type passed, it is not a metric that tracks int samples.");
        }

        int[] results = new int[MAX_SAMPLES];

        switch (type) {
            case DownloadRate:
                results = downloadRateSeries.getBufferCopy();
                break;
            case UploadRate:
                results = uploadRateSeries.getBufferCopy();
                break;
        }

        return results;
    }

    /**
     * yet to be implemented a new subscription method that has nothing to do with the SessionManager
     *
     * @param metric
     * @return
     */

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

    /**
     * yet to be implemented a new subscription method that has nothing to do with the SessionManager
     *
     * @param alert
     * @return
     */

    public void update(StatsAlert alert) {
        if (!alert.swig().getHandle().op_eq(torrentHandle)) {
            return; // not for us
        }

        if (!torrentHandle.is_valid()) {
            return; // nothing we can do here
        }

        torrent_status st = torrentHandle.status();

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
    }
}