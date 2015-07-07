package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.listen_succeeded_alert;

/**
 * This alert is posted when the listen port succeeds to be opened on a
 * particular interface. ``endpoint`` is the endpoint that successfully
 * was opened for listening.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ListenSucceededAlert extends AbstractAlert<listen_succeeded_alert> {

    public ListenSucceededAlert(listen_succeeded_alert alert) {
        super(alert);
    }

    /**
     * the endpoint libtorrent ended up listening on. The address
     * refers to the local interface and the port is the listen port.
     *
     * @return
     */
    public TcpEndpoint getEndpoint() {
        return new TcpEndpoint(alert.getEndpoint());
    }

    /**
     * the type of listen socket this alert refers to.
     *
     * @return
     */
    public SocketType getSockType() {
        return SocketType.fromSwig(alert.getSock_type());
    }

    public enum SocketType {

        TCP(listen_succeeded_alert.socket_type_t.tcp),

        TCP_SSL(listen_succeeded_alert.socket_type_t.tcp_ssl),

        UDP(listen_succeeded_alert.socket_type_t.udp);

        private SocketType(listen_succeeded_alert.socket_type_t swigObj) {
            this.swigObj = swigObj;
        }

        private final listen_succeeded_alert.socket_type_t swigObj;

        public listen_succeeded_alert.socket_type_t getSwig() {
            return swigObj;
        }

        public static SocketType fromSwig(listen_succeeded_alert.socket_type_t swigObj) {
            SocketType[] enumValues = SocketType.class.getEnumConstants();
            for (SocketType ev : enumValues) {
                if (ev.getSwig() == swigObj) {
                    return ev;
                }
            }
            throw new IllegalArgumentException("No enum " + SocketType.class + " with swig value " + swigObj);
        }
    }
}
