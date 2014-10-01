package com.frostwire.jlibtorrent.alerts;

/**
 * @author gubatron
 * @author aldenml
 */
public enum PortmapType {

    NAT_PMP(0),

    UPNP(1),

    UNKNOWN(-1);

    private PortmapType(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int getSwig() {
        return swigValue;
    }

    public static PortmapType fromSwig(int swigValue) {
        PortmapType[] enumValues = PortmapType.class.getEnumConstants();
        for (PortmapType ev : enumValues) {
            if (ev.getSwig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
