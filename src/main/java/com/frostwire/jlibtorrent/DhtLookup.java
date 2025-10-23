package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_lookup;

/**
 * Statistics for an active Distributed Hash Table (DHT) lookup operation.
 * <p>
 * {@code DhtLookup} provides real-time metrics about a DHT query in progress.
 * DHT lookups are the fundamental operation for peer discovery and DHT item retrieval
 * in distributed networks. This class gives visibility into the performance and state
 * of individual lookup operations.
 * <p>
 * <b>Understanding DHT Lookups:</b>
 * <br/>
 * A DHT lookup is a network traversal algorithm (like Kademlia) that:
 * <ul>
 *   <li>Starts with a target (node-id or info-hash)</li>
 *   <li>Queries nodes in parallel (branch factor)</li>
 *   <li>Receives routing information pointing closer to target</li>
 *   <li>Iteratively narrows down to target or nearby nodes</li>
 *   <li>Collects results (peers for info-hash, values for DHT items)</li>
 *   <li>Eventually completes when no closer nodes can be found</li>
 * </ul>
 * <p>
 * <b>Common DHT Lookup Types:</b>
 * <pre>
 * // "get_peers" - Find peers for an info-hash (peer discovery)
 * // "get" - Retrieve an immutable DHT item
 * // "put" - Store an immutable DHT item
 * // "find_node" - Locate specific DHT nodes
 * </pre>
 * <p>
 * <b>Monitoring DHT Lookups:</b>
 * <pre>
 * // Lookups are reported via DhtGetPeersReplyAlert
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.DHT_GET_PEERS_REPLY.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         DhtGetPeersReplyAlert a = (DhtGetPeersReplyAlert) alert;
 *
 *         // Get lookup statistics
 *         DhtLookup lookup = a.lookupData();
 *         System.out.println("Lookup type: " + lookup.type());
 *         System.out.println("Target: " + lookup.target().toHex());
 *         System.out.println("Outstanding requests: " + lookup.outstandingRequests());
 *         System.out.println("Responses received: " + lookup.responses());
 *         System.out.println("Timeouts: " + lookup.timeouts());
 *     }
 * });
 * </pre>
 * <p>
 * <b>Lookup Metrics Interpretation:</b>
 * <pre>
 * DhtLookup lookup = ...;
 *
 * // Branch factor: how many nodes are queried in parallel
 * // Typically 20 for get_peers, 3-4 for get/put
 * int branchFactor = lookup.branchFactor();
 * System.out.println("Parallel queries: " + branchFactor);
 *
 * // Outstanding requests: how many queries are awaiting response
 * int outstanding = lookup.outstandingRequests();
 * System.out.println("Pending responses: " + outstanding);
 *
 * // Responses: successful replies from nodes
 * int responses = lookup.responses();
 * System.out.println("Nodes responded: " + responses);
 *
 * // Timeouts: requests that failed (no response)
 * int timeouts = lookup.timeouts();
 * System.out.println("Failed requests: " + timeouts);
 *
 * // Nodes left: additional candidates not yet queried
 * int remaining = lookup.nodesLeft();
 * System.out.println("Candidates remaining: " + remaining);
 *
 * // Last message sent: seconds since most recent query
 * int lastSent = lookup.lastSent();
 * System.out.println("Time since last query: " + lastSent + "s");
 * </pre>
 * <p>
 * <b>Lookup Performance Analysis:</b>
 * <pre>
 * DhtLookup lookup = ...;
 *
 * // Success rate
 * int totalRequests = lookup.responses() + lookup.timeouts();
 * double successRate = (double) lookup.responses() / totalRequests * 100;
 * System.out.println("Success rate: " + successRate + "%");
 *
 * // Is lookup stalled?
 * if (lookup.outstandingRequests() == 0 &amp;&amp; lookup.nodesLeft() &gt; 0) {
 *     System.out.println("Lookup stalled - no pending requests but more nodes available");
 * }
 *
 * // Lookup is completing
 * if (lookup.outstandingRequests() == 0 &amp;&amp; lookup.nodesLeft() == 0) {
 *     System.out.println("Lookup complete!");
 * }
 *
 * // Lookup is active
 * if (lookup.outstandingRequests() &gt; 0) {
 *     System.out.println("Lookup in progress, " + lookup.outstandingRequests() + " requests pending");
 * }
 * </pre>
 * <p>
 * <b>Real-World Example - Peer Discovery:</b>
 * <pre>
 * // Finding peers for a torrent
 * Sha1Hash infoHash = new Sha1Hash("d8e8fca2dc0f896fd7cb4cb0031ba249");
 *
 * // Initiate DHT get_peers lookup
 * sm.dhtGetPeers(infoHash);
 *
 * // Listen for results
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.DHT_GET_PEERS_REPLY.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         DhtGetPeersReplyAlert a = (DhtGetPeersReplyAlert) alert;
 *         DhtLookup lookup = a.lookupData();
 *
 *         System.out.println("===== DHT Lookup Statistics =====");
 *         System.out.println("Type: " + lookup.type());
 *         System.out.println("Target: " + lookup.target().toHex());
 *         System.out.println("Outstanding: " + lookup.outstandingRequests());
 *         System.out.println("Responses: " + lookup.responses());
 *         System.out.println("Timeouts: " + lookup.timeouts());
 *         System.out.println("Branch factor: " + lookup.branchFactor());
 *         System.out.println("Nodes left: " + lookup.nodesLeft());
 *
 *         // Get actual peers from alert
 *         List&lt;TcpEndpoint&gt; peers = a.peers();
 *         System.out.println("Peers found: " + peers.size());
 *         for (TcpEndpoint peer : peers) {
 *             System.out.println("  " + peer.toString());
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>DHT Lookup Types and Their Branch Factors:</b>
 * <pre>
 * get_peers (find peers)       - Branch factor ~20 (aggressive search)
 * get (retrieve item)          - Branch factor ~3-4 (conservative)
 * put (store item)             - Branch factor ~3-4 (conservative)
 * find_node (locate nodes)     - Branch factor ~3-4 (conservative)
 * </pre>
 *
 * @see SessionManager#dhtGetPeers(Sha1Hash) - Initiate peer discovery
 * @see SessionManager#dhtGetItem(Sha1Hash, int) - Retrieve DHT item
 * @see DhtRoutingBucket - For bucket statistics
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtLookup {

    private final dht_lookup l;

    /**
     * internal use
     *
     * @param l
     */
    public DhtLookup(dht_lookup l) {
        this.l = l;
    }

    /**
     * The native object.
     *
     * @return the native object
     */
    public dht_lookup swig() {
        return l;
    }

    /**
     * string literal indicating which kind of lookup this is.
     *
     * @return
     */
    public String type() {
        return l.get_type();
    }

    /**
     * the number of outstanding request to individual nodes
     * this lookup has right now.
     *
     * @return
     */
    public int outstandingRequests() {
        return l.getOutstanding_requests();
    }

    /**
     * the total number of requests that have timed out so far
     * for this lookup.
     *
     * @return
     */
    public int timeouts() {
        return l.getTimeouts();
    }

    /**
     * the total number of responses we have received for this
     * lookup so far for this lookup.
     *
     * @return
     */
    public int responses() {
        return l.getResponses();
    }

    /**
     * the branch factor for this lookup. This is the number of
     * nodes we keep outstanding requests to in parallel by default.
     * when nodes time out we may increase this.
     *
     * @return
     */
    public int branchFactor() {
        return l.getBranch_factor();
    }

    /**
     * the number of nodes left that could be queries for this
     * lookup. Many of these are likely to be part of the trail
     * while performing the lookup and would never end up actually
     * being queried.
     *
     * @return
     */
    public int nodesLeft() {
        return l.getNodes_left();
    }

    /**
     * the number of seconds ago the
     * last message was sent that's still
     * outstanding.
     *
     * @return
     */
    public int lastSent() {
        return l.getLast_sent();
    }

    /**
     * the number of outstanding requests
     * that have exceeded the short timeout
     * and are considered timed out in the
     * sense that they increased the branch
     * factor.
     *
     * @return
     */
    public int firstTimeout() {
        return l.getFirst_timeout();
    }

    /**
     * The node-id or info-hash target for this lookup.
     *
     * @return the target
     */
    public Sha1Hash target() {
        return new Sha1Hash(l.getTarget());
    }
}
