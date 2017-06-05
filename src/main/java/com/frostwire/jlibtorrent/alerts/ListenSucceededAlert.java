package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.swig.listen_succeeded_alert;

/**
 * This alert is posted when the listen port succeeds to be opened on a
 * particular interface. {@link #address()} and {@link #port()} is the
 * endpoint that successfully was opened for listening.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ListenSucceededAlert extends AbstractAlert<listen_succeeded_alert> {

    ListenSucceededAlert(listen_succeeded_alert alert) {
        super(alert);
    }

    /**
     * The address libtorrent ended up listening on. This address
     * refers to the local interface.
     *
     * @return the address ended up listening on
     */
    public Address address() {
        return new Address(alert.get_address());
    }

    /**
     * The port libtorrent ended up listening on.
     *
     * @return the port
     */
    public int port() {
        return alert.getPort();
    }

    /**
     * the type of listen socket this alert refers to.
     *
     * @return the socket type
     */
    public SocketType socketType() {
        return SocketType.fromSwig(alert.getSocket_type().swigValue());
    }
}
