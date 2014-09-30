package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.tracker_error_alert;

/**
 * This alert is generated on tracker time outs, premature disconnects, invalid response or
 * a HTTP response other than "200 OK". From the alert you can get the handle to the torrent
 * the tracker belongs to.
 * <p/>
 * The ``times_in_row`` member says how many times in a row this tracker has failed.
 * ``status_code`` is the code returned from the HTTP server. 401 means the tracker needs
 * authentication, 404 means not found etc. If the tracker timed out, the code will be set
 * to 0.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackerErrorAlert extends TrackerAlert<tracker_error_alert> {

    public TrackerErrorAlert(tracker_error_alert alert) {
        super(alert);
    }

    public int getTimesInRow() {
        return alert.getTimes_in_row();
    }

    public int getStatusCode() {
        return alert.getStatus_code();
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }

    public String getMsg() {
        return alert.getMsg();
    }
}
