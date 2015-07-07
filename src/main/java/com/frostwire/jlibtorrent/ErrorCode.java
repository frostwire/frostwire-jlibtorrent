package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCode {

    private final error_code ec;

    public ErrorCode(error_code ec) {
        this.ec = ec;
    }

    public error_code getSwig() {
        return ec;
    }

    public void clear() {
        ec.clear();
    }

    public int value() {
        return ec.value();
    }

    public ErrorCategory category() {
        return new ErrorCategory(ec.category());
    }

    public ErrorCondition defaultErrorCondition() {
        return new ErrorCondition(ec.default_error_condition());
    }

    public String message() {
        return ec.message();
    }
}
