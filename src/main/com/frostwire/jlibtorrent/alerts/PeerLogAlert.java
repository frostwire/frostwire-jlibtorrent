package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_log_alert;

/**
 * This alert is posted by events specific to a peer. It's meant to be used
 * for trouble shooting and debugging. It's not enabled by the default alert
 * mask and is enabled by the ``alert::peer_log_notification`` bit. By
 * default it is disabled as a build configuration. To enable, build
 * libtorrent with logging support enabled (``logging=on`` with bjam or
 * define ``TORRENT_LOGGING``).
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerLogAlert extends PeerAlert<peer_log_alert> {

    public PeerLogAlert(peer_log_alert alert) {
        super(alert);
    }

    /**
     * String literal indicating the kind of event. For messages, this is the
     * message name.
     *
     * @return
     */
    public String eventType() {
        return alert.getEvent_type();
    }

    public Direction direction() {
        return Direction.fromSwig(alert.getDirection().swigValue());
    }

    /**
     * Returns the log message.
     *
     * @return
     */
    public String msg() {
        return alert.msg();
    }

    public enum Direction {

        INCOMING_MESSAGE(peer_log_alert.direction_t.incoming_message.swigValue()),
        OUTGOING_MESSAGE(peer_log_alert.direction_t.outgoing_message.swigValue()),
        INCOMING(peer_log_alert.direction_t.incoming.swigValue()),
        OUTGOING(peer_log_alert.direction_t.outgoing.swigValue()),
        INFO(peer_log_alert.direction_t.info.swigValue()),
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
