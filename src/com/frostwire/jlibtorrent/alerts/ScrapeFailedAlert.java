package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.scrape_failed_alert;

/**
 * If a scrape request fails, this alert is generated. This might be due
 * to the tracker timing out, refusing connection or returning an http response
 * code indicating an error.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ScrapeFailedAlert extends TrackerAlert<scrape_failed_alert> {

    public ScrapeFailedAlert(scrape_failed_alert alert) {
        super(alert);
    }

    /**
     * contains a message describing the error.
     *
     * @return
     */
    public String getMsg() {
        return alert.getMsg();
    }
}
