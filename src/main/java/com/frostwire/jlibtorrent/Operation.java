package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.operation_t;

/**
 * These constants are used to identify the operation that failed, causing a
 * peer to disconnect.
 *
 * @author gubatron
 * @author aldenml
 */
public enum Operation {

    /**
     * This is used when the bittorrent logic determines to disconnect.
     */
    OP_BITTORRENT(operation_t.op_bittorrent.swigValue()),

    /**
     * A call to {@code iocontrol} failed.
     */
    OP_IOCONTROL(operation_t.op_iocontrol.swigValue()),

    /**
     * A call to {@code getpeername} failed (querying the remote IP of a connection).
     */
    OP_GETPEERNAME(operation_t.op_getpeername.swigValue()),

    /**
     * A call to {@code getname} failed (querying the local IP of a connection).
     */
    OP_GETNAME(operation_t.op_getname.swigValue()),

    /**
     * An attempt to allocate a receive buffer failed.
     */
    OP_ALLOC_RECVBUF(operation_t.op_alloc_recvbuf.swigValue()),

    /**
     * An attempt to allocate a send buffer failed.
     */
    OP_ALLOC_SNDBUF(operation_t.op_alloc_sndbuf.swigValue()),

    /**
     * Writing to a file failed.
     */
    OP_FILE_WRITE(operation_t.op_file_write.swigValue()),

    /**
     * Reading from a file failed.
     */
    OP_FILE_READ(operation_t.op_file_read.swigValue()),

    /**
     * A non-read and non-write file operation failed.
     */
    OP_FILE(operation_t.op_file.swigValue()),

    /**
     * A socket write operation failed.
     */
    OP_SOCK_WRITE(operation_t.op_sock_write.swigValue()),

    /**
     * A socket read operation failed.
     */
    OP_SOCK_READ(operation_t.op_sock_read.swigValue()),

    /**
     * A call to {@code open()}, to create a socket socket failed.
     */
    OP_SOCK_OPEN(operation_t.op_sock_open.swigValue()),

    /**
     * A call to {@code bind()} on a socket failed.
     */
    OP_SOCK_BIND(operation_t.op_sock_bind.swigValue()),

    /**
     * An attempt to query the number of bytes available to read from a socket failed.
     */
    OP_AVAILABLE(operation_t.op_available.swigValue()),

    /**
     * A call related to bittorrent protocol encryption failed.
     */
    OP_ENCRYPTION(operation_t.op_encryption.swigValue()),

    /**
     * An attempt to connect a socket failed.
     */
    OP_CONNECT(operation_t.op_connect.swigValue()),

    /**
     * Establishing an SSL connection failed.
     */
    OP_SSL_HANDSHAKE(operation_t.op_ssl_handshake.swigValue()),

    /**
     * A connection failed to satisfy the bind interface setting.
     */
    OP_GET_INTERFACE(operation_t.op_get_interface.swigValue()),

    /**
     *
     */
    UNKNOWN(-1);

    Operation(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return
     */
    public int swig() {
        return swigValue;
    }

    /**
     * @param swigValue
     * @return
     */
    public static Operation fromSwig(int swigValue) {
        Operation[] enumValues = Operation.class.getEnumConstants();
        for (Operation ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        return UNKNOWN;
    }
}
