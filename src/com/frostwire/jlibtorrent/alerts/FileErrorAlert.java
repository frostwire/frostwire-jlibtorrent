package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_error_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class FileErrorAlert extends TorrentAlert<file_error_alert> {

    public FileErrorAlert(file_error_alert alert) {
        super(alert);
    }
}
