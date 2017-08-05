package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;

/**
 * @author gubatron
 * @author aldenml
 */
public final class AlertCategory {

    private final alert_category_t f;

    AlertCategory(alert_category_t f) {
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
