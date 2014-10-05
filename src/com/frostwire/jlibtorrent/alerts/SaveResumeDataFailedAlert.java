package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.save_resume_data_failed_alert;

/**
 * This alert is generated instead of ``save_resume_data_alert`` if there was an error
 * generating the resume data. ``error`` describes what went wrong.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SaveResumeDataFailedAlert extends TorrentAlert<save_resume_data_failed_alert> {

    public SaveResumeDataFailedAlert(save_resume_data_failed_alert alert) {
        super(alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
