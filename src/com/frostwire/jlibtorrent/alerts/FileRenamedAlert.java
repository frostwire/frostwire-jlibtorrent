package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_renamed_alert;

/**
 * This is posted as a response to a torrent_handle::rename_file() call, if the rename
 * operation succeeds.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileRenamedAlert extends TorrentAlert<file_renamed_alert> {

    public FileRenamedAlert(file_renamed_alert alert) {
        super(alert);
    }

    public String getName() {
        return alert.getName();
    }

    /**
     * Refers to the index of the file that was renamed,
     * ``name`` is the new name of the file.
     *
     * @return
     */
    public int getIndex() {
        return alert.getIndex();
    }
}
