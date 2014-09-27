package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public enum AlertType {

    TORRENT_ADDED(torrent_added_alert.alert_type),
    TORRENT_FINISHED(torrent_finished_alert.alert_type),
    TORRENT_REMOVED(torrent_removed_alert.alert_type),
    TORRENT_UPDATE(torrent_update_alert.alert_type),
    SAVE_RESUME_DATA(save_resume_data_alert.alert_type),
    BLOCK_FINISHED(block_finished_alert.alert_type),
    METADATA_RECEIVED(metadata_received_alert.alert_type),
    METADATA_FAILED(metadata_failed_alert.alert_type),
    FILE_ERROR(file_error_alert.alert_type),
    PORTMAP(portmap_alert.alert_type),
    PORTMAP_ERROR(portmap_error_alert.alert_type),
    PORTMAP_LOG(portmap_log_alert.alert_type),
    TRACKER_ANNOUNCE(tracker_announce_alert.alert_type),
    READ_PIECE(read_piece_alert.alert_type),
    UNKNOWN(Integer.MAX_VALUE);

    private AlertType(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int getSwig() {
        return swigValue;
    }

    public static AlertType fromSwig(int swigValue) {
        AlertType[] enumValues = AlertType.class.getEnumConstants();
        for (AlertType ev : enumValues) {
            if (ev.getSwig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
