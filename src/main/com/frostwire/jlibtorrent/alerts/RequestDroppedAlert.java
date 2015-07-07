package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.request_dropped_alert;

/**
 * This alert is generated when a peer rejects or ignores a piece request.
 *
 * @author gubatron
 * @author aldenml
 */
public final class RequestDroppedAlert extends PeerAlert<request_dropped_alert> {

    public RequestDroppedAlert(request_dropped_alert alert) {
        super(alert);
    }

    public int getBlockIndex() {
        return alert.getBlock_index();
    }

    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
