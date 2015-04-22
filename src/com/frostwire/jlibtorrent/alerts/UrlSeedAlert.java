package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.url_seed_alert;

/**
 * This alert is generated when a HTTP seed name lookup fails.
 *
 * @author gubatron
 * @author aldenml
 */
public final class UrlSeedAlert extends TorrentAlert<url_seed_alert> {

    public UrlSeedAlert(url_seed_alert alert) {
        super(alert);
    }

    /**
     * The error the web seed encountered. If this is not set, the server
     * sent an error message, call {@link #errorMessage()}.
     *
     * @return
     */
//    public ErrorCode error() {
//        return new ErrorCode(alert.getError());
//    }

    /**
     * The URL the error is associated with.
     *
     * @return
     */
    public String serverUrl() {
        return alert.getUrl();
    }

    /**
     * In case the web server sent an error message, this function returns
     * it.
     *
     * @return
     */
    public String errorMessage() {
        return alert.getMsg();
    }
}
