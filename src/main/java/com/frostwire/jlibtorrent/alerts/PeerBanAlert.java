package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_ban_alert;

/**
 * This alert is generated when a peer is banned because it has sent too many corrupt pieces
 * to us. ``ip`` is the endpoint to the peer that was banned.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerBanAlert extends PeerAlert<peer_ban_alert> {

    public PeerBanAlert(peer_ban_alert alert) {
        super(alert);
    }
}
