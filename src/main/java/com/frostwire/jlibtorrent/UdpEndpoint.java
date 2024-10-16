package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UdpEndpoint implements Cloneable {

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

    /**
     * Get the port associated with the endpoint. The port number is always in
     * the host's byte order.
     *
     * @return the port
     */
    public int port() {
        return endp.port();
    }

    @Override
    public String toString() {
        return "udp://" + endp.address().to_string() + ":" + endp.port();
    }

    @Override
    public UdpEndpoint clone() {
        return new UdpEndpoint(new udp_endpoint(endp));
    }
}
