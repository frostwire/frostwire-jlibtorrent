package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.error_code;

/**
 * Error status information from libtorrent operations.
 * <p>
 * {@code ErrorCode} wraps error information from libtorrent C++ library,
 * containing an integer error code value and human-readable error message.
 * Used throughout jlibtorrent to report operation failures in a portable way.
 * <p>
 * <b>Understanding Error Codes:</b>
 * <pre>
 * ErrorCode ec = ...;  // From alert or API
 *
 * // Check if this is actually an error
 * if (ec.isError()) {
 *     // An error occurred
 *     int code = ec.value();
 *     String msg = ec.message();
 *
 *     System.err.println(\"Error \" + code + \": \" + msg);
 * }
 * </pre>
 * <p>
 * <b>Common Error Scenarios:</b>
 * <pre>
 * // Network errors (Connection refused, timeout, etc)
 * // I/O errors (Disk full, permission denied, etc)
 * // Protocol errors (Invalid metadata, corrupted files, etc)
 * // System errors (Memory allocation, resource limits, etc)
 * </pre>
 * <p>
 * <b>Error Information Properties:</b>
 * <ul>
 *   <li><b>value():</b> Integer error code (platform/system dependent)</li>
 *   <li><b>message():</b> Human-readable error description</li>
 *   <li><b>isError():</b> True only if an actual error (0 = no error)</li>
 * </ul>
 *
 * @see Operation the operation that failed (if provided in alert)
 * @see com.frostwire.jlibtorrent.swig.error_code the underlying native error code
 *
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
