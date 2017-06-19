package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.file_error_alert;

/**
 * If the storage fails to read or write files that it needs access
 * to, this alert is generated and the torrent is paused.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileErrorAlert extends TorrentAlert<file_error_alert> {

    FileErrorAlert(file_error_alert alert) {
        super(alert);
    }

    /**
     * The error code describing the error.
     *
     * @return the error.
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }

    /**
     * The file that experienced the error.
     *
     * @return te filename.
     */
    public String filename() {
        return alert.filename();
    }

    /**
     * Indicates which underlying operation caused the error.
     *
     * @return the operation.
     */
    public Operation operation() {
        return Operation.fromSwig(alert.getOp());
    }
}
