package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.stats_alert;

/**
 * This alert is posted approximately once every second, and it contains
 * byte counters of most statistics that's tracked for torrents. Each active
 * torrent posts these alerts regularly.
 * <p>
 * This alert will be deprecated soon.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StatsAlert extends TorrentAlert<stats_alert> {

    StatsAlert(stats_alert alert) {
        super(alert);
    }

    // All of these are raw, and not smoothing is performed.
    // See {@link StatsChannel} for possible values of index.
    public int transferred(int index) {
        return alert.get_transferred(index);
    }

    /**
     * The number of milliseconds during which these stats were collected.
     * This is typically just above 1000, but if CPU is limited, it may be
     * higher than that.
     *
     * @return
     */
    public int interval() {
        return alert.getInterval();
    }

    /**
     *
     */
    public enum StatsChannel {

        /**
         *
         */
        UPLOAD_PAYLOAD(stats_alert.stats_channel.upload_payload.swigValue()),

        /**
         *
         */
        UPlOAD_PROTOCOL(stats_alert.stats_channel.upload_protocol.swigValue()),

        /**
         *
         */
        DOWNLOAD_PAYLOAD(stats_alert.stats_channel.download_payload.swigValue()),

        /**
         *
         */
        DOWNLOAD_PROTOCOL(stats_alert.stats_channel.download_protocol.swigValue()),

        /**
         *
         */
        UPLOAD_IP_PROTOCOL(stats_alert.stats_channel.upload_ip_protocol.swigValue()),

        /**
         *
         */
        DOWNLOAD_IP_PROTOCOL(stats_alert.stats_channel.download_ip_protocol.swigValue()),

        /**
         *
         */
        NUM_CHANNELS(stats_alert.stats_channel.num_channels.swigValue());

        StatsChannel(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @return
         */
        public int getIndex() {
            return swigValue;
        }
    }
}
