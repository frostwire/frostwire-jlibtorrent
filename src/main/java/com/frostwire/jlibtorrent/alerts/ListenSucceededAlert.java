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
    public SocketType getSocketType() {
        return SocketType.fromSwig(alert.getSock_type().swigValue());
    }

    /**
     *
     */
    public enum SocketType {

        /**
         *
         */
        TCP(listen_succeeded_alert.socket_type_t.tcp.swigValue()),

        /**
         *
         */
        TCP_SSL(listen_succeeded_alert.socket_type_t.tcp_ssl.swigValue()),

        /**
         *
         */
        UDP(listen_succeeded_alert.socket_type_t.udp.swigValue()),

        /**
         *
         */
        I2P(listen_succeeded_alert.socket_type_t.i2p.swigValue()),

        /**
         *
         */
        SOCKS5(listen_succeeded_alert.socket_type_t.socks5.swigValue()),

        /**
         *
         */
        UTP_SSL(listen_succeeded_alert.socket_type_t.utp_ssl.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        SocketType(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static SocketType fromSwig(int swigValue) {
            SocketType[] enumValues = SocketType.class.getEnumConstants();
            for (SocketType ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
