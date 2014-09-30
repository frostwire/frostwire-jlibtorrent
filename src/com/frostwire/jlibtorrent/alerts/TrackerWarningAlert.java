package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.tracker_warning_alert;

/**
 * This alert is triggered if the tracker reply contains a warning field. Usually this
 * means that the tracker announce was successful, but the tracker has a message to
 * the client.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackerWarningAlert extends TrackerAlert<tracker_warning_alert> {

    public TrackerWarningAlert(tracker_warning_alert alert) {
        super(alert);
    }

    /**
     * contains the warning message from the tracker.
     *
     * @return
     */
    public String getMsg() {
        return alert.getMsg();
    }
}
