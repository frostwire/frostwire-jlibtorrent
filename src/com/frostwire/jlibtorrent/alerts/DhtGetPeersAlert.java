package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_get_peers_alert;

/**
 * This alert is generated when a DHT node sends a ``get_peers`` message to
 * our DHT node. It belongs to the ``dht_notification`` category.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtGetPeersAlert extends AbstractAlert<dht_get_peers_alert> {

    public DhtGetPeersAlert(dht_get_peers_alert alert) {
        super(alert);
    }
}
