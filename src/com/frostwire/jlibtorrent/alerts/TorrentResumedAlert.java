package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_resumed_alert;

/**
 * This alert is generated as a response to a torrent_handle::resume() request. It is
 * generated when a torrent goes from a paused state to an active state.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentResumedAlert extends TorrentAlert<torrent_resumed_alert> {

    public TorrentResumedAlert(torrent_resumed_alert alert) {
        super(alert);
    }
}
