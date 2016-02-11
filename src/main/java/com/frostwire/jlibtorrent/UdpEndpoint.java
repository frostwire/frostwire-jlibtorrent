package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.udp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UdpEndpoint {

    private final udp_endpoint endp;

    public UdpEndpoint(udp_endpoint endp) {
        this.endp = endp;
    }

    public udp_endpoint swig() {
        return endp;
    }

    public Address address() {
        return new Address(endp.address());
    }

    public int port() {
        return endp.port();
    }

    @Override
    public String toString() {
        return "udp:" + endp.address() + ":" + endp.port();
    }
}
