package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.i2p_alert;

/**
 * this alert is used to report errors in the i2p SAM connection.
 *
 * @author gubatron
 * @author aldenml
 */
public final class I2pAlert extends AbstractAlert<i2p_alert> {

    public I2pAlert(i2p_alert alert) {
        super(alert);
    }

    /**
     * the error that occurred in the i2p SAM connection.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
