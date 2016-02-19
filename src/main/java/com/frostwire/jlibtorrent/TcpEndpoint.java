package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
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

    public TcpEndpoint() {
        this(new tcp_endpoint());
    }

    public TcpEndpoint(Address address, int port) {
        this(new tcp_endpoint(address.swig(), port));
    }

    public TcpEndpoint(String ip, int port) {
        error_code ec = new error_code();
        address addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
        this.endp = new tcp_endpoint(addr, port);
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
