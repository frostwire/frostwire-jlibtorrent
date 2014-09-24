package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.save_resume_data_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SaveResumeDataAlert extends TorrentAlert<save_resume_data_alert> {

    public SaveResumeDataAlert(save_resume_data_alert alert) {
        super(alert);
    }
    
    public entry getResumeData() {
        return alert.getResume_data();
    }
}
