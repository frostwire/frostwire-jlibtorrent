package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.dht_error_alert;

/**
 * Posted when something fails in the DHT. This is not necessarily a fatal
 * error, but it could prevent proper operation.
 *
 * @author gubatron
 * @author aldenml
 */
public final class DhtErrorAlert extends AbstractAlert<dht_error_alert> {

    public DhtErrorAlert(dht_error_alert alert) {
        super(alert);
    }

    /**
     * The error code.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }

    /**
     * the operation that failed
     *
     * @return
     */
    public Operation getOperation() {
        return Operation.fromSwig(alert.getOperation());
    }

    public enum Operation {

        UNKNOWN(dht_error_alert.op_t.unknown),

        HOSTNAME_LOOKUP(dht_error_alert.op_t.hostname_lookup);

        private Operation(dht_error_alert.op_t swigObj) {
            this.swigObj = swigObj;
        }

        private final dht_error_alert.op_t swigObj;

        public dht_error_alert.op_t getSwig() {
            return swigObj;
        }

        public static Operation fromSwig(dht_error_alert.op_t swigObj) {
            Operation[] enumValues = Operation.class.getEnumConstants();
            for (Operation ev : enumValues) {
                if (ev.getSwig() == swigObj) {
                    return ev;
                }
            }
            throw new IllegalArgumentException("No enum " + Operation.class + " with swig value " + swigObj);
        }
    }
}
