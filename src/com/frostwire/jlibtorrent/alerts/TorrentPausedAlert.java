package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_paused_alert;

/**
 * This alert is generated as a response to a ``torrent_handle::pause`` request. It is
 * generated once all disk IO is complete and the files in the torrent have been closed.
 * This is useful for synchronizing with the disk.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentPausedAlert extends TorrentAlert<torrent_paused_alert> {

    public TorrentPausedAlert(torrent_paused_alert alert) {
        super(alert);
    }
}
