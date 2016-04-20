package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.torrent_error_alert;

/**
 * This is posted whenever a torrent is transitioned into the error state.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentErrorAlert extends TorrentAlert<torrent_error_alert> {

    TorrentErrorAlert(torrent_error_alert alert) {
        super(alert);
    }

    /**
     * Specifies which error the torrent encountered.
     *
     * @return
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }

    /**
     * Returns the filename (or object) the error occurred on.
     *
     * @return
     */
    public String filename() {
        return alert.filename();
    }
}
