package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
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
     * The error itself. This may indicate that the tracker sent an error
     * message (``error::tracker_failure``), in which case it can be
     * retrieved by calling {@link #errorMessage()}.
     *
     * @return
     */
//    public ErrorCode error() {
//        return new ErrorCode(alert.getError());
//    }

    /**
     * If the error indicates there is an associated message, this returns
     * that message. Otherwise and empty string.
     *
     * @return
     */
    public String errorMessage() {
        return alert.getMsg();
    }
}
