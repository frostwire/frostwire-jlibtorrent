package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.stats_metric_vector;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LibTorrent {

    private LibTorrent() {
    }

    public static String version() {
        return libtorrent.LIBTORRENT_VERSION;
    }

    /**
     * returns the string in hexadecimal representation using the internal libtorrent to_hex
     * function.
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {
        return libtorrent.to_hex(Vectors.bytes2char_vector(data));
    }

    /**
     * This free function returns the list of available metrics exposed by
     * libtorrent's statistics API. Each metric has a name and a *value index*.
     * The value index is the index into the array in session_stats_alert where
     * this metric's value can be found when the session stats is sampled (by
     * calling post_session_stats()).
     *
     * @return
     */
    public static StatsMetric[] sessionStatsMetrics() {
        stats_metric_vector v = libtorrent.session_stats_metrics();

        int size = (int) v.size();
        StatsMetric[] arr = new StatsMetric[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new StatsMetric(v.get(i));
        }

        return arr;
    }
}
