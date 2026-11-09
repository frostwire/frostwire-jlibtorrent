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
     * The kind of event, upper case: for messages, the message name, e.g. {@code "HANDSHAKE"}.
     * <p>
     * libtorrent 2.1 made it an enum ({@link #event()}); this is its name, the same string
     * libtorrent 2.0 gave for the events both versions have.
     *
     * @return the event type
     */
    public String eventType() {
        return alert.getEvent_type().toString().toUpperCase(java.util.Locale.ROOT);
    }

    /**
     * @return the kind of event (libtorrent 2.1)
     */
    public peer_log_alert.event_t event() {
        return alert.getEvent_type();
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
     * peer. The exception is  which is neither incoming or outgoing.
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
