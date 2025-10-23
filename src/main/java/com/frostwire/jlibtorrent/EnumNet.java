/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 *
 * Licensed under the MIT License.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for enumerating network interfaces and routing tables on the local system.
 * <p>
 * {@code EnumNet} provides static methods to discover local network configuration including
 * active network interfaces, routing table entries, and gateway information. This is useful for
 * binding to specific network interfaces, diagnosing network connectivity, and implementing
 * network-aware features in BitTorrent applications.
 * <p>
 * <b>Understanding Network Enumeration:</b>
 * <br/>
 * Network enumeration discovers the local system's network setup:
 * <ul>
 *   <li><b>Network Interfaces:</b> IP addresses, netmasks, interface names, network adapters</li>
 *   <li><b>Routing Table:</b> Routes to destinations, gateways, MTU values</li>
 *   <li><b>Gateway Discovery:</b> Default gateway for a particular interface</li>
 *   <li><b>Interface Selection:</b> Choose which IP address to advertise or bind to</li>
 * </ul>
 * <p>
 * <b>Enumerating Network Interfaces:</b>
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * // Get all network interfaces on the system
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 *
 * System.out.println("Network interfaces found: \" + interfaces.size());
 * for (EnumNet.IpInterface iface : interfaces) {
 *     System.out.println(iface);
 *     // Output:
 *     // address: 192.168.1.100, netmask: 255.255.255.0, name: eth0,
 *     // friendlyName: Ethernet, description: Ethernet Adapter,
 *     // preferred: false
 * }
 * </pre>
 * <p>
 * <b>IpInterface Properties:</b>
 * <pre>
 * for (EnumNet.IpInterface iface : interfaces) {
 *     // Get the IP address
 *     Address ifaceAddr = iface.interfaceAddress();
 *     System.out.println(\"IP: \" + ifaceAddr);
 *
 *     // Get the netmask (determines network range)
 *     Address netmask = iface.netmask();
 *     System.out.println(\"Netmask: \" + netmask);
 *
 *     // Get interface name (eth0, wlan0, en0, etc)
 *     String name = iface.name();
 *     System.out.println(\"Interface: \" + name);
 *
 *     // Get friendly name (human-readable)
 *     String friendlyName = iface.friendlyName();
 *     System.out.println(\"Friendly: \" + friendlyName);
 *
 *     // Get description
 *     String description = iface.description();
 *     System.out.println(\"Description: \" + description);
 *
 *     // Check if preferred (marked as default/primary)
 *     boolean preferred = iface.preferred();
 *     System.out.println(\"Preferred: \" + preferred);
 * }
 * </pre>
 * <p>
 * <b>Enumerating Routing Table:</b>
 * <pre>
 * // Get all routes known to the system
 * java.util.List&lt;EnumNet.IpRoute&gt; routes = EnumNet.enumRoutes(sm);
 *
 * System.out.println(\"System routes: \" + routes.size());
 * for (EnumNet.IpRoute route : routes) {
 *     System.out.println(route);
 *     // Output:
 *     // destination: 192.168.1.0, netmask: 255.255.255.0,
 *     // gateway: 192.168.1.1, name: eth0, mtu: 1500
 * }
 * </pre>
 * <p>
 * <b>IpRoute Properties:</b>
 * <pre>
 * for (EnumNet.IpRoute route : routes) {
 *     // Get destination network (e.g., 192.168.0.0)
 *     Address destination = route.destination();
 *     System.out.println(\"Destination: \" + destination);
 *
 *     // Get netmask (determines network size)
 *     Address netmask = route.netmask();
 *     System.out.println(\"Netmask: \" + netmask);
 *
 *     // Get gateway to reach this destination
 *     Address gateway = route.gateway();
 *     System.out.println(\"Gateway: \" + gateway);
 *
 *     // Get interface name (which adapter this route uses)
 *     String name = route.name();
 *     System.out.println(\"Interface: \" + name);
 *
 *     // Get MTU (Maximum Transmission Unit - max packet size)
 *     int mtu = route.mtu();
 *     System.out.println(\"MTU: \" + mtu + \" bytes\");
 * }
 * </pre>
 * <p>
 * <b>Finding Default Gateway:</b>
 * <pre>
 * SessionManager sm = new SessionManager();
 * sm.start();
 *
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 * java.util.List&lt;EnumNet.IpRoute&gt; routes = EnumNet.enumRoutes(sm);
 *
 * // Convert routes to native format for gateway lookup
 * com.frostwire.jlibtorrent.swig.ip_route_vector route_vector =
 *     new com.frostwire.jlibtorrent.swig.ip_route_vector();
 * for (EnumNet.IpRoute route : routes) {
 *     route_vector.add(route.swig());
 * }
 *
 * // Find gateway for each interface
 * for (EnumNet.IpInterface iface : interfaces) {
 *     Address gateway = EnumNet.getGateway(sm, iface, route_vector);
 *     System.out.println(\"Interface \" + iface.name());
 *     System.out.println(\"  Gateway: \" + gateway);
 * }
 * </pre>
 * <p>
 * <b>Identifying Preferred Network Interface:</b>
 * <pre>
 * // Find the preferred interface (usually the main internet connection)
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 *
 * EnumNet.IpInterface preferred = null;
 * for (EnumNet.IpInterface iface : interfaces) {
 *     if (iface.preferred()) {
 *         preferred = iface;
 *         break;
 *     }
 * }
 *
 * if (preferred != null) {
 *     System.out.println(\"Preferred interface: \" + preferred.name());
 *     System.out.println(\"Address: \" + preferred.interfaceAddress());
 * } else {
 *     System.out.println(\"No preferred interface found\");
 * }
 * </pre>
 * <p>
 * <b>Network Connectivity Diagnostics:</b>
 * <pre>
 * // Check if system has IPv4 connectivity
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 * boolean hasIPv4 = false;
 *
 * for (EnumNet.IpInterface iface : interfaces) {
 *     Address addr = iface.interfaceAddress();
 *     if (addr.isV4() &amp;&amp; !addr.isLoopback()) {
 *         hasIPv4 = true;
 *         System.out.println(\"IPv4 via \" + iface.name() + \": \" + addr);
 *     }
 * }
 *
 * if (!hasIPv4) {
 *     System.out.println(\"Warning: No external IPv4 interface detected\");
 * }
 *
 * // Check for IPv6
 * boolean hasIPv6 = false;
 * for (EnumNet.IpInterface iface : interfaces) {
 *     Address addr = iface.interfaceAddress();
 *     if (addr.isV6() &amp;&amp; !addr.isLoopback()) {
 *         hasIPv6 = true;
 *         System.out.println(\"IPv6 via \" + iface.name() + \": \" + addr);
 *     }
 * }
 *
 * if (hasIPv6) {
 *     System.out.println(\"System supports IPv6\");
 * }
 * </pre>
 * <p>
 * <b>Network Configuration for BitTorrent:</b>
 * <pre>
 * // Find suitable interface for listening on peer port
 * java.util.List&lt;EnumNet.IpInterface&gt; interfaces = EnumNet.enumInterfaces(sm);
 *
 * for (EnumNet.IpInterface iface : interfaces) {
 *     Address addr = iface.interfaceAddress();
 *
 *     // Skip loopback and IPv6 link-local
 *     if (addr.isLoopback()) continue;
 *
 *     // Good interface for accepting peer connections
 *     if (addr.isV4() || (!addr.isLoopback() &amp;&amp; addr.isV6())) {
 *         System.out.println(\"Good peer interface: \" + iface.name());
 *         System.out.println(\"  Address: \" + addr);
 *         System.out.println(\"  Listen on port 6881\");
 *     }
 * }
 * </pre>
 * <p>
 * <b>Important Notes:</b>
 * <ul>
 *   <li><b>SessionManager Required:</b> Pass an active SessionManager to these methods</li>
 *   <li><b>Null Check:</b> Methods return empty lists if SessionManager is not started</li>
 *   <li><b>Network Changes:</b> Enumerate dynamically; network configuration may change</li>
 *   <li><b>Platform Dependent:</b> Interface names and properties vary by OS (Linux, macOS, Windows)</li>
 *   <li><b>Special Interfaces:</b> Loopback, VPN, docker interfaces may appear in results</li>
 * </ul>
 *
 * @see Address - IP address representation
 * @see TcpEndpoint - TCP connections to specific addresses
 * @see UdpEndpoint - UDP connections to specific addresses
 * @see SessionManager#start() - Must be called before network enumeration
 *
 * @author gubatron
 * @author aldenml
 */
public final class EnumNet {

    private EnumNet() {
    }

    public static List<IpInterface> enumInterfaces(SessionManager session) {
        if (session.swig() == null) {
            return Collections.emptyList();
        }

        ip_interface_vector v = libtorrent.enum_net_interfaces(session.swig());
        int size = (int) v.size();
        ArrayList<IpInterface> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new IpInterface(v.get(i)));
        }

        return l;
    }

    public static List<IpRoute> enumRoutes(SessionManager session) {
        if (session.swig() == null) {
            return Collections.emptyList();
        }

        ip_route_vector v = libtorrent.enum_routes(session.swig());
        int size = (int) v.size();
        ArrayList<IpRoute> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new IpRoute(v.get(i)));
        }

        return l;
    }

    public static Address getGateway(SessionManager session, IpInterface ipInterface, ip_route_vector routes) {
      return new Address(libtorrent.get_gateway(ipInterface.swig(), routes));
    }

    public static final class IpInterface {

        private final Address interfaceAddress;
        private final Address netmask;
        private final String name;
        private final String friendlyName;
        private final String description;
        private final boolean preferred;
        private final ip_interface s;

        IpInterface(ip_interface iface) {
            this.s = iface;
            this.interfaceAddress = new Address(iface.getInterface_address());
            this.netmask = new Address(iface.getNetmask());
            this.name = Vectors.byte_vector2ascii(iface.getName());
            this.friendlyName = Vectors.byte_vector2ascii(iface.getFriendly_name());
            this.description = Vectors.byte_vector2ascii(iface.getDescription());
            this.preferred = iface.getPreferred();
        }

        public ip_interface swig() {
            return s;
        }

        public Address interfaceAddress() {
            return interfaceAddress;
        }

        public Address netmask() {
            return netmask;
        }

        public String name() {
            return name;
        }

        public String friendlyName() {
            return friendlyName;
        }

        public String description() {
            return description;
        }

        public boolean preferred() {
            return preferred;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("address: ").append(interfaceAddress);
            sb.append(", netmask: ").append(netmask);
            sb.append(", name: ").append(name);
            sb.append(", friendlyName: ").append(friendlyName);
            sb.append(", description: ").append(description);
            sb.append(", preferred: ").append(preferred);

            return sb.toString();
        }
    }

    public static final class IpRoute {

        private final Address destination;
        private final Address netmask;
        private final Address gateway;
        private final String name;
        private final int mtu;
        private final ip_route s;

        IpRoute(ip_route route) {
            this.s = route;
            this.destination = new Address(route.getDestination());
            this.netmask = new Address(route.getNetmask());
            this.gateway = new Address(route.getGateway());
            this.name = Vectors.byte_vector2ascii(route.getName());
            this.mtu = route.getMtu();
        }

        public ip_route swig() {
            return this.s;
        }

        public Address destination() {
            return destination;
        }

        public Address netmask() {
            return netmask;
        }

        public Address gateway() {
            return gateway;
        }

        public String name() {
            return name;
        }

        public int mtu() {
            return mtu;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("destination: ").append(destination);
            sb.append(", netmask: ").append(netmask);
            sb.append(", gateway: ").append(gateway);
            sb.append(", name: ").append(name);
            sb.append(", mtu: ").append(mtu);

            return sb.toString();
        }
    }
}
