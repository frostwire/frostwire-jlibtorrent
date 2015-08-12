package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.dht_direct_response_alert;

/**
 * This is posted exactly once for every call to session_handle::dht_direct_request.
 * If the request failed, response() will return a default constructed bdecode_node.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtDirectResponseAlert extends AbstractAlert<dht_direct_response_alert> {

    public DhtDirectResponseAlert(dht_direct_response_alert alert) {
        super(alert);
    }

    public UdpEndpoint address() {
        return new UdpEndpoint(alert.getAddr());
    }

    // TODO: Build the wrapper
    public bdecode_node response() {
        return alert.response();
    }
}
