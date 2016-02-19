package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
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

    public UdpEndpoint() {
        this(new udp_endpoint());
    }

    public UdpEndpoint(Address address, int port) {
        this(new udp_endpoint(address.swig(), port));
    }

    public UdpEndpoint(String ip, int port) {
        error_code ec = new error_code();
        address addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
        this.endp = new udp_endpoint(addr, port);
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
