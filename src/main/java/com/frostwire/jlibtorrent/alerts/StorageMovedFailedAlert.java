package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.storage_moved_failed_alert;

/**
 * This alert is generated when an attempt to move the storage,
 * via {@code TorrentHandle#moveStorage} fails.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StorageMovedFailedAlert extends TorrentAlert<storage_moved_failed_alert> {

    StorageMovedFailedAlert(storage_moved_failed_alert alert) {
        super(alert);
    }

    /**
     * @return the error.
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }

    /**
     * If the error happened for a specific file, this returns its path.
     *
     * @return the file path.
     */
    public String filePath() {
        return alert.file_path();
    }

    /**
     * This indicates what underlying operation caused the error.
     *
     * @return the operation.
     */
    public Operation operation() {
        return Operation.fromSwig(alert.getOp());
    }
}
