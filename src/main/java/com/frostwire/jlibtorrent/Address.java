package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;

/**
 * IP address abstraction supporting both IPv4 and IPv6 formats.
 * <p>
 * {@code Address} represents a network IP address that can be either IPv4 (32-bit)
 * or IPv6 (128-bit). This class provides utilities for parsing, comparing, and querying
 * IP addresses used throughout BitTorrent networking including peer discovery, DHT nodes,
 * tracker communication, and local network interface enumeration.
 * <p>
 * <b>Understanding IP Addresses in BitTorrent:</b>
 * <br/>
 * Addresses are fundamental to BitTorrent networking:
 * <ul>
 *   <li><b>IPv4:</b> Traditional 32-bit addresses (e.g., 192.168.1.1)</li>
 *   <li><b>IPv6:</b> Modern 128-bit addresses (e.g., 2001:db8::1)</li>
 *   <li><b>Used For:</b> Peers, DHT nodes, trackers, local network interfaces</li>
 *   <li><b>Special Types:</b> Loopback, multicast, unspecified, private ranges</li>
 * </ul>
 * <p>
 * <b>Creating Addresses:</b>
 * <pre>
 * // From IPv4 string (dotted decimal)
 * Address ipv4 = new Address("192.168.1.100");
 * System.out.println("IPv4: " + ipv4);  // Output: 192.168.1.100
 *
 * // From IPv6 string (hexadecimal notation)
 * Address ipv6 = new Address("2001:db8::8a2e:370:7334");
 * System.out.println("IPv6: " + ipv6);  // Output: 2001:db8::8a2e:370:7334
 *
 * // From localhost (works for both IPv4 and IPv6)
 * Address localhost4 = new Address("127.0.0.1");
 * Address localhost6 = new Address("::1");
 *
 * // Empty address (unspecified)
 * Address empty = new Address();
 *
 * // Invalid addresses throw IllegalArgumentException
 * try {
 *     Address bad = new Address("not.an.ip.address");
 * } catch (IllegalArgumentException e) {
 *     System.err.println("Parse error: " + e.getMessage());
 * }
 * </pre>
 * <p>
 * <b>Querying Address Properties:</b>
 * <pre>
 * Address addr = new Address("192.168.1.1");
 *
 * // Check IP version
 * if (addr.isV4()) {
 *     System.out.println("IPv4 address");
 * } else if (addr.isV6()) {
 *     System.out.println("IPv6 address");
 * }
 *
 * // Check special address types
 * if (addr.isLoopback()) {
 *     System.out.println("Loopback (local communication only)\");
 * }
 *
 * if (addr.isUnspecified()) {
 *     System.out.println("Unspecified (0.0.0.0 or ::)\");
 * }
 *
 * if (addr.isMulticast()) {
 *     System.out.println("Multicast address\");
 * }
 *
 * // String representation
 * String ipString = addr.toString();  // "192.168.1.1"
 * </pre>
 * <p>
 * <b>Address Classification:</b>
 * <pre>
 * // Private IPv4 ranges (RFC 1918)
 * // 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16
 * Address private1 = new Address("10.0.0.1");
 * Address private2 = new Address("172.31.255.255");
 * Address private3 = new Address("192.168.1.1");
 *
 * // Link-local address
 * Address linkLocal = new Address("169.254.1.1");
 *
 * // Loopback addresses
 * Address loopbackV4 = new Address("127.0.0.1");
 * Address loopbackV6 = new Address("::1");
 *
 * // Multicast addresses
 * Address multicastV4 = new Address("224.0.0.1");  // All hosts multicast
 * Address multicastV6 = new Address("ff02::1");    // All nodes multicast
 * </pre>
 * <p>
 * <b>Ordering Addresses:</b>
 * <pre>
 * Address addr1 = new Address("10.0.0.1");
 * Address addr2 = new Address("192.168.1.1");
 *
 * // Addresses implement Comparable&lt;Address&gt;
 * int comparison = addr1.compareTo(addr2);
 * if (comparison &lt; 0) {
 *     System.out.println("addr1 is before addr2 in sort order\");
 * }
 *
 * // Useful for sorting lists of addresses
 * java.util.List&lt;Address&gt; addresses = new java.util.ArrayList&lt;&gt;();
 * addresses.add(new Address("192.168.1.1\"));
 * addresses.add(new Address("10.0.0.1\"));
 * addresses.add(new Address("172.16.0.1\"));
 * java.util.Collections.sort(addresses);
 * </pre>
 * <p>
 * <b>Using Addresses with Endpoints:</b>
 * <pre>
 * // Create endpoint from address
 * Address peerAddr = new Address("203.0.113.50\");
 * TcpEndpoint peer = new TcpEndpoint(peerAddr, 6881);
 * System.out.println("Connecting to: \" + peer);
 *
 * UdpEndpoint dhtNode = new UdpEndpoint(peerAddr, 6881);
 * System.out.println("DHT node: \" + dhtNode);
 * </pre>
 * <p>
 * <b>Network Interface Enumeration:</b>
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * // Enumerate local network interfaces
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 * for (EnumNet.IpInterface iface : interfaces) {
 *     Address ifaceAddr = iface.interfaceAddress();
 *     Address netmask = iface.netmask();
 *
 *     System.out.println(\"Interface \" + iface.name());
 *     System.out.println(\"  Address: \" + ifaceAddr);
 *     System.out.println(\"  IPv4: \" + ifaceAddr.isV4());
 *     System.out.println(\"  Netmask: \" + netmask);
 * }
 *
 * // Enumerate routing table
 * java.util.List&lt;EnumNet.IpRoute&gt; routes = EnumNet.enumRoutes(sm);
 * for (EnumNet.IpRoute route : routes) {
 *     Address destination = route.destination();
 *     Address gateway = route.gateway();
 *
 *     System.out.println(\"Route to \" + destination);
 *     System.out.println(\"  Via gateway: \" + gateway);
 * }
 * </pre>
 * <p>
 * <b>Clone and Copy Semantics:</b>
 * <pre>
 * Address original = new Address("192.168.1.1");
 *
 * // Create a copy
 * Address copy = original.clone();
 *
 * // Both represent the same IP address
 * System.out.println(original.toString());  // 192.168.1.1
 * System.out.println(copy.toString());      // 192.168.1.1
 * </pre>
 * <p>
 * <b>Performance Considerations:</b>
 * <ul>
 *   <li>String parsing only happens in constructors; queries are O(1)</li>
 *   <li>Comparison is efficient (native bit comparison)</li>
 *   <li>IPv4 addresses require less memory than IPv6</li>
 *   <li>Addresses are immutable; thread-safe</li>
 * </ul>
 *
 * @see TcpEndpoint - TCP connections using addresses
 * @see UdpEndpoint - UDP connections using addresses
 * @see EnumNet.IpInterface - Network interface with address
 * @see EnumNet.IpRoute - Routing entry with gateway address
 *
 * @author gubatron
 * @author aldenml
 */
public final class Address implements Comparable<Address>, Cloneable {

    private final address addr;

    /**
     * @param addr the native object
     */
    public Address(address addr) {
        this.addr = addr;
    }

    /**
     * Create an address from an IPv4 address string in dotted decimal form,
     * or from an IPv6 address in hexadecimal notation.
     *
     * @param ip the ip string representation
     */
    public Address(String ip) {
        error_code ec = new error_code();
        this.addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
    }

    /**
     *
     */
    public Address() {
        this(new address());
    }

    /**
     * @return native object
     */
    public address swig() {
        return addr;
    }

    /**
     * Get whether the address is an IP version 4 address.
     *
     * @return if it's an IPv4 address
     */
    public boolean isV4() {
        return addr.is_v4();
    }

    /**
     * Get whether the address is an IP version 6 address.
     *
     * @return if it's an IPv6 address
     */
    public boolean isV6() {
        return addr.is_v6();
    }

    /**
     * Determine whether the address is a loopback address.
     *
     * @return if it's a loopback address
     */
    public boolean isLoopback() {
        return addr.is_loopback();
    }

    /**
     * Determine whether the address is unspecified.
     *
     * @return if it's an unspecified address
     */
    public boolean isUnspecified() {
        return addr.is_unspecified();
    }

    /**
     * Determine whether the address is a multicast address.
     *
     * @return if it's an multicast address
     */
    public boolean isMulticast() {
        return addr.is_multicast();
    }

    /**
     * Compare addresses for ordering.
     *
     * @param o the other address
     * @return -1, 0 or 1
     */
    @Override
    public int compareTo(Address o) {
        return address.compare(this.addr, o.addr);
    }

    /**
     * Get the address as a string in dotted decimal format.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return this.addr.to_string();
    }

    @Override
    public Address clone() {
        return new Address(new address(addr));
    }
}
