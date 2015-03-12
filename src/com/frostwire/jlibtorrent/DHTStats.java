package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DHTStats {

    private final RoutingBucket[] buckets;

    private final int totalNodes;

    public DHTStats(DHTRoutingBucket[] buckets) {
        this.buckets = new RoutingBucket[buckets.length];

        int totalNodes = 0;

        for (int i = 0; i < buckets.length; i++) {
            int numNodes = buckets[i].getNumNodes();

            this.buckets[i] = new RoutingBucket(numNodes);
            totalNodes += numNodes;
        }

        this.totalNodes = totalNodes;
    }

    public int totalNodes() {
        return this.totalNodes;
    }

    public static final class RoutingBucket {

        public RoutingBucket(int numNodes) {
            this.numNodes = numNodes;
        }

        public final int numNodes;
    }
}
