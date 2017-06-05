package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.dht_announce_alert;

/**
 * This alert is generated when a DHT node announces to an info-hash on our
 * DHT node. It belongs to the ``dht_notification`` category.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtAnnounceAlert extends AbstractAlert<dht_announce_alert> {

    DhtAnnounceAlert(dht_announce_alert alert) {
        super(alert);
    }

    /**
     * @return the ip
     */
    public Address ip() {
        return new Address(alert.get_ip());
    }

    /**
     * @return the port
     */
    public int port() {
        return alert.getPort();
    }

    /**
     * @return the info-hash
     */
    public Sha1Hash infoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }
}
