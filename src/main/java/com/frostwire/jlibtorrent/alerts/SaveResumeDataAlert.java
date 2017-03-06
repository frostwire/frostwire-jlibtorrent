package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.save_resume_data_alert;

/**
 * This alert is generated as a response to a ``torrent_handle::save_resume_data`` request.
 * It is generated once the disk IO thread is done writing the state for this torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SaveResumeDataAlert extends TorrentAlert<save_resume_data_alert> {

    SaveResumeDataAlert(save_resume_data_alert alert) {
        super(alert);
    }

    /**
     * The {@code params} structure is populated with the fields to be passed to
     * {@link com.frostwire.jlibtorrent.SessionHandle#addTorrent(AddTorrentParams, ErrorCode)}
     * or {@link com.frostwire.jlibtorrent.SessionHandle#asyncAddTorrent(AddTorrentParams)}
     * to resume the torrent. To save the state to disk, you may pass it
     * on to {@code write_resume_data()}.
     *
     * @return the params object
     */
    public AddTorrentParams params() {
        return new AddTorrentParams(alert.getParams());
    }
}
