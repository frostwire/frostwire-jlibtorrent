package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.torrent_log_alert;

/**
 * This alert is posted by torrent wide events. It's meant to be used for
 * trouble shooting and debugging. It's not enabled by the default alert
 * mask and is enabled by the ``alert::torrent_log_notification`` bit.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentLogAlert extends TorrentAlert<torrent_log_alert> {

    TorrentLogAlert(torrent_log_alert alert) {
        super(alert);
    }

    /**
     * The log message.
     *
     * @return the log message
     */
    public String logMessage() {
        return alert.log_message();
    }
}
