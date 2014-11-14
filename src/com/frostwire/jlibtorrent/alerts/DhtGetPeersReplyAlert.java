package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_get_peers_reply_alert;
import com.frostwire.jlibtorrent.swig.tcp_endpoint_vector;

import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtGetPeersReplyAlert extends AbstractAlert<dht_get_peers_reply_alert> {

    public DhtGetPeersReplyAlert(dht_get_peers_reply_alert alert) {
        super(alert);
    }

    public Sha1Hash getInfoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }

    public ArrayList<TcpEndpoint> getPeers() {
        tcp_endpoint_vector v = alert.getPeers();
        int size = (int) v.size();
        ArrayList<TcpEndpoint> peers = new ArrayList<TcpEndpoint>(size);

        for (int i = 0; i < size; i++) {
            peers.add(new TcpEndpoint(v.get(i)));
        }

        return peers;
    }
}
