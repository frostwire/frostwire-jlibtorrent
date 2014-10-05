package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.stats_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class StatsAlert extends TorrentAlert<stats_alert> {

    public StatsAlert(stats_alert alert) {
        super(alert);
    }

    /**
     * an array of samples. The enum describes what each sample is a
     * measurement of. All of these are raw, and not smoothing is performed.
     *
     * @return
     */
    public int[] getTransferred() {
        return Vectors.int_vector2ints(alert.transferred_v());
    }

    /**
     * the number of milliseconds during which these stats were collected.
     * This is typically just above 1000, but if CPU is limited, it may be
     * higher than that.
     *
     * @return
     */
    public int getInterval() {
        return alert.getInterval();
    }

    public enum StatsChannel {
        UPLOAD_PAYLOAD(stats_alert.stats_channel.upload_payload.swigValue()),
        UPlOAD_PROTOCOL(stats_alert.stats_channel.upload_protocol.swigValue()),
        DOWNLOAD_PAYLOAD(stats_alert.stats_channel.download_payload.swigValue()),
        DOWNLOAD_PROTOCOL(stats_alert.stats_channel.download_protocol.swigValue()),
        UPLOAD_IP_PROTOCOL(stats_alert.stats_channel.upload_ip_protocol.swigValue()),
        UPLOAD_DHT_PROTOCOL(stats_alert.stats_channel.upload_dht_protocol.swigValue()),
        UPLOAD_TRACKER_PROTOCOL(stats_alert.stats_channel.upload_tracker_protocol.swigValue()),
        DOWNLOAD_IP_PROTOCOL(stats_alert.stats_channel.download_ip_protocol.swigValue()),
        DOWNLOAD_DHT_PROTOCOL(stats_alert.stats_channel.download_dht_protocol.swigValue()),
        DOWNLOAD_TRACKER_PROTOCOL(stats_alert.stats_channel.download_tracker_protocol.swigValue()),
        NUM_CHANNELS(stats_alert.stats_channel.num_channels.swigValue());

        private StatsChannel(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public int getIndex() {
            return swigValue;
        }
    }
}
