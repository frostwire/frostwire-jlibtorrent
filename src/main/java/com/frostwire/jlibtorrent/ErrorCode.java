package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ErrorCode {

    private int value;
    private String message;
    private boolean isError;

    /**
     * @param ec the native object
     */
    public ErrorCode(error_code ec) {
        assign(ec);
    }

    /**
     * @return the internal error code value
     */
    public int value() {
        return value;
    }

    /**
     * @return the error message
     */
    public String message() {
        return message;
    }

    /**
     * Returns if this error code actually represents an error.
     *
     * @return true if an actual error
     */
    public boolean isError() {
        return isError;
    }

    void assign(error_code ec) {
        value = ec.value();
        message = ec.message();
        isError = ec.op_bool();
    }
}
