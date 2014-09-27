package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_completed_alert;

/**
 * This is posted whenever an individual file completes its download. i.e.
 * All pieces overlapping this file have passed their hash check.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileCompletedAlert extends TorrentAlert<file_completed_alert> {

    public FileCompletedAlert(file_completed_alert alert) {
        super(alert);
    }

    /**
     * Refers to the index of the file that completed.
     *
     * @return
     */
    public int getIndex() {
        return alert.getIndex();
    }
}
