package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCode {

    private error_code ec;

    public ErrorCode(error_code ec) {
        this.ec = ec;
    }

    public error_code getSwig() {
        return ec;
    }
}
