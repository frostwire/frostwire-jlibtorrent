package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_routing_bucket;

/**
 * holds dht routing table stats
 *
 * @author gubatron
 * @author aldenml
 */
public final class DHTRoutingBucket {

    private final dht_routing_bucket t;

    public DHTRoutingBucket(dht_routing_bucket t) {
        this.t = t;
    }

    public dht_routing_bucket getSwig() {
        return t;
    }

    /**
     * the total number of nodes in the routing table.
     *
     * @return
     */
    public int getNumNodes() {
        return t.getNum_nodes();
    }

    /**
     * the total number of replacement nodes in the routing table.
     *
     * @return
     */
    public int getNumReplacements() {
        return t.getNum_replacements();
    }

    /**
     * number of seconds since last activity.
     *
     * @return
     */
    public int getLastActive() {
        return t.getLast_active();
    }
}
