package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.listen_failed_alert;

/**
 * This alert is generated when none of the ports, given in the port range, to
 * session can be opened for listening. The {@link #endpoint()} member is the
 * interface and port that failed, {@link #getError()} is the error code describing
 * the failure.
 * <p/>
 * libtorrent may sometimes try to listen on port 0, if all other ports failed.
 * Port 0 asks the operating system to pick a port that's free). If that fails
 * you may see a {@link ListenFailedAlert} with port 0 even if you didn't ask to
 * listen on it.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ListenFailedAlert extends AbstractAlert<listen_failed_alert> {

    public ListenFailedAlert(listen_failed_alert alert) {
        super(alert);
    }

    /**
     * The interface libtorrent attempted to listen on that failed.
     *
     * @return
     */
    public String listenInterface() {
        return alert.listen_interface();
    }

    /**
     * The error the system returned.
     *
     * @return
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }

    /**
     * The specific low level operation that failed.
     *
     * @return
     * @see com.frostwire.jlibtorrent.alerts.ListenFailedAlert.Operation
     */
    public Operation operation() {
        return Operation.fromSwig(alert.getOperation());
    }

    /**
     * The type of listen socket this alert refers to.
     *
     * @return
     * @see com.frostwire.jlibtorrent.alerts.ListenFailedAlert.SocketType
     */
    public SocketType socketType() {
        return SocketType.fromSwig(alert.getSock_type().swigValue());
    }

    /**
     * The address and port libtorrent attempted to listen on.
     *
     * @return
     */
    public TcpEndpoint endpoint() {
        return new TcpEndpoint(alert.getEndpoint());
    }

    public Address address() {
        return endpoint().address();
    }

    public int port() {
        return endpoint().port();
    }

    public enum SocketType {

        TCP(listen_failed_alert.socket_type_t.tcp.swigValue()),
        TCP_SSL(listen_failed_alert.socket_type_t.tcp_ssl.swigValue()),
        UDP(listen_failed_alert.socket_type_t.udp.swigValue()),
        I2P(listen_failed_alert.socket_type_t.i2p.swigValue()),
        SOCKS5(listen_failed_alert.socket_type_t.socks5.swigValue()),
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

    public enum Operation {

        PARSE_ADDRESS(listen_failed_alert.op_t.parse_addr.swigValue()),
        OPEN(listen_failed_alert.op_t.open.swigValue()),
        BIND(listen_failed_alert.op_t.bind.swigValue()),
        LISTEN(listen_failed_alert.op_t.listen.swigValue()),
        GET_PEER_NAME(listen_failed_alert.op_t.get_peer_name.swigValue()),
        ACCEPT(listen_failed_alert.op_t.accept.swigValue()),
        UNKNOWN(-1);

        Operation(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Operation fromSwig(int swigValue) {
            Operation[] enumValues = Operation.class.getEnumConstants();
            for (Operation ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
