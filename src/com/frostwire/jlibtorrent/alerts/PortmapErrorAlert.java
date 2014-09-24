package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.portmap_error_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when a NAT router was successfully found but some
// part of the port mapping request failed. It contains a text message that
// may help the user figure out what is wrong. This alert is not generated in
// case it appears the client is not running on a NAT:ed network or if it
// appears there is no NAT router that can be remote controlled to add port
// mappings.
public final class PortmapErrorAlert extends AbstractAlert<portmap_error_alert> {

    public PortmapErrorAlert(portmap_error_alert alert) {
        super(alert);
    }

    // refers to the mapping index of the port map that failed, i.e.
    // the index returned from add_mapping().
    public int getMapping() {
        return alert.getMapping();
    }

    // 0 for NAT-PMP and 1 for UPnP.
    public int getMapType() {
        return alert.getMap_type();
    }

    // tells you what failed.
    public error_code getError() {
        return alert.getError();
    }
}
