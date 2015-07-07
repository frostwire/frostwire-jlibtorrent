package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.lsd_peer_alert;

/**
 * This alert is generated when we receive a local service discovery message
 * from a peer for a torrent we're currently participating in.
 *
 * @author gubatron
 * @author aldenml
 */
public final class LsdPeerAlert extends PeerAlert<lsd_peer_alert> {

    public LsdPeerAlert(lsd_peer_alert alert) {
        super(alert);
    }
}
