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

    public DhtAnnounceAlert(dht_announce_alert alert) {
        super(alert);
    }

    public Address getIP() {
        return new Address(alert.getIp());
    }

    public int getPort() {
        return alert.getPort();
    }

    public Sha1Hash getInfoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }
}
