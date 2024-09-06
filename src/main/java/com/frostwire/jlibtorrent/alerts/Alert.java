package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Alert<T extends alert> {

    T swig();

    alert_category_t ERROR_NOTIFICATION = alert.error_notification;
    alert_category_t PEER_NOTIFICATION = alert.peer_notification;
    alert_category_t PORT_MAPPING_NOTIFICATION = alert.port_mapping_notification;
    alert_category_t STORAGE_NOTIFICATION = alert.storage_notification;
    alert_category_t TRACKER_NOTIFICATION = alert.tracker_notification;
    alert_category_t CONNECT_NOTIFICATION = alert.connect_notification;
    alert_category_t STATUS_NOTIFICATION = alert.status_notification;
    alert_category_t IP_BLOCK_NOTIFICATION = alert.ip_block_notification;
    alert_category_t PERFORMANCE_WARNING = alert.performance_warning;
    alert_category_t DHT_NOTIFICATION = alert.dht_notification;
    alert_category_t SESSION_LOG_NOTIFICATION = alert.session_log_notification;
    alert_category_t TORRENT_LOG_NOTIFICATION = alert.torrent_log_notification;
    alert_category_t PEER_LOG_NOTIFICATION = alert.peer_log_notification;
    alert_category_t INCOMING_REQUEST_NOTIFICATION = alert.incoming_request_notification;
    alert_category_t DHT_LOG_NOTIFICATION = alert.dht_log_notification;
    alert_category_t DHT_OPERATION_NOTIFICATION = alert.dht_operation_notification;
    alert_category_t PORT_MAPPING_LOG_NOTIFICATION = alert.port_mapping_log_notification;
    alert_category_t PICKER_LOG_NOTIFICATION = alert.picker_log_notification;
    alert_category_t FILE_PROGRESS_NOTIFICATION = alert.file_progress_notification;
    alert_category_t PIECE_PROGRESS_NOTIFICATION = alert.piece_progress_notification;
    alert_category_t UPLOAD_NOTIFICATION = alert.upload_notification;
    alert_category_t BLOCK_PROGRESS_NOTIFICATION = alert.block_progress_notification;
    alert_category_t ALL_CATEGORIES = alert.all_categories;

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
    alert_category_t category();
}
