package com.frostwire.jlibtorrent.alerts;

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
    public long value(int index) {
        return alert.get_value(index);
    }
}
