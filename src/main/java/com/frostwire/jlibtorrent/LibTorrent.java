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

    public static String revision() {
        return libtorrent.LIBTORRENT_REVISION_SHA1;
    }

    /**
     * This number is the previous revision of jlibtorrent, the one
     * before the native JNI code is committed.
     * <p/>
     * It depends heavily in a good release workflow performed by the
     * developers, so take it with care.
     *
     * @return
     */
    public static String jrevision() {
        return libtorrent.JLIBTORRENT_REVISION_SHA1;
    }

    public static String fullVersion() {
        return version() + "-rev-" + revision() + "-jrev-" + jrevision();
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

    /**
     * given a name of a metric, this function returns the counter index of it,
     * or -1 if it could not be found. The counter index is the index into the
     * values array returned by session_stats_alert.
     *
     * @param name
     * @return
     */
    public static int findMetricIdx(String name) {
        return libtorrent.find_metric_idx(name);
    }
}
