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

    public StatsMetric(stats_metric sm) {
        this.name = sm.getName();
        this.valueIndex = sm.getValue_index();
        this.type = sm.getType();
    }

    public final String name;

    public final int valueIndex;

    public final int type;

    @Override
    public String toString() {
        return name + ":" + valueIndex + ":" + typeString();
    }

    private String typeString() {
        String str = "unknown";

        if (type == TYPE_COUNTER) {
            str = "counter";
        } else if (type == TYPE_GAUGE) {
            str = "gauge";
        }

        return str;
    }
}
