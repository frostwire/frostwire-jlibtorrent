package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.close_reason_t;
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
     * The kind of socket this peer was connected over.
     *
     * @return
     */
    public int getSocketType() {
        return alert.getSocket_type();
    }

    /**
     * The operation or level where the error occurred. Specified as an
     * value from the operation_t enum.
     *
     * @return
     */
    public Operation getOperation() {
        return Operation.fromSwig(alert.getOperation());
    }

    /**
     * tells you what error caused peer to disconnect.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }

    /**
     * The reason the peer disconnected (if specified).
     *
     * @return
     */
    // TODO: translate close_reason_t
    public close_reason_t getReason() {
        return alert.getReason();
    }
}
