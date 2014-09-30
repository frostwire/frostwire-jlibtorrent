package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.add_torrent_alert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;

/**
 * This alert is always posted when a torrent was attempted to be added
 * and contains the return status of the add operation. The torrent handle of the new
 * torrent can be found in the base class' ``handle`` member. If adding
 * the torrent failed, ``error`` contains the error code.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AddTorrentAlert extends TorrentAlert<add_torrent_alert> {

    public AddTorrentAlert(add_torrent_alert alert) {
        super(alert);
    }

    // a copy of the parameters used when adding the torrent, it can be used
    // to identify which invocation to ``async_add_torrent()`` caused this alert.
    public add_torrent_params getParams() {
        return alert.getParams();
    }

    /**
     * set to the error, if one occurred while adding the torrent.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
