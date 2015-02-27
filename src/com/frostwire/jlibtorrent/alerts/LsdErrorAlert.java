package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.lsd_error_alert;

/**
 * posted if the local service discovery socket fails to start properly.
 * it's categorized as ``error_notification``.
 *
 * @author gubatron
 * @author aldenml
 */
public final class LsdErrorAlert extends AbstractAlert<lsd_error_alert> {

    public LsdErrorAlert(lsd_error_alert alert) {
        super(alert);
    }

    /**
     * The error code.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
