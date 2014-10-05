package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.peer_disconnected_alert;

/**
 * This alert is generated when a peer is disconnected for any reason (other than the ones
 * covered by peer_error_alert ).
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerDisconnectedAlert extends PeerAlert<peer_disconnected_alert> {

    public PeerDisconnectedAlert(peer_disconnected_alert alert) {
        super(alert);
    }

    /**
     * tells you what error caused peer to disconnect.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
