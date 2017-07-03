package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.stats_metric_vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LibTorrent {

    private LibTorrent() {
    }

    /**
     * The version number as reported by libtorrent
     *
     * @return the version number
     */
    public static int versionNum() {
        return libtorrent.LIBTORRENT_VERSION_NUM;
    }

    /**
     * The version string as reported by libtorrent
     *
     * @return the version string
     */
    public static String version() {
        return libtorrent.version();
    }

    /**
     * The git revision of libtorrent the native library is using.
     * <p>
     * This is not the internal revision libtorrent reports, since
     * that string is updated from time to time. This library can be
     * using an up to date revision, this string is manually
     * hardcoded in each version of jlibtorrent. See
     * {@link libtorrent#LIBTORRENT_REVISION} for the libtorrent string.
     *
     * @return the git revision
     */
    public static String revision() {
        return "a1df74a40415689a43f436382a5cc58a48f9a92e";
    }

    public static int boostVersionNum() {
        return libtorrent.boost_version();
    }

    public static String boostVersion() {
        return libtorrent.boost_lib_version();
    }

    public static int opensslVersionNum() {
        return libtorrent.openssl_version_number();
    }

    public static String opensslVersion() {
        return libtorrent.openssl_version_text();
    }

    public static String jlibtorrentVersion() {
        return "1.2.0.11-RC3";
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
    public static List<StatsMetric> sessionStatsMetrics() {
        stats_metric_vector v = libtorrent.session_stats_metrics();
        int size = (int) v.size();

        ArrayList<StatsMetric> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new StatsMetric(v.get(i)));
        }

        return l;
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
