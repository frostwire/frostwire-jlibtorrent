package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TcpEndpoint implements Cloneable {

    private final tcp_endpoint endp;

    /**
     * @param endp the native object
     */
    public TcpEndpoint(tcp_endpoint endp) {
        this.endp = endp;
    }

    /**
     *
     */
    public TcpEndpoint() {
        this(new tcp_endpoint());
    }

    /**
     * @param address the address
     * @param port    the port
     */
    public TcpEndpoint(Address address, int port) {
        this(new tcp_endpoint(address.swig(), port));
    }

    /**
     * @param ip   the address as an IP
     * @param port the port
     */
    public TcpEndpoint(String ip, int port) {
        error_code ec = new error_code();
        address addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
        this.endp = new tcp_endpoint(addr, port);
    }

    /**
     * @return the native object
     */
    public tcp_endpoint swig() {
        return endp;
    }

    /**
     * @return the address
     */
    public Address address() {
        return new Address(endp.address());
    }

    /**
     * @return the port
     */
    public int port() {
        return endp.port();
    }

    /**
     * @return the string representation
     */
    @Override
    public String toString() {
        return "tcp:" + Address.toString(endp.address()) + ":" + endp.port();
    }

    @Override
    public TcpEndpoint clone() {
        return new TcpEndpoint(new tcp_endpoint(endp));
    }
}
