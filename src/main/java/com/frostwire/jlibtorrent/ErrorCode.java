package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCode {

    private final error_code ec;

    /**
     * @param ec the native object
     */
    public ErrorCode(error_code ec) {
        this.ec = ec;
    }

    /**
     *
     */
    public ErrorCode() {
        this(new error_code());
    }

    /**
     * @return the native object
     */
    public error_code swig() {
        return ec;
    }

    /**
     *
     */
    public void clear() {
        ec.clear();
    }

    /**
     * @return the internal error code value
     */
    public int value() {
        return ec.value();
    }

    /**
     * @return the error message
     */
    public String message() {
        return ec.message();
    }
}
