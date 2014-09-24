package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_added_alert;

/**
 * The TorrentAddedAlert is posted once every time a torrent is successfully
 * added. It doesn't contain any members of its own, but inherits the torrent handle
 * from its base class.
 * It's posted when the ``status_notification`` bit is set in the alert_mask.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentAddedAlert extends TorrentAlert<torrent_added_alert> {

    public TorrentAddedAlert(torrent_added_alert alert) {
        super(alert);
    }
}
