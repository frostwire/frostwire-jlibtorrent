package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
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
     * The error was unexpected and it is unknown which operation caused it.
     */
    UNKNOWN(operation_t.unknown.swigValue()),

    /**
     * This is used when the bittorrent logic determines to disconnect.
     */
    BITTORRENT(operation_t.bittorrent.swigValue()),

    /**
     * A call to iocontrol failed.
     */
    IOCONTROL(operation_t.iocontrol.swigValue()),

    /**
     * A call to getpeername failed (querying the remote IP of a connection).
     */
    GETPEERNAME(operation_t.getpeername.swigValue()),

    /**
     * A call to getname failed (querying the local IP of a connection).
     */
    GETNAME(operation_t.getname.swigValue()),

    /**
     * An attempt to allocate a receive buffer failed.
     */
    ALLOC_RECVBUF(operation_t.alloc_recvbuf.swigValue()),

    /**
     * An attempt to allocate a send buffer failed.
     */
    ALLOC_SNDBUF(operation_t.alloc_sndbuf.swigValue()),

    /**
     * Writing to a file failed.
     */
    FILE_WRITE(operation_t.file_write.swigValue()),

    /**
     * Reading from a file failed.
     */
    FILE_READ(operation_t.file_read.swigValue()),

    /**
     * A non-read and non-write file operation failed.
     */
    FILE(operation_t.file.swigValue()),

    /**
     * A socket write operation failed.
     */
    SOCK_WRITE(operation_t.sock_write.swigValue()),

    /**
     * A socket read operation failed.
     */
    SOCK_READ(operation_t.sock_read.swigValue()),

    /**
     * A call to open(), to create a socket socket failed.
     */
    SOCK_OPEN(operation_t.sock_open.swigValue()),

    /**
     * A call to bind() on a socket failed.
     */
    SOCK_BIND(operation_t.sock_bind.swigValue()),

    /**
     * An attempt to query the number of bytes available to read from a socket
     * failed.
     */
    AVAILABLE(operation_t.available.swigValue()),

    /**
     * A call related to bittorrent protocol encryption failed.
     */
    ENCRYPTION(operation_t.encryption.swigValue()),

    /**
     * An attempt to connect a socket failed.
     */
    CONNECT(operation_t.connect.swigValue()),

    /**
     * Establishing an SSL connection failed.
     */
    SSL_HANDSHAKE(operation_t.ssl_handshake.swigValue()),

    /**
     * A connection failed to satisfy the bind interface setting.
     */
    GET_INTERFACE(operation_t.get_interface.swigValue()),

    /**
     * A call to listen() on a socket.
     */
    SOCK_LISTEN(operation_t.sock_listen.swigValue()),

    /**
     * A call to the ioctl to bind a socket to a specific network device or
     * adaptor.
     */
    SOCK_BIND_TO_DEVICE(operation_t.sock_bind_to_device.swigValue()),

    /**
     * A call to accept() on a socket.
     */
    SOCK_ACCEPT(operation_t.sock_accept.swigValue()),

    /**
     * Convert a string into a valid network address.
     */
    PARSE_ADDRESS(operation_t.parse_address.swigValue()),

    /**
     * Enumeration network devices or adapters.
     */
    ENUM_IF(operation_t.enum_if.swigValue()),

    /**
     *
     */
    FILE_STAT(operation_t.file_stat.swigValue()),

    /**
     *
     */
    FILE_COPY(operation_t.file_copy.swigValue()),

    /**
     *
     */
    FILE_FALLOCATE(operation_t.file_fallocate.swigValue()),

    /**
     *
     */
    FILE_HARD_LINK(operation_t.file_hard_link.swigValue()),

    /**
     *
     */
    FILE_REMOVE(operation_t.file_remove.swigValue()),

    /**
     *
     */
    FILE_RENAME(operation_t.file_rename.swigValue()),

    /**
     *
     */
    FILE_OPEN(operation_t.file_open.swigValue()),

    /**
     *
     */
    MKDIR(operation_t.mkdir.swigValue()),

    /**
     *
     */
    CHECK_RESUME(operation_t.check_resume.swigValue()),

    /**
     *
     */
    EXCEPTION(operation_t.exception.swigValue()),

    /**
     *
     */
    ALLOC_CACHE_PIECE(operation_t.alloc_cache_piece.swigValue()),

    /**
     *
     */
    PARTFILE_MOVE(operation_t.partfile_move.swigValue()),

    /**
     *
     */
    PARTFILE_READ(operation_t.partfile_read.swigValue()),

    /**
     *
     */
    PARTFILE_WRITE(operation_t.partfile_write.swigValue()),

    /**
     *
     */
    HOSTNAME_LOOKUP(operation_t.hostname_lookup.swigValue());

    Operation(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return the native value.
     */
    public int swig() {
        return swigValue;
    }

    public String nativeName() {
        try {
            return libtorrent.operation_name(operation_t.swigToEnum(swigValue));
        } catch (Throwable e) {
            return "invalid enum value";
        }
    }

    /**
     * @param swigValue the native value
     * @return the swig enum.
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

    /**
     * @param swigValue the native enum
     * @return the swig enum.
     */
    public static Operation fromSwig(operation_t swigValue) {
        return fromSwig(swigValue.swigValue());
    }
}
