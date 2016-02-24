package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.portmap_alert;

/**
 * This alert is generated when a NAT router was successfully found and
 * a port was successfully mapped on it. On a NAT:ed network with a NAT-PMP
 * capable router, this is typically generated once when mapping the TCP
 * port and, if DHT is enabled, when the UDP port is mapped.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PortmapAlert extends AbstractAlert<portmap_alert> {

    public PortmapAlert(portmap_alert alert) {
        super(alert);
    }

    /**
     * refers to the mapping index of the port map that failed, i.e.
     * the index returned from add_mapping().
     *
     * @return
     */
    public int mapping() {
        return alert.getMapping();
    }

    /**
     * the external port allocated for the mapping.
     *
     * @return
     */
    public int externalPort() {
        return alert.getExternal_port();
    }

    public PortmapType mapType() {
        return PortmapType.fromSwig(alert.getMap_type());
    }

    /**
     * The protocol this mapping was for.
     *
     * @return
     */
    public Protocol protocol() {
        return Protocol.fromSwig(alert.getProtocol());
    }

    /**
     *
     */
    public enum Protocol {

        /**
         *
         */
        TCP(portmap_alert.protocol_t.tcp.swigValue()),

        /**
         *
         */
        UDP(portmap_alert.protocol_t.udp.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Protocol(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int swig() {
            return swigValue;
        }

        public static Protocol fromSwig(int swigValue) {
            Protocol[] enumValues = Protocol.class.getEnumConstants();
            for (Protocol ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
