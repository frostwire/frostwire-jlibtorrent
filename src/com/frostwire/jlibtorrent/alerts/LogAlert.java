package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.log_alert;

/**
 * This alert is posted by some session wide event. Its main purpose is
 * // trouble shooting and debugging. It's not enabled by the default alert
 * // mask and is enabled by the ``alert::session_log_notification`` bit.
 * // Furhtermore, it's by default disabled as a build configuration. To
 * // enable, build libtorrent with logging support enabled (``logging=on``
 * // with bjam or define ``TORRENT_LOGGING``).
 *
 * @author gubatron
 * @author aldenml
 */
public final class LogAlert extends AbstractAlert<log_alert> {

    public LogAlert(log_alert alert) {
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
