package com.frostwire.jlibtorrent.alerts;

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
     * the HTTP seed that failed.
     *
     * @return
     */
    public String getUrl() {
        return alert.getUrl();
    }

    /**
     * the error message, potentially from the server.
     *
     * @return
     */
    public String getMsg() {
        return alert.getMsg();
    }
}
