package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_routing_bucket;

/**
 * Statistics for a single bucket in the DHT routing table.
 * <p>
 * {@code DhtRoutingBucket} provides metrics about a single routing table bucket in the
 * local Kademlia DHT implementation. The DHT routing table organizes known nodes into
 * buckets based on distance from your own node ID. Each bucket contains active nodes
 * and replacement candidates.
 * <p>
 * <b>Understanding DHT Routing Buckets:</b>
 * <br/>
 * In Kademlia DHT, the routing table divides the node ID space into buckets:
 * <ul>
 *   <li><b>Total Buckets:</b> 160 buckets (one for each bit in SHA-1 hash)</li>
 *   <li><b>Bucket Size:</b> Typically 20 nodes (k-value)</li>
 *   <li><b>Purpose:</b> Organize known peers by their distance from your node ID</li>
 *   <li><b>Strategy:</b> Maintain nodes that are closer to you for efficient lookups</li>
 * </ul>
 * <p>
 * <b>Bucket Organization:</b>
 * <pre>
 * Closer to me                        Farther from me
 * [Bucket 0] [Bucket 1] ... [Bucket 159]
 *   (20 nodes) (20 nodes)   (20 nodes)
 *
 * When you need to find something:
 * - Start with closest bucket
 * - Query nodes in that bucket
 * - They tell you about closer nodes
 * - Move to next bucket and repeat
 * - Eventually narrow down to target
 * </pre>
 * <p>
 * <b>Monitoring DHT Routing Table:</b>
 * <pre>
 * // Get routing buckets from DHT stats alert
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.DHT_STATS.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         DhtStatsAlert a = (DhtStatsAlert) alert;
 *         List&lt;DhtRoutingBucket&gt; buckets = a.routingBuckets();
 *
 *         System.out.println("Total buckets: " + buckets.size());
 *         int totalNodes = 0;
 *
 *         for (DhtRoutingBucket bucket : buckets) {
 *             int nodes = bucket.numNodes();
 *             totalNodes += nodes;
 *
 *             if (nodes &gt; 0) {
 *                 System.out.println("Bucket: " + nodes + " nodes, " +
 *                     bucket.numReplacements() + " replacements, " +
 *                     "last active " + bucket.lastActive() + "s ago");
 *             }
 *         }
 *
 *         System.out.println("Total nodes in routing table: " + totalNodes);
 *     }
 * });
 * </pre>
 * <p>
 * <b>Bucket Metrics Interpretation:</b>
 * <pre>
 * DhtRoutingBucket bucket = ...;
 *
 * // Active nodes in this bucket
 * int numNodes = bucket.numNodes();
 * System.out.println("Active nodes: " + numNodes);
 *
 * // Replacement nodes (backup candidates)
 * // Used when an active node becomes unresponsive
 * int replacements = bucket.numReplacements();
 * System.out.println("Replacement candidates: " + replacements);
 *
 * // How long since this bucket was used
 * int lastActive = bucket.lastActive();
 * System.out.println("Last active: " + lastActive + " seconds ago");
 *
 * // Bucket health assessment
 * if (numNodes &lt; 5) {
 *     System.out.println("Warning: Few nodes in this bucket");
 * }
 *
 * if (replacements == 0) {
 *     System.out.println("Warning: No replacement candidates");
 * }
 *
 * if (lastActive &gt; 3600) {
 *     System.out.println("Warning: This bucket hasn't been used in over an hour");
 * }
 * </pre>
 * <p>
 * <b>Analyzing Routing Table Health:</b>
 * <pre>
 * List&lt;DhtRoutingBucket&gt; buckets = dhtStatsAlert.routingBuckets();
 *
 * // Count routing table status
 * int totalNodes = 0;
 * int totalReplacements = 0;
 * int activeBuckets = 0;
 *
 * for (DhtRoutingBucket bucket : buckets) {
 *     int nodes = bucket.numNodes();
 *     if (nodes &gt; 0) {
 *         totalNodes += nodes;
 *         totalReplacements += bucket.numReplacements();
 *         activeBuckets++;
 *     }
 * }
 *
 * System.out.println("===== DHT Routing Table Status =====");
 * System.out.println("Active buckets: " + activeBuckets + " / " + buckets.size());
 * System.out.println("Total nodes: " + totalNodes);
 * System.out.println("Replacement nodes: " + totalReplacements);
 * System.out.println("Average nodes per bucket: " + (totalNodes / Math.max(activeBuckets, 1)));
 *
 * // Network connectivity assessment
 * if (activeBuckets &lt; 10) {
 *     System.out.println("Network Status: Poor (few active buckets)");
 * } else if (activeBuckets &lt; 50) {
 *     System.out.println("Network Status: Fair (moderate connectivity)");
 * } else if (activeBuckets &lt; 100) {
 *     System.out.println("Network Status: Good (well connected)");
 * } else {
 *     System.out.println("Network Status: Excellent (highly connected)");
 * }
 * </pre>
 * <p>
 * <b>Bucket Refresh Strategy:</b>
 * <pre>
 * // Oldest buckets (least recently used) should be refreshed
 * List&lt;DhtRoutingBucket&gt; buckets = dhtStatsAlert.routingBuckets();
 *
 * DhtRoutingBucket stalestBucket = null;
 * int maxInactivity = 0;
 *
 * for (DhtRoutingBucket bucket : buckets) {
 *     if (bucket.numNodes() &gt; 0) {
 *         int inactivity = bucket.lastActive();
 *         if (inactivity &gt; maxInactivity) {
 *             maxInactivity = inactivity;
 *             stalestBucket = bucket;
 *         }
 *     }
 * }
 *
 * if (stalestBucket != null &amp;&amp; maxInactivity &gt; 1800) {
 *     System.out.println("Routing table needs refresh - last activity was " +
 *         maxInactivity + " seconds ago");
 * }
 * </pre>
 * <p>
 * <b>DHT Bootstrap and Network Join:</b>
 * <pre>
 * // When you start, routing table is empty
 * // Bootstrap process populates it:
 *
 * // 1. Connect to known bootstrap nodes
 * // 2. Query them to find nodes closer to your ID
 * // 3. Query those nodes, getting even closer ones
 * // 4. Gradually fill routing table with nearby nodes
 * //
 * // After bootstrap:
 * // - You'll have ~1600-3200 total nodes (160 buckets * 20 nodes)
 * // - Buckets are maintained automatically via lookups
 * // - Periodic refresh keeps far buckets alive
 * </pre>
 *
 * @see SessionManager#getDhtRoutingBuckets() - Get all routing buckets
 * @see DhtLookup - For active lookup metrics
 * @see SessionManager#postDhtStats() - Request DHT statistics
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
