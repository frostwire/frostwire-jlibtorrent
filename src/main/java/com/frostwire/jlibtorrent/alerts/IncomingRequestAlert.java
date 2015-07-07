package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PeerRequest;
import com.frostwire.jlibtorrent.swig.incoming_request_alert;

/**
 * Posted every time an incoming request from a peer is accepted and queued
 * up for being serviced. This alert is only posted if
 * the alert::incoming_request_notification flag is enabled in the alert
 * mask.
 *
 * @author gubatron
 * @author aldenml
 */
public final class IncomingRequestAlert extends PeerAlert<incoming_request_alert> {

    public IncomingRequestAlert(incoming_request_alert alert) {
        super(alert);
    }

    /**
     * The request this peer sent to us.
     *
     * @return
     */
    public PeerRequest request() {
        return new PeerRequest(alert.getReq());
    }
}
