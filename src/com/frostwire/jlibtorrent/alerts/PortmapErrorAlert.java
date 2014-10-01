package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.portmap_error_alert;

/**
 * This alert is generated when a NAT router was successfully found but some
 * part of the port mapping request failed. It contains a text message that
 * may help the user figure out what is wrong. This alert is not generated in
 * case it appears the client is not running on a NAT:ed network or if it
 * appears there is no NAT router that can be remote controlled to add port
 * mappings.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PortmapErrorAlert extends AbstractAlert<portmap_error_alert> {

    public PortmapErrorAlert(portmap_error_alert alert) {
        super(alert);
    }

    /**
     * refers to the mapping index of the port map that failed, i.e.
     * the index returned from add_mapping().
     *
     * @return
     */
    public int getMapping() {
        return alert.getMapping();
    }

    public PortmapType getMapType() {
        return PortmapType.fromSwig(alert.getMap_type());
    }

    /**
     * tells you what failed.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
