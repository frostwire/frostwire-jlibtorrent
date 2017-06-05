package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.udp_error_alert;

/**
 * This alert is posted when there is an error on the UDP socket. The
 * UDP socket is used for all uTP, DHT and UDP tracker traffic. It's
 * global to the session.
 *
 * @author gubatron
 * @author aldenml
 */
public final class UdpErrorAlert extends AbstractAlert<udp_error_alert> {

    UdpErrorAlert(udp_error_alert alert) {
        super(alert);
    }

    /**
     * The source address associated with the error (if any).
     *
     * @return the endpoint
     */
    public UdpEndpoint endpoint() {
        return new UdpEndpoint(alert.get_endpoint());
    }

    /**
     * The error code describing the error.
     *
     * @return the error
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }
}
