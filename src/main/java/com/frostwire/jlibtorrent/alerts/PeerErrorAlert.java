package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.peer_error_alert;

/**
 * This alert is generated when a peer sends invalid data over the
 * peer-peer protocol. The peer will be disconnected, but you get
 * its ip address from the alert, to identify it.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerErrorAlert extends PeerAlert<peer_error_alert> {

    PeerErrorAlert(peer_error_alert alert) {
        super(alert);
    }

    /**
     * @return the operation.
     */
    public Operation operation() {
        return Operation.fromSwig(alert.getOp());
    }

    /**
     * Tells you what error caused this alert.
     *
     * @return the error
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }
}
