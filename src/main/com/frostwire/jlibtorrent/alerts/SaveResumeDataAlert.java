package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.swig.save_resume_data_alert;

/**
 * This alert is generated as a response to a ``torrent_handle::save_resume_data`` request.
 * It is generated once the disk IO thread is done writing the state for this torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SaveResumeDataAlert extends TorrentAlert<save_resume_data_alert> {

    public SaveResumeDataAlert(save_resume_data_alert alert) {
        super(alert);
    }

    /**
     * points to the resume data.
     *
     * @return
     */
    public Entry getResumeData() {
        return new Entry(alert.getResume_data());
    }
}
