package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_category;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCategory {

    private final error_category ec;

    public ErrorCategory(error_category ec) {
        this.ec = ec;
    }

    public error_category getSwig() {
        return ec;
    }

    public String name() {
        return ec.name();
    }

    public String message(int ev) {
        return ec.message(ev);
    }

    public ErrorCondition defaultErrorCondition(int ev) {
        return new ErrorCondition(ec.default_error_condition(ev));
    }

    public boolean equivalent(int code, ErrorCondition condition) {
        return ec.equivalent(code, condition.getSwig());
    }

    public boolean equivalent(ErrorCode code, int condition) {
        return ec.equivalent(code.getSwig(), condition);
    }
}
