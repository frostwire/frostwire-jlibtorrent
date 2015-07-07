package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.scrape_reply_alert;

/**
 * This alert is generated when a scrape request succeeds.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ScrapeReplyAlert extends TrackerAlert<scrape_reply_alert> {

    public ScrapeReplyAlert(scrape_reply_alert alert) {
        super(alert);
    }

    /**
     * the data returned in the scrape response. These numbers
     * may be -1 if the reponse was malformed.
     *
     * @return
     */
    public int getIncomplete() {
        return alert.getIncomplete();
    }

    /**
     * the data returned in the scrape response. These numbers
     * may be -1 if the reponse was malformed.
     *
     * @return
     */
    public int getComplete() {
        return alert.getComplete();
    }
}
