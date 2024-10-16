package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.BDecodeNode;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_direct_response_alert;

/**
 * This is posted exactly once for every call to
 * {@link com.frostwire.jlibtorrent.SessionHandle#dhtDirectRequest(UdpEndpoint, Entry, long)}.
 * <p>
 * If the request failed, {@link #response()} will return a default constructed {@link BDecodeNode}.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtDirectResponseAlert extends AbstractAlert<dht_direct_response_alert> {

    DhtDirectResponseAlert(dht_direct_response_alert alert) {
        super(alert);
    }

    /**
     * @return the user data
     */
    public long userdata() {
        return alert.get_userdata().get();
    }

    /**
     * @return the endpoint
     */
    public UdpEndpoint endpoint() {
        return new UdpEndpoint(alert.get_endpoint());
    }

    /**
     * @return the response
     */
    public BDecodeNode response() {
        return new BDecodeNode(alert.response());
    }
}
