package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_info;

/**
 * Holds information and statistics about one peer
 * that libtorrent is connected to.
 * <p>
 * This class is a lightweight version of the native {@link peer_info}, and
 * only carries a subset of all the information. However, it's completely open
 * for custom use or optimization to accommodate client necessities.
 *
 * @author gubatron
 * @author aldenml
 */
public class PeerInfo {

    protected String client;
    protected long totalDownload;
    protected long totalUpload;
    protected int flags;
    protected byte source;
    protected int upSpeed;
    protected int downSpeed;
    protected ConnectionType connectionType;
    protected float progress;
    protected int progressPpm;
    protected String ip;

    public PeerInfo(peer_info p) {
        init(p);
    }

    /**
     * This describe the software at the other end of the connection.
     * In some cases this information is not available, then it will contain
     * a string that may give away something about which software is running
     * in the other end. In the case of a web seed, the server type and
     * version will be a part of this string.
     *
     * @return the client string
     */
    public String client() {
        return client;
    }

    /**
     * The total number of bytes downloaded from this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes downloaded
     */
    public long totalDownload() {
        return totalDownload;
    }

    /**
     * The total number of bytes uploaded to this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes uploaded
     */
    public long totalUpload() {
        return totalUpload;
    }

    /**
     * Tells you in which state the peer is in. It is set to
     * any combination of the peer_flags_t flags.
     *
     * @return the flags as an integer
     */
    public int flags() {
        return flags;
    }

    /**
     * A combination of flags describing from which sources this peer
     * was received. A combination of the peer_source_flags_t flags.
     *
     * @return the flags as a byte
     */
    public byte source() {
        return source;
    }

    /**
     * The current upload speed we have to and from this peer
     * (including any protocol messages). Updated about once per second
     *
     * @return current upload speed we have to and from this peer
     */
    public int upSpeed() {
        return upSpeed;
    }

    /**
     * The current download speed we have to and from this peer
     * (including any protocol messages). Updated about once per second
     *
     * @return current download speed we have to and from this peer
     */
    public int downSpeed() {
        return downSpeed;
    }

    /**
     * The kind of connection this peer uses.
     *
     * @return the connection type
     */
    public ConnectionType connectionType() {
        return connectionType;
    }

    /**
     * The progress of the peer in the range [0, 1]. This is always 0 when
     * floating point operations are disabled, instead use ``progress_ppm``.
     *
     * @return the progress of the peer in the range [0, 1]
     */
    public float progress() {
        return progress;
    }

    /**
     * Indicates the download progress of the peer in the range [0, 1000000]
     * (parts per million).
     *
     * @return the download progress of the peer in the range [0, 1000000]
     */
    public int progressPpm() {
        return progressPpm;
    }

    /**
     * The IP-address to this peer.
     *
     * @return a string representing the endpoint.
     */
    public String ip() {
        return ip;
    }

    protected void init(peer_info p) {
        client = Vectors.byte_vector2ascii(p.get_client());
        totalDownload = p.getTotal_download();
        totalUpload = p.getTotal_upload();
        flags = p.get_flags();
        source = p.get_source();
        upSpeed = p.getUp_speed();
        downSpeed = p.getDown_speed();
        connectionType = ConnectionType.fromSwig(p.getConnection_type());
        progress = p.getProgress();
        progressPpm = p.getProgress_ppm();
        ip = new TcpEndpoint(p.getIp()).toString();
    }

    /**
     * The kind of connection this is. Used for the connectionType field.
     */
    public enum ConnectionType {

        /**
         * Regular bittorrent connection.
         */
        STANDARD_BITTORRENT(peer_info.connection_type_t.standard_bittorrent.swigValue()),

        /**
         * HTTP connection using the `BEP 19`_ protocol
         */
        WEB_SEED(peer_info.connection_type_t.web_seed.swigValue()),

        /**
         * HTTP connection using the `BEP 17`_ protocol.
         */
        HTTP_SEED(peer_info.connection_type_t.http_seed.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        ConnectionType(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the native value
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue the swig value
         * @return the enum value
         */
        public static ConnectionType fromSwig(int swigValue) {
            ConnectionType[] enumValues = ConnectionType.class.getEnumConstants();
            for (ConnectionType ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
