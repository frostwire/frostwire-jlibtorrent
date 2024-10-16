package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_connect_alert;

/**
 * This alert is posted every time an outgoing peer connect attempts succeeds.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerConnectAlert extends PeerAlert<peer_connect_alert> {

    PeerConnectAlert(peer_connect_alert alert) {
        super(alert);
    }

    public int socketType() {
        return alert.getSocket_type().swigValue();
    }
}
