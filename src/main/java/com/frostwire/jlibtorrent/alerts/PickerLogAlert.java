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

    public static final class PickerFlags {

        private final picker_flags_t f;

        private PickerFlags(picker_flags_t f) {
            this.f = f;
        }

        public picker_flags_t swig() {
            return f;
        }

        public static final PickerFlags PARTIAL_RATIO = new PickerFlags(picker_log_alert.partial_ratio);
        public static final PickerFlags PRIORITIZE_PARTIALS = new PickerFlags(picker_log_alert.prioritize_partials);
        public static final PickerFlags RAREST_FIRST_PARTIALS = new PickerFlags(picker_log_alert.rarest_first_partials);
        public static final PickerFlags RAREST_FIRST = new PickerFlags(picker_log_alert.rarest_first);
        public static final PickerFlags REVERSE_RAREST_FIRST = new PickerFlags(picker_log_alert.reverse_rarest_first);
        public static final PickerFlags SUGGESTED_PIECES = new PickerFlags(picker_log_alert.suggested_pieces);
        public static final PickerFlags PRIO_SEQUENTIAL_PIECES = new PickerFlags(picker_log_alert.prio_sequential_pieces);
        public static final PickerFlags SEQUENTIAL_PIECES = new PickerFlags(picker_log_alert.sequential_pieces);
        public static final PickerFlags REVERSE_PIECES = new PickerFlags(picker_log_alert.time_critical);
        public static final PickerFlags TIME_CRITICAL = new PickerFlags(picker_log_alert.time_critical);
        public static final PickerFlags RANDOM_PIECES = new PickerFlags(picker_log_alert.random_pieces);
        public static final PickerFlags PREFER_CONTIGUOUS = new PickerFlags(picker_log_alert.prefer_contiguous);
        public static final PickerFlags REVERSE_SEQUENTIAL = new PickerFlags(picker_log_alert.reverse_sequential);
        public static final PickerFlags BACKUP1 = new PickerFlags(picker_log_alert.backup1);
        public static final PickerFlags BACKUP2 = new PickerFlags(picker_log_alert.backup2);
        public static final PickerFlags END_GAME = new PickerFlags(picker_log_alert.end_game);
    }

    /**
     * This is a bitmask of which features were enabled for this particular
     * pick. The bits are defined in the picker_flags_t enum.
     *
     * @return the flags
     */
    public PickerFlags pickerFlags() {
        return new PickerFlags(alert.getPicker_flags());
    }
}
