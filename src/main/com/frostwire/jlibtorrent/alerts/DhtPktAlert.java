package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.UdpEndpoint;
import com.frostwire.jlibtorrent.swig.dht_pkt_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtPktAlert extends AbstractAlert<dht_pkt_alert> {

    public DhtPktAlert(dht_pkt_alert alert) {
        super(alert);
    }

    /**
     * Returns a pointer to the packet buffer and size of the packet,
     * respectively. This buffer is only valid for as long as the alert itself
     * is valid, which is owned by libtorrent and reclaimed whenever
     * pop_alerts() is called on the session.
     *
     * @return
     */
    public byte[] pktBuf() {
        throw new UnsupportedOperationException("Pending implementation.");
    }

    /**
     * @return
     */
    public int pktSize() {
        return alert.pkt_size();
    }

    /**
     * Whether this is an incoming or outgoing packet.
     *
     * @return
     */
    public Direction dir() {
        return Direction.fromSwig(alert.getDir().swigValue());
    }

    /**
     * The DHT node we received this packet from, or sent this packet to
     * (depending on {@link #dir()}).
     *
     * @return
     */
    public UdpEndpoint node() {
        return new UdpEndpoint(alert.getNode());
    }

    public enum Direction {

        INCOMING(dht_pkt_alert.direction_t.incoming.swigValue()),
        OUTGOING(dht_pkt_alert.direction_t.outgoing.swigValue()),
        UNKNOWN(-1);

        Direction(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Direction fromSwig(int swigValue) {
            Direction[] enumValues = Direction.class.getEnumConstants();
            for (Direction ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
