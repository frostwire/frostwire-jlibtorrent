package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.swig.external_ip_alert;

/**
 * Whenever libtorrent learns about the machines external IP, this alert is
 * generated. The external IP address can be acquired from the tracker (if it
 * supports that) or from peers that supports the extension protocol.
 * The address can be accessed through the ``external_address`` member.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ExternalIpAlert extends AbstractAlert<external_ip_alert> {

    public ExternalIpAlert(external_ip_alert alert) {
        super(alert);
    }

    /**
     * the IP address that is believed to be our external IP.
     *
     * @return
     */
    public Address getExternalAddress() {
        return new Address(alert.getExternal_address());
    }
}
