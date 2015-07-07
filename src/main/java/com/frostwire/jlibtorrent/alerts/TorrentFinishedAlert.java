package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_finished_alert;

/**
 * This alert is generated when a torrent switches from being a downloader to a seed.
 * It will only be generated once per torrent. It contains a torrent handle to the
 * torrent in question.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentFinishedAlert extends TorrentAlert<torrent_finished_alert> {

    public TorrentFinishedAlert(torrent_finished_alert alert) {
        super(alert);
    }
}
