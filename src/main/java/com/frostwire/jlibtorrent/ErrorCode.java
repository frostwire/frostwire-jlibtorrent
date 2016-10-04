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

    public ErrorCode() {
        this(new error_code());
    }

    public error_code swig() {
        return ec;
    }

    public void clear() {
        ec.clear();
    }

    public int value() {
        return ec.value();
    }

    public String message() {
        return ec.message();
    }
}
