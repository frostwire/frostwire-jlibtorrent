package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.operation_t;

/**
 * Identifies the type of operation that caused an error or peer disconnect.
 * <p>
 * {@code Operation} categorizes failures in libtorrent by the operation being performed
 * when the error occurred. Useful for diagnosing problems and implementing operation-specific
 * error handling. See {@code PeerError} for peer disconnects with error details.
 * <p>
 * <b>Understanding Operation Failures:</b>
 * <pre>
 * // When a peer disconnects with an error
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PEER_DISCONNECTED.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PeerDisconnectedAlert a = (PeerDisconnectedAlert) alert;
 *         PeerError error = a.error();
 *
 *         Operation op = error.operation();
 *         ErrorCode ec = error.errorCode();
 *
 *         System.out.println(\"Peer failed during: \" + op);
 *         System.out.println(\"Error: \" + ec.message());
 *
 *         // Handle based on operation type
 *         if (op == Operation.SOCK_READ) {
 *             System.out.println(\"Network read timeout or connection reset\");
 *         } else if (op == Operation.FILE_WRITE) {
 *             System.out.println(\"Disk write failed (space, permissions?)\");
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>Common Operations Causing Failures:</b>
 * <ul>
 *   <li><b>Network I/O:</b> SOCK_READ, SOCK_WRITE, CONNECT, SOCK_OPEN</li>
 *   <li><b>File I/O:</b> FILE_READ, FILE_WRITE, FILE_OPEN, FILE_STAT</li>
 *   <li><b>Protocol:</b> ENCRYPTION, BITTORRENT (protocol-level error)</li>
 *   <li><b>System:</b> ALLOC_RECVBUF, ALLOC_SNDBUF (out of memory)</li>
 * </ul>
 * <p>
 * <b>Network Operations:</b>
 * <pre>
 * SOCK_READ:       Read from socket failed (timeout, connection reset, no data)
 * SOCK_WRITE:      Write to socket failed (connection broken, pipe full)
 * SOCK_OPEN:       Creating socket failed (resource limits)
 * SOCK_BIND:       Binding to port failed (port in use, permission denied)
 * SOCK_LISTEN:     Making socket listen failed
 * SOCK_ACCEPT:     Accepting connection failed
 * SOCK_BIND_TO_DEVICE: Binding to specific network adapter failed
 * CONNECT:         Connection attempt failed (refused, timeout, unreachable)
 * AVAILABLE:       Querying available data failed
 * </pre>
 * <p>
 * <b>File Operations:</b>
 * <pre>
 * FILE_READ:       Reading file failed (I/O error, file vanished)
 * FILE_WRITE:      Writing file failed (disk full, permission denied)
 * FILE_OPEN:       Opening file failed (not found, permission denied)
 * FILE_STAT:       Querying file info failed (file deleted, permission)
 * FILE_COPY:       Copying file failed
 * FILE_REMOVE:     Deleting file failed
 * FILE_RENAME:     Renaming file failed
 * FILE_FALLOCATE:  Pre-allocating space failed (disk full, unsupported)
 * FILE_HARD_LINK:  Creating hard link failed
 * MKDIR:           Creating directory failed
 * </pre>
 * <p>
 * <b>Protocol and Crypto Operations:</b>
 * <pre>
 * ENCRYPTION:      BitTorrent protocol encryption failed (corrupt data)
 * BITTORRENT:      BitTorrent protocol logic error (invalid message)
 * SSL_HANDSHAKE:   TLS/SSL handshake failed (invalid certificate, version mismatch)
 * PARSE_ADDRESS:   Parsing IP address string failed (invalid format)
 * </pre>
 * <p>
 * <b>Memory and Resource Operations:</b>
 * <pre>
 * ALLOC_RECVBUF:   Allocating receive buffer failed (out of memory)
 * ALLOC_SNDBUF:    Allocating send buffer failed (out of memory)
 * ALLOC_CACHE_PIECE: Allocating piece cache failed (out of memory)
 * </pre>
 * <p>
 * <b>Diagnostic Uses:</b>
 * <pre>
 * // Track frequent failure types
 * java.util.Map&lt;Operation, Integer&gt; failures = new java.util.HashMap&lt;&gt;();
 *
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PEER_DISCONNECTED.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PeerDisconnectedAlert a = (PeerDisconnectedAlert) alert;
 *         Operation op = a.error().operation();
 *
 *         failures.put(op, failures.getOrDefault(op, 0) + 1);
 *     }
 * });
 *
 * // Later: analyze patterns
 * // Too many FILE_WRITE failures? → Disk problem
 * // Too many SOCK_READ? → Network instability
 * // Too many ALLOC_*? → Memory pressure
 * </pre>
 * <p>
 * <b>Using Operation.nativeName():</b>
 * <pre>
 * Operation op = Operation.FILE_WRITE;
 * String name = op.nativeName();  // "file_write" (from C++ library)
 *
 * // For logging/debugging
 * System.out.println(\"Failed operation: \" + name);
 * </pre>
 *
 * @see PeerError - Contains Operation and ErrorCode
 * @see ErrorCode - Error details for the operation
 * @see PeerDisconnectedAlert - Uses Operation to identify failure type
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
