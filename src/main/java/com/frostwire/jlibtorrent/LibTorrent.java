package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.libtorrent_jni;
import com.frostwire.jlibtorrent.swig.stats_metric_vector;

import java.util.ArrayList;
import java.util.List;

/**
 * System information and version utilities for libtorrent and jlibtorrent.
 * <p>
 * {@code LibTorrent} provides access to library version information, dependency versions,
 * platform capabilities, and session metrics. This is useful for logging, debugging,
 * feature detection, and reporting.
 * <p>
 * <b>Version Information:</b>
 * <pre>
 * // Get version info for debugging and logs
 * System.out.println("LibTorrent version: " + LibTorrent.version());
 * System.out.println("LibTorrent version num: " + LibTorrent.versionNum());
 * System.out.println("LibTorrent revision: " + LibTorrent.revision());
 *
 * System.out.println("JLibTorrent version: " + LibTorrent.jlibtorrentVersion());
 *
 * System.out.println("Boost version: " + LibTorrent.boostVersion());
 * System.out.println("OpenSSL version: " + LibTorrent.opensslVersion());
 *
 * // Output example:
 * // LibTorrent version: 2.0.5
 * // LibTorrent version num: 131589
 * // LibTorrent revision: c2012b084c6654d681720ea0693d87a48bc95b14
 * // JLibTorrent version: 2.0.12.7
 * // Boost version: 1_78_0
 * // OpenSSL version: OpenSSL 1.1.1n  15 Mar 2022
 * </pre>
 * <p>
 * <b>Platform Detection and Capabilities:</b>
 * <pre>
 * // Check for ARM NEON support (ARM optimization)
 * if (LibTorrent.hasArmNeonSupport()) {
 *     System.out.println("Running on ARM with NEON support");
 * } else {
 *     System.out.println("Not running ARM NEON platform");
 * }
 * </pre>
 * <p>
 * <b>Session Metrics Discovery:</b>
 * <pre>
 * // Get all available metrics for session statistics
 * List&lt;StatsMetric&gt; metrics = LibTorrent.sessionStatsMetrics();
 *
 * System.out.println("Available session metrics: " + metrics.size());
 * for (StatsMetric metric : metrics) {
 *     System.out.println("  " + metric.name() + " (index " + metric.valueIndex() + ")");
 * }
 *
 * // Example output (first few):
 * // Available session metrics: 150+
 * //   net.sent_payload_bytes (index 0)
 * //   net.recv_payload_bytes (index 1)
 * //   net.sent_bytes (index 2)
 * //   net.recv_bytes (index 3)
 * //   net.read_bytes (index 4)
 * //   ...
 * </pre>
 * <p>
 * <b>Metrics Lookup:</b>
 * <pre>
 * // Find metric index by name
 * int downloadIndex = LibTorrent.findMetricIdx("net.recv_payload_bytes");
 * int uploadIndex = LibTorrent.findMetricIdx("net.sent_payload_bytes");
 *
 * if (downloadIndex >= 0 &amp;&amp; uploadIndex >= 0) {
 *     System.out.println("Found download metric at index: " + downloadIndex);
 *     System.out.println("Found upload metric at index: " + uploadIndex);
 *
 *     // Use these indices with SessionStatsAlert.values()
 *     // to get actual metric values from session statistics
 * } else {
 *     System.out.println("Metric not found");
 * }
 * </pre>
 * <p>
 * <b>Using Metrics with Session Statistics:</b>
 * <pre>
 * // Get session metrics
 * List&lt;StatsMetric&gt; metrics = LibTorrent.sessionStatsMetrics();
 *
 * // Post session stats request
 * sm.postSessionStats();
 *
 * // In your alert listener for SessionStatsAlert:
 * // SessionStatsAlert alert = ...;
 * // long[] values = alert.values();
 *
 * // Look up a metric
 * int idx = LibTorrent.findMetricIdx("net.recv_payload_bytes");
 * if (idx &gt;= 0) {
 *     long bytesReceived = values[idx];
 *     System.out.println("Downloaded: " + bytesReceived + " bytes");
 * }
 * </pre>
 * <p>
 * <b>Typical Use Case - Version Reporting:</b>
 * <pre>
 * // Log library versions on startup
 * String versionInfo = String.format(
 *     "JLibTorrent %s (libtorrent %s, Boost %s, OpenSSL %s)",
 *     LibTorrent.jlibtorrentVersion(),
 *     LibTorrent.version(),
 *     LibTorrent.boostVersion(),
 *     LibTorrent.opensslVersion()
 * );
 * System.out.println(versionInfo);
 *
 * // Report capabilities
 * if (LibTorrent.hasArmNeonSupport()) {
 *     System.out.println("Platform: ARM with NEON optimization");
 * }
 * </pre>
 *
 * @see StatsMetric - For metric structure and information
 * @see SessionManager#postSessionStats() - To request session statistics
 *
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
        return "c2012b084c6654d681720ea0693d87a48bc95b14";
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
        return libtorrent_jni.jlibtorrentVersion();
    }

    /**
     * This free function returns the list of available metrics exposed by
     * libtorrent's statistics API. Each metric has a name and a *value index*.
     * The value index is the index into the array in session_stats_alert where
     * this metric's value can be found when the session stats is sampled (by
     * calling post_session_stats()).
     *
     * @return the list of all metrics
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
     * @param name the name of the metric
     * @return the index of the metric
     */
    public static int findMetricIdx(String name) {
        return libtorrent.find_metric_idx_s(name);
    }

    /**
     * If the native library is an ARM architecture variant, returns true
     * if the running platform has NEON support.
     *
     * @return true if the running platform has NEON support
     */
    public static boolean hasArmNeonSupport() {
        return libtorrent.arm_neon_support();
    }
}
