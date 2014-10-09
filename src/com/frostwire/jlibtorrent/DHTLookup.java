package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.dht_lookup;

/**
 * Holds statistics about a current dht_lookup operation.
 * a DHT lookup is the travesal of nodes, looking up a
 * set of target nodes in the DHT for retrieving and possibly
 * storing information in the DHT
 *
 * @author gubatron
 * @author aldenml
 */
public final class DHTLookup {

    private final dht_lookup l;

    public DHTLookup(dht_lookup l) {
        this.l = l;
    }

    public dht_lookup getSwig() {
        return l;
    }

    /**
     * string literal indicating which kind of lookup this is.
     *
     * @return
     */
    public String getType() {
        return l.getType();
    }

    /**
     * the number of outstanding request to individual nodes
     * this lookup has right now.
     *
     * @return
     */
    public int getOutstandingRequests() {
        return l.getOutstanding_requests();
    }

    /**
     * the total number of requests that have timed out so far
     * for this lookup.
     *
     * @return
     */
    public int getTimeouts() {
        return l.getTimeouts();
    }

    /**
     * the total number of responses we have received for this
     * lookup so far for this lookup.
     *
     * @return
     */
    public int getResponses() {
        return l.getResponses();
    }

    /**
     * the branch factor for this lookup. This is the number of
     * nodes we keep outstanding requests to in parallel by default.
     * when nodes time out we may increase this.
     *
     * @return
     */
    public int getBranchFactor() {
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
    public int getNodesLeft() {
        return l.getNodes_left();
    }

    /**
     * the number of seconds ago the
     * last message was sent that's still
     * outstanding.
     *
     * @return
     */
    public int getLastSent() {
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
    public int getFirstTimeout() {
        return l.getFirst_timeout();
    }
}
