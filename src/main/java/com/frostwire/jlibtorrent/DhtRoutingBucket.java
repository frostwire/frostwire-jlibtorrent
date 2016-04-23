package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_routing_bucket;

/**
 * Hold information about a single DHT routing table bucket
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtRoutingBucket {

    private final dht_routing_bucket t;

    public DhtRoutingBucket(dht_routing_bucket t) {
        this.t = t;
    }

    public dht_routing_bucket swig() {
        return t;
    }

    /**
     * The total number of nodes in the routing table.
     *
     * @return
     */
    public int numNodes() {
        return t.getNum_nodes();
    }

    /**
     * The total number of replacement nodes in the routing table.
     *
     * @return
     */
    public int numReplacements() {
        return t.getNum_replacements();
    }

    /**
     * Number of seconds since last activity.
     *
     * @return
     */
    public int lastActive() {
        return t.getLast_active();
    }
}
