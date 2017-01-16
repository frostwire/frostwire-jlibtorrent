package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.DhtLookup;
import com.frostwire.jlibtorrent.DhtRoutingBucket;
import com.frostwire.jlibtorrent.swig.dht_lookup_vector;
import com.frostwire.jlibtorrent.swig.dht_routing_bucket_vector;
import com.frostwire.jlibtorrent.swig.dht_stats_alert;

import java.util.ArrayList;

/**
 * Contains current DHT state. Posted in response to
 * {@link com.frostwire.jlibtorrent.SessionHandle#postDHTStats()}.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtStatsAlert extends AbstractAlert<dht_stats_alert> {

    DhtStatsAlert(dht_stats_alert alert) {
        super(alert);
    }

    /**
     * An array (list) with the currently running DHT lookups.
     *
     * @return the list of active requests
     */
    public ArrayList<DhtLookup> activeRequests() {
        dht_lookup_vector v = alert.getActive_requests();
        int size = (int) v.size();

        ArrayList<DhtLookup> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new DhtLookup(v.get(i)));
        }

        return l;
    }

    /**
     * Contains information about every bucket in the DHT routing
     * table.
     *
     * @return the routing table
     */
    public ArrayList<DhtRoutingBucket> routingTable() {
        dht_routing_bucket_vector v = alert.getRouting_table();
        int size = (int) v.size();
        ArrayList<DhtRoutingBucket> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new DhtRoutingBucket(v.get(i)));
        }

        return l;
    }
}
