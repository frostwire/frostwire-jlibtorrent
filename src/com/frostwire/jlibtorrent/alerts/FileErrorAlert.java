package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.file_error_alert;

/**
 * If the storage fails to read or write files that it needs access to, this alert is
 * generated and the torrent is paused.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileErrorAlert extends TorrentAlert<file_error_alert> {

    public FileErrorAlert(file_error_alert alert) {
        super(alert);
    }

    /**
     * The path to the file that was accessed when the error occurred.
     *
     * @return
     */
    public String getFile() {
        return alert.getFile();
    }

    /**
     * The error code describing the error.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
