package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.lsd_error_alert;

/**
 * Posted if the local service discovery socket fails to start properly.
 *
 * @author gubatron
 * @author aldenml
 */
public final class LsdErrorAlert extends AbstractAlert<lsd_error_alert> {

    LsdErrorAlert(lsd_error_alert alert) {
        super(alert);
    }

    /**
     * The error code.
     *
     * @return the error
     */
    public ErrorCode error() {
        return new ErrorCode(alert.getError());
    }
}
