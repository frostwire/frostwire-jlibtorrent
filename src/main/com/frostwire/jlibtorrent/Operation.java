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
    OP_BITTORRENT(operation_t.op_bittorrent),

    /**
     * a call to iocontrol failed.
     */
    OP_IOCONTROL(operation_t.op_iocontrol),

    /**
     * a call to getpeername failed (querying the remote IP of a connection).
     */
    OP_GETPEERNAME(operation_t.op_getpeername),

    /**
     * a call to getname failed (querying the local IP of a connection).
     */
    OP_GETNAME(operation_t.op_getname),

    /**
     * an attempt to allocate a receive buffer failed.
     */
    OP_ALLOC_RECVBUF(operation_t.op_alloc_recvbuf),

    /**
     * an attempt to allocate a send buffer failed.
     */
    OP_ALLOC_SNDBUF(operation_t.op_alloc_sndbuf),

    /**
     * writing to a file failed.
     */
    OP_FILE_WRITE(operation_t.op_file_write),

    /**
     * reading from a file failed.
     */
    OP_FILE_READ(operation_t.op_file_read),

    /**
     * a non-read and non-write file operation failed.
     */
    OP_FILE(operation_t.op_file),

    /**
     * a socket write operation failed.
     */
    OP_SOCK_WRITE(operation_t.op_sock_write),

    /**
     * a socket read operation failed.
     */
    OP_SOCK_READ(operation_t.op_sock_read),

    /**
     * a call to open(), to create a socket socket failed.
     */
    OP_SOCK_OPEN(operation_t.op_sock_open),

    /**
     * a call to bind() on a socket failed.
     */
    OP_SOCK_BIND(operation_t.op_sock_bind),

    /**
     * an attempt to query the number of bytes available to read from a socket failed.
     */
    OP_AVAILABLE(operation_t.op_available),

    /**
     * a call related to bittorrent protocol encryption failed.
     */
    OP_ENCRYPTION(operation_t.op_encryption),

    /**
     * an attempt to  connect a socket failed.
     */
    OP_CONNECT(operation_t.op_connect),

    /**
     * establishing an SSL connection failed.
     */
    OP_SSL_HANDSHAKE(operation_t.op_ssl_handshake),

    /**
     * a connection failed to satisfy the bind interface setting.
     */
    OP_GET_INTERFACE(operation_t.op_get_interface);

    private Operation(operation_t swigObj) {
        this.swigObj = swigObj;
    }

    private final operation_t swigObj;

    public operation_t getSwig() {
        return swigObj;
    }

    public static Operation fromSwig(operation_t swigObj) {
        Operation[] enumValues = Operation.class.getEnumConstants();
        for (Operation ev : enumValues) {
            if (ev.getSwig() == swigObj) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + Operation.class + " with swig value " + swigObj);
    }
}
