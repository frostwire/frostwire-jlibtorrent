package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_snubbed_alert;

/**
 * This alert is generated when a peer is snubbed, when it stops sending data when we request
 * it.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerSnubbedAlert extends PeerAlert<peer_snubbed_alert> {

    public PeerSnubbedAlert(peer_snubbed_alert alert) {
        super(alert);
    }
}
