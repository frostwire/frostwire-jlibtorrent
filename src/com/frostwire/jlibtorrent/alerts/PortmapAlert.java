package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.portmap_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when a NAT router was successfully found and
// a port was successfully mapped on it. On a NAT:ed network with a NAT-PMP
// capable router, this is typically generated once when mapping the TCP
// port and, if DHT is enabled, when the UDP port is mapped.
public final class PortmapAlert extends AbstractAlert<portmap_alert> {

    public PortmapAlert(portmap_alert alert) {
        super(alert);
    }

    // refers to the mapping index of the port map that failed, i.e.
    // the index returned from add_mapping().
    public int getMapping() {
        return alert.getMapping();
    }

    // the external port allocated for the mapping.
    public int getExternalPort() {
        return alert.getExternal_port();
    }

    // 0 for NAT-PMP and 1 for UPnP.
    public int getMapType() {
        return alert.getMap_type();
    }
}
