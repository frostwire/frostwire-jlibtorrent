package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.tcp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TcpEndpoint {

    private final tcp_endpoint endp;

    public TcpEndpoint(tcp_endpoint endp) {
        this.endp = endp;
    }

    public tcp_endpoint swig() {
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
        return "tcp:" + endp.address() + ":" + endp.port();
    }
}
