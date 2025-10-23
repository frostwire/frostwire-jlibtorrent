package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;

/**
 * A network address and TCP port pair for peer connections in BitTorrent.
 * <p>
 * {@code TcpEndpoint} represents a remote peer's address and port for TCP connections.
 * In BitTorrent, peers discovered via DHT, trackers, or PEX are identified by their
 * IPv4 or IPv6 address combined with a TCP port where they're listening for connections.
 * <p>
 * <b>Understanding TCP Endpoints:</b>
 * <br/>
 * TCP endpoints are used to represent peer locations throughout the BitTorrent protocol:
 * <ul>
 *   <li><b>Peer Discovery:</b> Trackers and DHT return lists of TcpEndpoints for connecting</li>
 *   <li><b>Connection:</b> SessionManager connects to TcpEndpoints to establish peer relationships</li>
 *   <li><b>Peer Info:</b> PeerInfo objects contain TcpEndpoints showing connected peers</li>
 *   <li><b>Address Format:</b> Supports both IPv4 (192.168.1.1:6881) and IPv6 ([::1]:6881)</li>
 * </ul>
 * <p>
 * <b>Creating TCP Endpoints:</b>
 * <pre>
 * // From IPv4 address and port
 * TcpEndpoint peer1 = new TcpEndpoint("192.168.1.100", 6881);
 * System.out.println(peer1);  // Output: 192.168.1.100:6881
 *
 * // From IPv6 address (automatically formatted with brackets)
 * TcpEndpoint peer2 = new TcpEndpoint("2001:db8::1", 6881);
 * System.out.println(peer2);  // Output: [2001:db8::1]:6881
 *
 * // From localhost
 * TcpEndpoint local = new TcpEndpoint("127.0.0.1", 6882);
 * System.out.println(local);  // Output: 127.0.0.1:6882
 *
 * // From Address object (more efficient for complex operations)
 * Address addr = new Address("10.0.0.1");
 * TcpEndpoint peer3 = new TcpEndpoint(addr, 6883);
 * </pre>
 * <p>
 * <b>Querying Endpoints:</b>
 * <pre>
 * TcpEndpoint peer = new TcpEndpoint("tracker.example.com", 6881);
 *
 * // Get the address component
 * Address addr = peer.address();
 * boolean isV4 = addr.isV4();  // true for IPv4
 * boolean isV6 = addr.isV6();  // true for IPv6
 * String ipString = addr.toString();  // "192.168.1.100"
 *
 * // Get the port
 * int port = peer.port();  // 6881
 * System.out.println("Port: " + port);
 *
 * // Get full string representation (with proper IPv6 bracket formatting)
 * String fullAddr = peer.toString();  // "192.168.1.100:6881"
 * </pre>
 * <p>
 * <b>Usage in Peer Discovery:</b>
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * // DHT peer discovery returns list of TcpEndpoints
 * Sha1Hash infoHash = new Sha1Hash("d8e8fca2dc0f896fd7cb4cb0031ba249");
 * ArrayList&lt;TcpEndpoint&gt; peers = sm.dhtGetPeers(infoHash, 20);
 *
 * for (TcpEndpoint peer : peers) {
 *     System.out.println("Found peer: " + peer.toString());
 *     // Connect to: peer.address() on peer.port()
 * }
 * </pre>
 * <p>
 * <b>Connected Peers from Torrent:</b>
 * <pre>
 * TorrentHandle th = sm.find(infoHash);
 * TorrentStatus status = th.status();
 *
 * // Get list of connected peers
 * java.util.List&lt;PeerInfo&gt; peers = th.peers();
 * for (PeerInfo peerInfo : peers) {
 *     TcpEndpoint endpoint = peerInfo.endpoint();
 *     System.out.println("Connected to: " + endpoint);
 *     System.out.println("  Download speed: " + peerInfo.downSpeed() + " B/s");
 *     System.out.println("  Upload speed: " + peerInfo.upSpeed() + " B/s");
 * }
 * </pre>
 * <p>
 * <b>Error Handling:</b>
 * <p>
 * Creating a TcpEndpoint from an invalid IP address throws IllegalArgumentException:
 * <pre>
 * try {
 *     TcpEndpoint invalid = new TcpEndpoint("not.a.valid.ip", 6881);
 * } catch (IllegalArgumentException e) {
 *     System.err.println("Invalid IP address: " + e.getMessage());
 * }
 * </pre>
 * <p>
 * <b>TCP Endpoint vs UDP Endpoint:</b>
 * <p>
 * {@code TcpEndpoint} is for BitTorrent peer connections (TCP port).
 * {@code UdpEndpoint} is for DHT and uTP connections (UDP port).
 * They may use the same or different ports on the same host.
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Creating endpoints from String requires IP parsing; reuse Address objects when possible</li>
 *   <li>Endpoints are lightweight; safe to create and discard frequently</li>
 *   <li>String constructor performs validation; invalid IPs throw immediately</li>
 * </ul>
 *
 * @see Address - For IP address manipulation
 * @see UdpEndpoint - For UDP-based network operations
 * @see PeerInfo#endpoint() - Get endpoint of a connected peer
 * @see SessionManager#dhtGetPeers(Sha1Hash, int) - Get peer endpoints from DHT
 *
 * @author gubatron
 * @author aldenml
 */
public final class TcpEndpoint implements Cloneable {

    private final tcp_endpoint endp;

    /**
     * @param endp the native object
     */
    public TcpEndpoint(tcp_endpoint endp) {
        this.endp = endp;
    }

    /**
     *
     */
    public TcpEndpoint() {
        this(new tcp_endpoint());
    }

    /**
     * @param address the address
     * @param port    the port
     */
    public TcpEndpoint(Address address, int port) {
        this(new tcp_endpoint(address.swig(), port));
    }

    /**
     * @param ip   the address as an IP
     * @param port the port
     */
    public TcpEndpoint(String ip, int port) {
        error_code ec = new error_code();
        address addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
        this.endp = new tcp_endpoint(addr, port);
    }

    /**
     * @return the native object
     */
    public tcp_endpoint swig() {
        return endp;
    }

    /**
     * @return the address
     */
    public Address address() {
        return new Address(endp.address());
    }

    /**
     * @return the port
     */
    public int port() {
        return endp.port();
    }

    /**
     * @return the string representation
     */
    @Override
    public String toString() {
        address addr = endp.address();
        String s = new Address(addr).toString();
        return (addr.is_v4() ? s : "[" + s + "]") + ":" + endp.port();
    }

    @Override
    public TcpEndpoint clone() {
        return new TcpEndpoint(new tcp_endpoint(endp));
    }
}
