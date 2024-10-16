package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PortmapTransport;
import com.frostwire.jlibtorrent.swig.portmap_log_alert;

/**
 * This alert is generated to log informational events related to either
 * UPnP or NAT-PMP. They contain a log line and the type (0 = NAT-PMP
 * and 1 = UPnP). Displaying these messages to an end user is only useful
 * for debugging the UPnP or NAT-PMP implementation.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PortmapLogAlert extends AbstractAlert<portmap_log_alert> {

    PortmapLogAlert(portmap_log_alert alert) {
        super(alert);
    }

    public PortmapTransport mapType() {
        return PortmapTransport.fromSwig(alert.getMap_transport().swigValue());
    }

    /**
     * The message associated with this log line.
     *
     * @return the log message
     */
    public String logMessage() {
        return alert.log_message();
    }
}
