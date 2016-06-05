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
     * @return
     */
    public Address address() {
        return new Address(alert.getAddress());
    }

    /**
     * The port libtorrent ended up listening on.
     *
     * @return
     */
    public int port() {
        return alert.getPort();
    }

    /**
     * the type of listen socket this alert refers to.
     *
     * @return
     */
    public SocketType socketType() {
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

        /**
         * @return
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue
         * @return
         */
        public static SocketType fromSwig(int swigValue) {
            SocketType[] enumValues = SocketType.class.getEnumConstants();
            for (SocketType ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
