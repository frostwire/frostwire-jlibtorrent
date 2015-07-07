package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.fastresume_rejected_alert;

/**
 * This alert is generated when a fastresume file has been passed to add_torrent() but the
 * files on disk did not match the fastresume file. The error_code explains the reason why the
 * resume file was rejected.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FastresumeRejectedAlert extends TorrentAlert<fastresume_rejected_alert> {

    public FastresumeRejectedAlert(fastresume_rejected_alert alert) {
        super(alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
