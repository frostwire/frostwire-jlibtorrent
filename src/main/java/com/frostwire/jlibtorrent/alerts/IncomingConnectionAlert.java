package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.incoming_connection_alert;

/**
 * The incoming connection alert is posted every time we successfully accept
 * an incoming connection, through any mean. The most straigh-forward ways
 * of accepting incoming connections are through the TCP listen socket and
 * the UDP listen socket for uTP sockets. However, connections may also be
 * accepted ofer a Socks5 or i2p listen socket, or via a torrent specific
 * listen socket for SSL torrents.
 *
 * @author gubatron
 * @author aldenml
 */
public final class IncomingConnectionAlert extends AbstractAlert<incoming_connection_alert> {

    IncomingConnectionAlert(incoming_connection_alert alert) {
        super(alert);
    }

    /**
     * Tells you what kind of socket the connection was accepted.
     *
     * @return the socket type
     */
    public SocketType socketType() {
        return SocketType.fromSwig(alert.getSocket_type());
    }

    /**
     * It is the IP address and port the connection came from.
     *
     * @return the endpoint
     */
    public TcpEndpoint endpoint() {
        return new TcpEndpoint(alert.get_endpoint());
    }

    /**
     *
     */
    public enum SocketType {

        /**
         * no socket instantiated.
         */
        NONE(0),

        /**
         *
         */
        TCP(1),

        /**
         *
         */
        SOCKS5(2),

        /**
         *
         */
        HTTP(3),

        /**
         *
         */
        UTP(4),

        /**
         *
         */
        I2P(5),

        /**
         *
         */
        SSL_TCP(6),

        /**
         *
         */
        SSL_SOCKS5(7),

        /**
         * Like SSL_HTTP.
         */
        HTTPS(8),

        /**
         *
         */
        SSL_UTP(9),

        /**
         *
         */
        UNKNOWN(-1);

        SocketType(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the native value
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue the native value
         * @return the java enum
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
