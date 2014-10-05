package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.storage_moved_failed_alert;

/**
 * The ``storage_moved_failed_alert`` is generated when an attempt to move the storage,
 * via torrent_handle::move_storage(), fails.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StorageMovedFailedAlert extends TorrentAlert<storage_moved_failed_alert> {

    public StorageMovedFailedAlert(storage_moved_failed_alert alert) {
        super(alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
