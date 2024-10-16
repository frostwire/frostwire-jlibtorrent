package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_log_alert;

/**
 * This alert is posted by events specific to a peer. It's meant to be used
 * for trouble shooting and debugging. It's not enabled by the default alert
 * mask and is enabled by the ``alert::peer_log_notification`` bit.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerLogAlert extends PeerAlert<peer_log_alert> {

    PeerLogAlert(peer_log_alert alert) {
        super(alert);
    }

    /**
     * String literal indicating the kind of event. For messages, this is the
     * message name.
     *
     * @return the event type
     */
    public String eventType() {
        return alert.get_event_type();
    }

    /**
     * @return the direction
     */
    public Direction direction() {
        return Direction.fromSwig(alert.getDirection().swigValue());
    }

    /**
     * Returns the log message.
     *
     * @return the log message
     */
    public String logMessage() {
        return alert.log_message();
    }

    /**
     * Describes whether this log refers to in-flow or out-flow of the
     * peer. The exception is {@link #INFO} which is neither incoming or outgoing.
     */
    public enum Direction {

        /**
         *
         */
        INCOMING_MESSAGE(peer_log_alert.direction_t.incoming_message.swigValue()),

        /**
         *
         */
        OUTGOING_MESSAGE(peer_log_alert.direction_t.outgoing_message.swigValue()),

        /**
         *
         */
        INCOMING(peer_log_alert.direction_t.incoming.swigValue()),

        /**
         *
         */
        OUTGOING(peer_log_alert.direction_t.outgoing.swigValue()),

        /**
         *
         */
        INFO(peer_log_alert.direction_t.info.swigValue()),

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
         * @param swigValue
         * @return
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
