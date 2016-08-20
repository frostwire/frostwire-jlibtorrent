package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.stats_metric;

/**
 * Describes one statistics metric from the session.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StatsMetric {

    public static final String DHT_NODES_GAUGE_NAME = "dht.dht_nodes";
    public static final int DHT_NODES_GAUGE_INDEX = LibTorrent.findMetricIdx(DHT_NODES_GAUGE_NAME);

    public static final int TYPE_COUNTER = stats_metric.type_counter;
    public static final int TYPE_GAUGE = stats_metric.type_gauge;

    StatsMetric(stats_metric sm) {
        this.name = sm.get_name();
        this.valueIndex = sm.getValue_index();
        this.type = sm.getType();
    }

    public final String name;

    public final int valueIndex;

    public final int type;

    @Override
    public String toString() {
        return name + ":" + valueIndex + ":" + typeStr();
    }

    private String typeStr() {
        String str = "unknown";

        if (type == TYPE_COUNTER) {
            str = "counter";
        } else if (type == TYPE_GAUGE) {
            str = "gauge";
        }

        return str;
    }
}
