package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.portmap_protocol;

/**
 * @author gubatron
 * @author aldenml
 */
public enum PortmapProtocol {

    NONE(portmap_protocol.none.swigValue()),

    TCP(portmap_protocol.tcp.swigValue()),

    UDP(portmap_protocol.udp.swigValue());

    PortmapProtocol(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int swig() {
        return swigValue;
    }

    public static PortmapProtocol fromSwig(int swigValue) {
        PortmapProtocol[] enumValues = PortmapProtocol.class.getEnumConstants();
        for (PortmapProtocol ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + PortmapProtocol.class + " with value " + swigValue);
    }
}
