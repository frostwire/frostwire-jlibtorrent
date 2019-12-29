package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.picker_flags_t;
import com.frostwire.jlibtorrent.swig.picker_log_alert;

/**
 * This is posted when one or more blocks are picked by the piece picker,
 * assuming the verbose piece picker logging is enabled (see
 * picker_log_notification).
 *
 * @author gubatron
 * @author aldenml
 */
public final class PickerLogAlert extends PeerAlert<picker_log_alert> {

    PickerLogAlert(picker_log_alert alert) {
        super(alert);
    }

    public static final picker_flags_t PARTIAL_RATIO = picker_log_alert.partial_ratio;
    public static final picker_flags_t PRIORITIZE_PARTIALS = picker_log_alert.prioritize_partials;
    public static final picker_flags_t RAREST_FIRST_PARTIALS = picker_log_alert.rarest_first_partials;
    public static final picker_flags_t RAREST_FIRST = picker_log_alert.rarest_first;
    public static final picker_flags_t REVERSE_RAREST_FIRST = picker_log_alert.reverse_rarest_first;
    public static final picker_flags_t SUGGESTED_PIECES = picker_log_alert.suggested_pieces;
    public static final picker_flags_t PRIO_SEQUENTIAL_PIECES = picker_log_alert.prio_sequential_pieces;
    public static final picker_flags_t SEQUENTIAL_PIECES = picker_log_alert.sequential_pieces;
    public static final picker_flags_t REVERSE_PIECES = picker_log_alert.time_critical;
    public static final picker_flags_t TIME_CRITICAL = picker_log_alert.time_critical;
    public static final picker_flags_t RANDOM_PIECES = picker_log_alert.random_pieces;
    public static final picker_flags_t PREFER_CONTIGUOUS = picker_log_alert.prefer_contiguous;
    public static final picker_flags_t REVERSE_SEQUENTIAL = picker_log_alert.reverse_sequential;
    public static final picker_flags_t BACKUP1 = picker_log_alert.backup1;
    public static final picker_flags_t BACKUP2 = picker_log_alert.backup2;
    public static final picker_flags_t END_GAME = picker_log_alert.end_game;
    public static final picker_flags_t EXTENT_AFFINITY = picker_log_alert.extent_affinity;
    
    /**
     * This is a bitmask of which features were enabled for this particular
     * pick. The bits are defined in the picker_flags_t enum.
     *
     * @return the flags
     */
    public picker_flags_t pickerFlags() {
        return alert.getPicker_flags();
    }
}
