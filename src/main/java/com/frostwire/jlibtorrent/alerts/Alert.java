package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Alert<T extends alert> {

    T swig();

    /**
     * A timestamp is automatically created in the constructor (in milliseconds).
     *
     * @return the timestamp
     */
    long timestamp();

    // returns an integer that is unique to this alert type. It can be
    // compared against a specific alert by querying a static constant called ``alert_type``
    // in the alert. It can be used to determine the run-time type of an alert* in
    // order to cast to that alert type and access specific members.
    //
    // e.g:
    //
    // .. code:: c++
    //
    //	std::vector<alert*> alerts;
    //	ses.pop_alerts(&alerts);
    //	for (alert* i : alerts) {
    //		switch (a->type()) {
    //
    //			case read_piece_alert::alert_type:
    //			{
    //				read_piece_alert* p = (read_piece_alert*)a;
    //				if (p->ec) {
    //					// read_piece failed
    //					break;
    //				}
    //				// use p
    //				break;
    //			}
    //			case file_renamed_alert::alert_type:
    //			{
    //				// etc...
    //			}
    //		}
    //	}
    AlertType type();

    /**
     * Returns a string literal describing the type of the alert. It does
     * not include any information that might be bundled with the alert.
     *
     * @return
     */
    String what();

    /**
     * Generate a string describing the alert and the information bundled
     * with it. This is mainly intended for debug and development use. It is not suitable
     * to use this for applications that may be localized. Instead, handle each alert
     * type individually and extract and render the information from the alert depending
     * on the locale.
     *
     * @return
     */
    String message();

    /**
     * Returns a bitmask specifying which categories this alert belong to.
     *
     * @return the alert category
     */
    AlertCategory category();

    final class AlertCategory {

        private final alert_category_t f;

        public AlertCategory(alert_category_t f) {
            this.f = f;
        }

        public alert_category_t swig() {
            return f;
        }

        public final static AlertCategory ERROR_NOTIFICATION = new AlertCategory(alert.error_notification);
        public final static AlertCategory PEER_NOTIFICATION = new AlertCategory(alert.peer_notification);
        public final static AlertCategory PORT_MAPPING_NOTIFICATION = new AlertCategory(alert.port_mapping_notification);
        public final static AlertCategory STORAGE_NOTIFICATION = new AlertCategory(alert.storage_notification);
        public final static AlertCategory TRACKER_NOTIFICATION = new AlertCategory(alert.tracker_notification);
        public final static AlertCategory DEBUG_NOTIFICATION = new AlertCategory(alert.debug_notification);
        public final static AlertCategory STATUS_NOTIFICATION = new AlertCategory(alert.status_notification);
        public final static AlertCategory PROGRESS_NOTIFICATION = new AlertCategory(alert.progress_notification);
        public final static AlertCategory IP_BLOCK_NOTIFICATION = new AlertCategory(alert.ip_block_notification);
        public final static AlertCategory PERFORMANCE_WARNING = new AlertCategory(alert.performance_warning);
        public final static AlertCategory DHT_NOTIFICATION = new AlertCategory(alert.dht_notification);
        public final static AlertCategory STATS_NOTIFICATION = new AlertCategory(alert.stats_notification);
        public final static AlertCategory SESSION_LOG_NOTIFICATION = new AlertCategory(alert.session_log_notification);
        public final static AlertCategory TORRENT_LOG_NOTIFICATION = new AlertCategory(alert.torrent_log_notification);
        public final static AlertCategory PEER_LOG_NOTIFICATION = new AlertCategory(alert.peer_log_notification);
        public final static AlertCategory INCOMING_REQUEST_NOTIFICATION = new AlertCategory(alert.incoming_request_notification);
        public final static AlertCategory DHT_LOG_NOTIFICATION = new AlertCategory(alert.dht_log_notification);
        public final static AlertCategory DHT_OPERATION_NOTIFICATION = new AlertCategory(alert.dht_operation_notification);
        public final static AlertCategory PORT_MAPPING_LOG_NOTIFICATION = new AlertCategory(alert.port_mapping_log_notification);
        public final static AlertCategory PICKER_LOG_NOTIFICATION = new AlertCategory(alert.picker_log_notification);
        public final static AlertCategory ALL_CATEGORIES = new AlertCategory(alert.all_categories);
    }
}
