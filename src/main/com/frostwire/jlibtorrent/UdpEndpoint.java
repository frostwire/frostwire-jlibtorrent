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

    public udp_endpoint getSwig() {
        return endp;
    }

    public String getAddress() {
        return endp.address();
    }

    public int getPort() {
        return endp.port();
    }
}
