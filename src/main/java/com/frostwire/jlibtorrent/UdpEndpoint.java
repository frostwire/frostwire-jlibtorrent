package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

/**
 * A network address and UDP port pair for DHT and uTP protocol communication.
 * <p>
 * {@code UdpEndpoint} represents a remote node's address and UDP port in the DHT network
 * or for uTP (Micro Transport Protocol) connections. Unlike {@code TcpEndpoint} which is used
 * for traditional BitTorrent peer connections on TCP, UdpEndpoint identifies nodes participating
 * in the Distributed Hash Table (DHT) or peers using the uTP protocol over UDP.
 * <p>
 * <b>Understanding UDP Endpoints:</b>
 * <br/>
 * UDP endpoints represent peer/node locations for UDP-based protocols:
 * <ul>
 *   <li><b>DHT Communication:</b> DHT operations use UDP to query remote nodes</li>
 *   <li><b>DHT Node Discovery:</b> DHT bootstrap and lookups return lists of UdpEndpoints</li>
 *   <li><b>uTP Connections:</b> Alternative to TCP using UDP with Micro Transport Protocol</li>
 *   <li><b>Address Format:</b> Supports both IPv4 (192.168.1.1:6881) and IPv6 addresses</li>
 * </ul>
 * <p>
 * <b>Creating UDP Endpoints:</b>
 * <pre>
 * // From IPv4 address and port
 * UdpEndpoint dhtNode1 = new UdpEndpoint("203.0.113.50", 6881);
 * System.out.println(dhtNode1);  // Output: udp://203.0.113.50:6881
 *
 * // From IPv6 address
 * UdpEndpoint dhtNode2 = new UdpEndpoint("2001:db8::8a2e:370:7334", 6881);
 * System.out.println(dhtNode2);  // Output: udp://2001:db8::8a2e:370:7334:6881
 *
 * // From Address object
 * Address dhtNodeAddr = new Address("151.101.1.140");
 * UdpEndpoint bootstrapNode = new UdpEndpoint(dhtNodeAddr, 6881);
 *
 * // Create empty endpoint (useful for initialization)
 * UdpEndpoint empty = new UdpEndpoint();
 * </pre>
 * <p>
 * <b>Querying Endpoint Information:</b>
 * <pre>
 * UdpEndpoint node = new UdpEndpoint("162.125.18.133", 6881);
 *
 * // Get the address component
 * Address addr = node.address();
 * boolean isV4 = addr.isV4();  // true for IPv4
 * boolean isV6 = addr.isV6();  // true for IPv6
 * String ipString = addr.toString();
 *
 * // Get the port
 * int port = node.port();  // 6881
 *
 * // Get full string representation with udp:// prefix
 * String fullAddr = node.toString();  // "udp://162.125.18.133:6881"
 * </pre>
 * <p>
 * <b>DHT Bootstrap Nodes:</b>
 * <p>
 * Bootstrap nodes are well-known DHT nodes used to join the DHT network:
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * // Common DHT bootstrap nodes (well-known, public DHT nodes)
 * UdpEndpoint[] bootstrapNodes = {
 *     new UdpEndpoint("router.bittorrent.com", 6881),
 *     new UdpEndpoint("dht.transmissionbt.com", 6881),
 *     new UdpEndpoint("router.utorrent.com", 6881),
 *     new UdpEndpoint("208.67.72.220", 6881)  // OpenDNS
 * };
 *
 * // Bootstrap DHT by adding these nodes to routing table
 * for (UdpEndpoint node : bootstrapNodes) {
 *     try {
 *         sm.dhtBootstrap(node);
 *     } catch (Exception e) {
 *         System.out.println("Could not connect to " + node);
 *     }
 * }
 * </pre>
 * <p>
 * <b>DHT Lookup Operations:</b>
 * <pre>
 * // Perform DHT lookup (uses UDP endpoints internally)
 * Sha1Hash infoHash = new Sha1Hash("d8e8fca2dc0f896fd7cb4cb0031ba249");
 * java.util.ArrayList&lt;TcpEndpoint&gt; peers = sm.dhtGetPeers(infoHash, 20);
 *
 * // The DHT communicates with UDP endpoints to find these TCP peers
 * </pre>
 * <p>
 * <b>DHT Routing Table Statistics:</b>
 * <pre>
 * // Monitor DHT bucket population (uses UdpEndpoints internally)
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.DHT_STATS.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         DhtStatsAlert a = (DhtStatsAlert) alert;
 *         List&lt;DhtRoutingBucket&gt; buckets = a.routingBuckets();
 *
 *         int totalNodes = 0;
 *         for (DhtRoutingBucket bucket : buckets) {
 *             totalNodes += bucket.numNodes();
 *             // Each node tracked is conceptually a UdpEndpoint
 *         }
 *
 *         System.out.println("DHT routing table has \" + totalNodes + \" nodes\");
 *     }
 * });
 * </pre>
 * <p>
 * <b>Comparison: TCP Endpoint vs UDP Endpoint:</b>
 * <pre>
 * ┌─────────────────────────────┬─────────────────┬──────────────────┐
 * │ Characteristic              │ TcpEndpoint     │ UdpEndpoint      │
 * ├─────────────────────────────┼─────────────────┼──────────────────┤
 * │ Protocol                    │ TCP             │ UDP              │
 * │ Purpose                     │ Peer data xfer  │ DHT/uTP          │
 * │ Connection State            │ Connected       │ Connectionless   │
 * │ String Format               │ 192.168.1.1:80  │ udp://192....:80 │
 * │ IPv6 Format                 │ [::1]:80        │ [::1]:80         │
 * │ Typical BitTorrent Usage    │ Blocks exchange │ Node discovery   │
 * │ Number per Peer             │ 1 per torrent   │ Many (DHT)       │
 * └─────────────────────────────┴─────────────────┴──────────────────┘
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>UDP endpoints are lightweight and frequently created/destroyed</li>
 *   <li>DHT operations may create thousands of UdpEndpoint references internally</li>
 *   <li>String constructor performs IP address validation; invalid IPs throw immediately</li>
 *   <li>Cloning is supported for creating copies of endpoint references</li>
 * </ul>
 *
 * @see TcpEndpoint - For TCP peer connections
 * @see Address - For IP address manipulation
 * @see DhtLookup - For DHT lookup operations that use UDP endpoints
 * @see DhtRoutingBucket - For DHT routing table containing UDP node endpoints
 *
 * @author gubatron
 * @author aldenml
 */
public final class UdpEndpoint implements Cloneable {

    private final udp_endpoint endp;

    public UdpEndpoint(udp_endpoint endp) {
        this.endp = endp;
    }

    public UdpEndpoint() {
        this(new udp_endpoint());
    }

    public UdpEndpoint(Address address, int port) {
        this(new udp_endpoint(address.swig(), port));
    }

    public UdpEndpoint(String ip, int port) {
        error_code ec = new error_code();
        address addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
        this.endp = new udp_endpoint(addr, port);
    }

    public udp_endpoint swig() {
        return endp;
    }

    public Address address() {
        return new Address(endp.address());
    }

    /**
     * Get the port associated with the endpoint. The port number is always in
     * the host's byte order.
     *
     * @return the port
     */
    public int port() {
        return endp.port();
    }

    @Override
    public String toString() {
        return "udp://" + endp.address().to_string() + ":" + endp.port();
    }

    @Override
    public UdpEndpoint clone() {
        return new UdpEndpoint(new udp_endpoint(endp));
    }
}
