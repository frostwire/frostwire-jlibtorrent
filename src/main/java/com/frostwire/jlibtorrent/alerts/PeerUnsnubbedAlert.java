package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_unsnubbed_alert;

/**
 * This alert is generated when a peer is unsnubbed. Essentially when it was snubbed for stalling
 * sending data, and now it started sending data again.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerUnsnubbedAlert extends PeerAlert<peer_unsnubbed_alert> {

    public PeerUnsnubbedAlert(peer_unsnubbed_alert alert) {
        super(alert);
    }
}
