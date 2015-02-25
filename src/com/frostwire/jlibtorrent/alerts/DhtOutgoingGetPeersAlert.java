package com.frostwire.jlibtorrent.alerts;

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


}
