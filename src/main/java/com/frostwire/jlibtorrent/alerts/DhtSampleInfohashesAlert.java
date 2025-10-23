package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtSampleInfohashesAlert extends AbstractAlert<dht_sample_infohashes_alert> {

    DhtSampleInfohashesAlert(dht_sample_infohashes_alert alert) {
        super(alert);
    }

    public UdpEndpoint endpoint() {
        return new UdpEndpoint(alert.get_endpoint());
    }

    public long interval() {
        return alert.get_interval();
    }

    /**
     * This value indicates how many infohash keys are currently
     * in the node's storage.
     * <p>
     * If the value is larger than the number of returned samples
     * it indicates that the indexer may obtain additional samples
     * after waiting out the interval.
     *
     * @return how many infohash keys are currently in the node's
     * storage
     */
    public int numInfohashes() {
        return alert.getNum_infohashes();
    }

    public int numSamples() {
        return alert.num_samples();
    }

    public List<Sha1Hash> samples() {
        sha1_hash_vector v = alert.samples();
        int size = (int) v.size();
        ArrayList<Sha1Hash> samples = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            sha1_hash ih = v.get(i);
            Sha1Hash h = new Sha1Hash(ih).clone();
            samples.add(h);
        }

        return samples;
    }

    /**
     * The total number of nodes returned by .
     *
     * @return the number of nodes
     */
    public int numNodes() {
        return alert.num_nodes();
    }

    /**
     * This is the set of more DHT nodes returned by the request.
     * <p>
     * The information is included so that indexing nodes can perform
     * a keyspace traversal with a single RPC per node by adjusting
     * the target value for each RPC.
     *
     * @return the set of more DHT nodes returned by the request
     */
    public List<Pair<Sha1Hash, UdpEndpoint>> nodes() {
        sha1_hash_udp_endpoint_pair_vector v = alert.nodes();
        int size = (int) v.size();
        ArrayList<Pair<Sha1Hash, UdpEndpoint>> nodes = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            sha1_hash_udp_endpoint_pair p = v.get(i);
            Sha1Hash h = new Sha1Hash(p.getFirst()).clone();
            UdpEndpoint endp = new UdpEndpoint(p.getSecond()).clone();
            nodes.add(new Pair<>(h, endp));
        }

        return nodes;
    }
}
