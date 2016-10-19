package com.frostwire.jlibtorrent.alerts;

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

    /**
     * This is a bitmask of which features were enabled for this particular
     * pick. The bits are defined in the picker_flags_t enum.
     *
     * @return the flags
     */
    public long pickerFlags() {
        return alert.getPicker_flags();
    }
}
