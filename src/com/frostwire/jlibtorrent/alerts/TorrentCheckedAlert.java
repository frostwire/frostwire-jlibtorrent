package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_checked_alert;

/**
 * This alert is posted when a torrent completes checking. i.e. when it transitions
 * out of the ``checking files`` state into a state where it is ready to start downloading
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentCheckedAlert extends TorrentAlert<torrent_checked_alert> {

    public TorrentCheckedAlert(torrent_checked_alert alert) {
        super(alert);
    }
}
