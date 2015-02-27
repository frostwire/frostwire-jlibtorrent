package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_log_alert;

/**
 * This alert is posted by events specific to a peer. It's meant to be used
 * // for trouble shooting and debugging. It's not enabled by the default alert
 * // mask and is enabled by the ``alert::peer_log_notification`` bit. By
 * // default it is disabled as a build configuration. To enable, build
 * // libtorrent with logging support enabled (``logging=on`` with bjam or
 * // define ``TORRENT_LOGGING``).
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerLogAlert extends PeerAlert<peer_log_alert> {

    public PeerLogAlert(peer_log_alert alert) {
        super(alert);
    }

    /**
     * The log message.
     *
     * @return
     */
    public String getMsg() {
        return alert.getMsg();
    }
}
