package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.dht_pkt_alert;

/**
 * This alert is posted every time a DHT message is sent or received. It is
 * only posted if the ``alert::dht_log_notification`` alert category is
 * enabled. It contains a verbatim copy of the message.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtPktAlert extends AbstractAlert<dht_pkt_alert> {

    DhtPktAlert(dht_pkt_alert alert) {
        super(alert);
    }

    /**
     * Returns a copy of the packet buffer and size of the packet,
     * respectively. This buffer is only valid for as long as the alert itself
     * is valid, which is owned by libtorrent and reclaimed whenever
     * pop_alerts() is called on the session.
     *
     */
    @SuppressWarnings("unused")
    public byte[] pktBuf() {
        byte_vector pktBuf = alert.get_pkt_buf();
        return Vectors.byte_vector2bytes(pktBuf);
    }

    /**
     * Whether this is an incoming or outgoing packet.
     *
     * @return the direction
     */
    @SuppressWarnings("unused")
    public Direction direction() {
        return Direction.fromSwig(alert.getDirection().swigValue());
    }

    /**
     * The DHT node we received this packet from, or sent this packet to
     * (depending on ).
     *
     * @return the node endpoint
     */
    public UdpEndpoint node() {
        return new UdpEndpoint(alert.get_node());
    }

    /**
     *
     */
    public enum Direction {

        /**
         *
         */
        INCOMING(dht_pkt_alert.direction_t.incoming.swigValue()),

        /**
         *
         */
        OUTGOING(dht_pkt_alert.direction_t.outgoing.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        Direction(int swigValue) {
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
        public static Direction fromSwig(int swigValue) {
            Direction[] enumValues = Direction.class.getEnumConstants();
            for (Direction ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
