package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_reply_alert;

/**
 * This alert is generated each time the DHT receives peers from a node. ``num_peers``
 * is the number of peers we received in this packet. Typically these packets are
 * received from multiple DHT nodes, and so the alerts are typically generated
 * a few at a time.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtReplyAlert extends TrackerAlert<dht_reply_alert> {

    public DhtReplyAlert(dht_reply_alert alert) {
        super(alert);
    }

    public int getNumPeers() {
        return alert.getNum_peers();
    }
}
