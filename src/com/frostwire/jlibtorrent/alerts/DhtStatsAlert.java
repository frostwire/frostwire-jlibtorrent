package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.DHTLookup;
import com.frostwire.jlibtorrent.DHTRoutingBucket;
import com.frostwire.jlibtorrent.swig.dht_lookup_vector;
import com.frostwire.jlibtorrent.swig.dht_routing_bucket_vector;
import com.frostwire.jlibtorrent.swig.dht_stats_alert;

/**
 * Contains current DHT state. Posted in response to {@link com.frostwire.jlibtorrent.Session#postDHTStats()}.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtStatsAlert extends AbstractAlert<dht_stats_alert> {

    public DhtStatsAlert(dht_stats_alert alert) {
        super(alert);
    }

    public DHTLookup[] getActiveRequests() {
        dht_lookup_vector v = alert.getActive_requests();

        int size = (int) v.size();
        DHTLookup[] arr = new DHTLookup[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new DHTLookup(v.get(i));
        }

        return arr;
    }

    public DHTRoutingBucket[] getRoutingTable() {
        dht_routing_bucket_vector v = alert.getRouting_table();

        int size = (int) v.size();
        DHTRoutingBucket[] arr = new DHTRoutingBucket[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new DHTRoutingBucket(v.get(i));
        }

        return arr;
    }
}
