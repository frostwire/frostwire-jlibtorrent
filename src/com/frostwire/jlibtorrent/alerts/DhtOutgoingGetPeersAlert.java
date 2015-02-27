package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_outgoing_get_peers_alert;

/**
 * This alert is generated when we send a get_peers request.
 * It belongs to the {@code dht_notification} category.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtOutgoingGetPeersAlert extends AbstractAlert<dht_outgoing_get_peers_alert> {

    public DhtOutgoingGetPeersAlert(dht_outgoing_get_peers_alert alert) {
        super(alert);
    }

    /**
     * the info_hash of the torrent we're looking for peers for.
     *
     * @return
     */
    public Sha1Hash getInfoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }

    /**
     * if this was an obfuscated lookup, this is the info-hash target
     * actually sent to the node.
     *
     * @return
     */
    public Sha1Hash getObfuscatedInfoHash() {
        return new Sha1Hash(alert.getObfuscated_info_hash());
    }

    /**
     * the endpoint we're sending this query to
     *
     * @return
     */
    public UdpEndpoint getIp() {
        return new UdpEndpoint(alert.getIp());
    }
}
