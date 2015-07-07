package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.mmap_cache_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class MmapCacheAlert extends AbstractAlert<mmap_cache_alert> {

    public MmapCacheAlert(mmap_cache_alert alert) {
        super(alert);
    }

    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }
}
