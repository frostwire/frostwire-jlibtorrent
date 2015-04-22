package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_renamed_alert;

/**
 * This is posted as a response to a {@link com.frostwire.jlibtorrent.TorrentHandle#renameFile(int, String)}, if the rename
 * operation succeeds.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileRenamedAlert extends TorrentAlert<file_renamed_alert> {

    public FileRenamedAlert(file_renamed_alert alert) {
        super(alert);
    }

    public String newName() {
        return alert.getName();
    }

    /**
     * Refers to the index of the file that was renamed.
     *
     * @return
     */
    public int index() {
        return alert.getIndex();
    }
}
