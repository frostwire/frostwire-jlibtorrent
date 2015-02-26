package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.stats_metric;

/**
 * Describes one statistics metric from the session.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StatsMetric {

    public static final int TYPE_COUNTER = stats_metric.type_counter;
    public static final int TYPE_GAUGE = stats_metric.type_gauge;

    private final stats_metric sm;

    public StatsMetric(stats_metric sm) {
        this.sm = sm;
    }

    public stats_metric getSwig() {
        return sm;
    }

    public String getName() {
        return sm.getName();
    }

    public int getValueIndex() {
        return sm.getValue_index();
    }

    public int getType() {
        return sm.getType();
    }

    @Override
    public String toString() {
        return sm.getName() + ":" + sm.getValue_index() + ":" + (sm.getType() == TYPE_COUNTER ? "counter" : "gauge");
    }
}
