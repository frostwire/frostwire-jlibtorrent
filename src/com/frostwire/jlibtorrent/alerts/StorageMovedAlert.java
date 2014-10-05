package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.storage_moved_alert;

/**
 * The ``storage_moved_alert`` is generated when all the disk IO has completed and the
 * files have been moved, as an effect of a call to ``torrent_handle::move_storage``. This
 * is useful to synchronize with the actual disk. The ``path`` member is the new path of
 * the storage.
 *
 * @author gubatron
 * @author aldenml
 */
public final class StorageMovedAlert extends TorrentAlert<storage_moved_alert> {

    public StorageMovedAlert(storage_moved_alert alert) {
        super(alert);
    }

    public String getPath() {
        return alert.getPath();
    }
}
