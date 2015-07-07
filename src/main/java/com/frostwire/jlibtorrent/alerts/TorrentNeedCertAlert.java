package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.torrent_need_cert_alert;

/**
 * This is always posted for SSL torrents. This is a reminder to the client that
 * the torrent won't work unless torrent_handle::set_ssl_certificate() is called with
 * a valid certificate. Valid certificates MUST be signed by the SSL certificate
 * in the .torrent file.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentNeedCertAlert extends TorrentAlert<torrent_need_cert_alert> {

    public TorrentNeedCertAlert(torrent_need_cert_alert alert) {
        super(alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
