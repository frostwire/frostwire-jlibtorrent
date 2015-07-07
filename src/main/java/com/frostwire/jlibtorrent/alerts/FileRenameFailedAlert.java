package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.file_rename_failed_alert;

/**
 * This is posted as a response to a torrent_handle::rename_file() call, if the rename
 * operation failed.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileRenameFailedAlert extends TorrentAlert<file_rename_failed_alert> {

    public FileRenameFailedAlert(file_rename_failed_alert alert) {
        super(alert);
    }

    /**
     * refers to the index of the file that was supposed to be renamed.
     *
     * @return
     */
    public int getIndex() {
        return alert.getIndex();
    }

    /**
     * is the error code returned from the filesystem.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
