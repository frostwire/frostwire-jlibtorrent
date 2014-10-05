package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_condition;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCondition {

    private final error_condition ec;

    public ErrorCondition(error_condition ec) {
        this.ec = ec;
    }

    public error_condition getSwig() {
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

    public String message() {
        return ec.message();
    }
}
