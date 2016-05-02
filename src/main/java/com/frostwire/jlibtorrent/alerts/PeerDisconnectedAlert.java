package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.peer_disconnected_alert;

/**
 * This alert is generated when a peer is disconnected for any reason (other than the ones
 * covered by {@link PeerErrorAlert}).
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerDisconnectedAlert extends PeerAlert<peer_disconnected_alert> {

    PeerDisconnectedAlert(peer_disconnected_alert alert) {
        super(alert);
    }

    /**
     * The kind of socket this peer was connected over.
     *
     * @return
     */
    public int socketType() {
        return alert.getSocket_type();
    }

    /**
     * The operation or level where the error occurred.
     *
     * @return
     * @see Operation
     */
    public Operation operation() {
        return Operation.fromSwig(alert.getOperation().swigValue());
    }

    /**
     * @return
     */
    public String operationName() {
        return libtorrent.operation_name(alert.getOperation().swigValue());
    }

    /**
     * Tells you what error caused peer to disconnect.
     *
     * @return
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }

    /**
     * The reason the peer disconnected (if specified).
     *
     * @return
     */
    public CloseReason reason() {
        return CloseReason.fromSwig(alert.getReason().swigValue());
    }
}
