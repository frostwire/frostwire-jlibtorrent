package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_log_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtLogAlert extends AbstractAlert<dht_log_alert> {

    DhtLogAlert(dht_log_alert alert) {
        super(alert);
    }

    /**
     * The log message.
     *
     * @return
     */
    public String logMessage() {
        return alert.log_message();
    }

    /**
     * The module, or part, of the DHT that produced this log message.
     *
     * @return the dht module
     */
    public DhtModule module() {
        return DhtModule.fromSwig(alert.getModule().swigValue());
    }

    /**
     *
     */
    public enum DhtModule {

        TRACKER(dht_log_alert.dht_module_t.tracker.swigValue()),
        NODE(dht_log_alert.dht_module_t.node.swigValue()),
        ROUTING_TABLE(dht_log_alert.dht_module_t.routing_table.swigValue()),
        RPC_MANAGER(dht_log_alert.dht_module_t.rpc_manager.swigValue()),
        TRAVERSAL(dht_log_alert.dht_module_t.traversal.swigValue()),
        UNKNOWN(-1);

        DhtModule(int swigValue) {
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
        public static DhtModule fromSwig(int swigValue) {
            DhtModule[] enumValues = DhtModule.class.getEnumConstants();
            for (DhtModule ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
