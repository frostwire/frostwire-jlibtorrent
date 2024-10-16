package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_get_peers_reply_alert;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;
import com.frostwire.jlibtorrent.swig.tcp_endpoint_vector;

import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtGetPeersReplyAlert extends AbstractAlert<dht_get_peers_reply_alert> {

    DhtGetPeersReplyAlert(dht_get_peers_reply_alert alert) {
        super(alert);
    }

    /**
     * @return the hash
     */
    public Sha1Hash infoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }

    /**
     * @return the number of peers
     */
    public int numPeers() {
        return alert.num_peers();
    }

    /**
     * This method creates a new list each time is called.
     *
     * @return the list of peers
     */
    public ArrayList<TcpEndpoint> peers() {
        tcp_endpoint_vector v = alert.peers();
        int size = (int) v.size();
        ArrayList<TcpEndpoint> peers = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            tcp_endpoint endp = v.get(i);
            String ip = new Address(endp.address()).toString(); // clone
            peers.add(new TcpEndpoint(ip, endp.port()));
        }

        return peers;
    }
}
