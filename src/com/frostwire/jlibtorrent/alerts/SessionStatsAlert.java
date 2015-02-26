package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.session_stats_alert;

/**
 * The session_stats_alert is posted when the user requests session statistics by
 * calling {@link com.frostwire.jlibtorrent.Session#postSessionStats()} on the session object. Its category is
 * ``status_notification``, but it is not subject to filtering, since it's only
 * manually posted anyway.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SessionStatsAlert extends AbstractAlert<session_stats_alert> {

    public SessionStatsAlert(session_stats_alert alert) {
        super(alert);
    }

    /**
     * the number of microseconds since the session was
     * started. It represent the time when the snapshot of values was taken. When
     * the network thread is under heavy load, the latency between calling
     * post_session_stats() and receiving this alert may be significant, and
     * the timestamp may help provide higher accuracy in measurements.
     *
     * @return
     */
    public long getStatsTimestamp() {
        return alert.getTimestamp().longValue();
    }

    /**
     * An array are a mix of *counters* and *gauges*, which
     * meanings can be queries via the session_stats_metrics() function on the session.
     * The mapping from a specific metric to an index into this array is constant for a
     * specific version of libtorrent, but may differ for other versions. The intended
     * usage is to request the mapping, i.e. call session_stats_metrics(), once
     * on startup, and then use that mapping to interpret these values throughout
     * the process' runtime.
     *
     * @return
     */
    public long[] getValues() {
        return Vectors.uint64_vector2longs(alert.getValues());
    }
}
