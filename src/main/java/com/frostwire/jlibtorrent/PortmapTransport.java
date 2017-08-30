package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.portmap_transport;

/**
 * @author gubatron
 * @author aldenml
 */
public enum PortmapTransport {

    NAT_PMP(portmap_transport.natpmp.swigValue()),

    UPNP(portmap_transport.upnp.swigValue());

    PortmapTransport(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int swig() {
        return swigValue;
    }

    public static PortmapTransport fromSwig(int swigValue) {
        PortmapTransport[] enumValues = PortmapTransport.class.getEnumConstants();
        for (PortmapTransport ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + PortmapTransport.class + " with value " + swigValue);
    }
}
