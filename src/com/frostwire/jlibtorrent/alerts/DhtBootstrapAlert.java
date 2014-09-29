package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_bootstrap_alert;

/**
 * This alert is posted when the initial DHT bootstrap is done.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtBootstrapAlert extends AbstractAlert<dht_bootstrap_alert> {

    public DhtBootstrapAlert(dht_bootstrap_alert alert) {
        super(alert);
    }
}
