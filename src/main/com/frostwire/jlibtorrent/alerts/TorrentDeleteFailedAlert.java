package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.torrent_delete_failed_alert;

/**
 * This alert is generated when a request to delete the files of a torrent fails.
 * Just removing a torrent from the session cannot fail
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentDeleteFailedAlert extends TorrentAlert<torrent_delete_failed_alert> {

    public TorrentDeleteFailedAlert(torrent_delete_failed_alert alert) {
        super(alert);
    }

    /**
     * tells you why it failed.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }

    /**
     * the info hash of the torrent whose files failed to be deleted.
     *
     * @return
     */
    public Sha1Hash getInfoHash() {
        return new Sha1Hash(alert.getInfo_hash());
    }
}
