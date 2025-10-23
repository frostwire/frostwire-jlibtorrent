package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.portmap_transport;

/**
 * Transport protocol for automatic port mapping on home routers.
 * <p>
 * {@code PortmapTransport} specifies which protocol to use when automatically opening ports
 * on the router to enable incoming BitTorrent peer connections. Both UPnP and NAT-PMP are
 * zero-configuration protocols that allow applications to request port forwarding without
 * manual router configuration.
 * <p>
 * <b>Understanding Port Mapping Protocols:</b>
 * <pre>
 * Without Port Mapping:
 *   - You connect to peers (outgoing works through NAT)
 *   - Peers cannot connect to you (incoming blocked by NAT/firewall)
 *   - Result: Reduced connectivity, slower downloads, lower upload opportunities
 *
 * With Port Mapping (UPnP or NAT-PMP):
 *   - Router automatically forwards incoming TCP port to your application
 *   - Router automatically forwards incoming UDP port to your application
 *   - Peers can initiate connections to you
 *   - Result: Full connectivity, faster downloads, enabled seeding
 * </pre>
 * <p>
 * <b>NAT-PMP (Network Address Translation Port Mapping Protocol):</b>
 * <ul>
 *   <li><b>Standard:</b> RFC 6886 (Apple protocol, widely supported)</li>
 *   <li><b>Discovery:</b> Uses multicast on local network (224.0.0.1:5350)</li>
 *   <li><b>Routers:</b> Apple AirPort, many modern routers</li>
 *   <li><b>Setup Time:</b> Usually 1-2 seconds</li>
 *   <li><b>Simplicity:</b> Simpler than UPnP, less overhead</li>
 *   <li><b>Lease Time:</b> Port mappings expire and must be renewed (typically 1 hour)</li>
 * </ul>
 * <p>
 * <b>UPnP (Universal Plug and Play):</b>
 * <ul>
 *   <li><b>Standard:</b> IETF standard (widely deployed)</li>
 *   <li><b>Discovery:</b> Uses SSDP multicast on local network</li>
 *   <li><b>Routers:</b> Almost all modern home routers support UPnP</li>
 *   <li><b>Setup Time:</b> Usually 2-5 seconds (more complex than NAT-PMP)</li>
 *   <li><b>Complexity:</b> More feature-rich but higher overhead</li>
 *   <li><b>Lease Time:</b> Typically 30 minutes to 1 hour (must be renewed)</li>
 * </ul>
 * <p>
 * <b>Comparison Table:</b>
 * <table border="1">
 * <caption>Transport Protocol Comparison</caption>
 *   <tr><th>Feature</th><th>NAT-PMP</th><th>UPnP</th></tr>
 *   <tr><td>Standard</td><td>RFC 6886 (Apple)</td><td>IETF / IEC</td></tr>
 *   <tr><td>Typical Port Time</td><td>1-2 seconds</td><td>2-5 seconds</td></tr>
 *   <tr><td>Router Support</td><td>Good (Apple, some others)</td><td>Excellent (most routers)</td></tr>
 *   <tr><td>Lease Renewal</td><td>Required (~1 hour)</td><td>Required (~30 min)</td></tr>
 *   <tr><td>Complexity</td><td>Simpler</td><td>More features</td></tr>
 *   <tr><td>Security Notes</td><td>More minimal API</td><td>Broader functionality</td></tr>
 * </table>
 * <p>
 * <b>Configuring Port Mapping:</b>
 * <pre>
 * // Typically handled automatically by SessionManager/SettingsPack
 * // These enums appear in PortmapAlert and PortmapErrorAlert
 *
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {
 *             AlertType.PORTMAP_ALERT.swig(),
 *             AlertType.PORTMAP_ERROR_ALERT.swig()
 *         };
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         if (alert instanceof PortmapAlert) {
 *             PortmapAlert a = (PortmapAlert) alert;
 *             PortmapTransport transport = a.mapTransport();
 *             System.out.println("Port mapped via " + transport);
 *             // transport is either NAT_PMP or UPNP
 *         } else if (alert instanceof PortmapErrorAlert) {
 *             PortmapErrorAlert a = (PortmapErrorAlert) alert;
 *             PortmapTransport transport = a.mapTransport();
 *             System.err.println("Failed to map port via " + transport);
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>Port Mapping Behavior:</b>
 * <pre>
 * Startup:
 *   1. jlibtorrent detects available routers on local network
 *   2. Tries configured transport (UPnP first by default)
 *   3. Requests port mapping with timeout
 *   4. On success: PortmapAlert is generated with port number
 *   5. On failure: PortmapErrorAlert generated, may try next protocol
 *
 * Periodic:
 *   - Port leases must be renewed before expiration
 *   - jlibtorrent automatically re-maps before lease expires
 *   - If router becomes unavailable, mapping is cleaned up
 *
 * Shutdown:
 *   - jlibtorrent sends unmapping request to router
 *   - Router removes the port forwarding rule
 * </pre>
 * <p>
 * <b>When Port Mapping Fails:</b>
 * <ul>
 *   <li><b>Router doesn't support UPnP/NAT-PMP:</b> Manual port forwarding in router settings</li>
 *   <li><b>Router has UPnP disabled:</b> Enable in router configuration</li>
 *   <li><b>Firewall blocking:</b> Allow UPnP/NAT-PMP in firewall</li>
 *   <li><b>No router found:</b> Check network connection</li>
 *   <li><b>Port already in use:</b> Change listening port in settings</li>
 * </ul>
 * <p>
 * <b>Settings for Port Mapping:</b>
 * <pre>
 * SettingsPack settings = new SettingsPack();
 *
 * // Enable port mapping (usually enabled by default)
 * settings.setBoolean("enable_upnp", true);    // Try UPnP
 * settings.setBoolean("enable_natpmp", true);  // Try NAT-PMP
 *
 * // Port mapping retry settings
 * settings.setInteger("natpmp_tick_interval", 120);  // Re-map every 2 minutes
 * settings.setInteger("upnp_lsd_timeout", 250);      // UPnP discovery timeout
 *
 * sm.applySettings(settings);
 * </pre>
 * <p>
 * <b>Performance Impact:</b>
 * <ul>
 *   <li>Initial port mapping takes 1-5 seconds (done at startup)</li>
 *   <li>Minimal overhead once established (just periodic renewal)</li>
 *   <li>Improves connectivity significantly (incoming connections possible)</li>
 *   <li>Worth enabling for most applications (home networks, servers)</li>
 * </ul>
 *
 * @see PortmapProtocol - Which ports (TCP/UDP) are mapped
 * @see SettingsPack - Configure port mapping settings
 *
 * @author gubatron
 * @author aldenml
 */
public enum PortmapTransport {

    /**
     * NAT-PMP (Network Address Translation Port Mapping Protocol).
     * Simpler, faster, requires RFC 6886 compatible router (Apple AirPort, some modern routers).
     */
    NAT_PMP(portmap_transport.natpmp.swigValue()),

    /**
     * UPnP (Universal Plug and Play).
     * More widely supported by home routers, but slightly more overhead.
     */
    UPNP(portmap_transport.upnp.swigValue());

    PortmapTransport(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int swig() {
        return swigValue;
    }

    public static PortmapTransport fromSwig(int swigValue) {
        PortmapTransport[] enumValues = PortmapTransport.class.getEnumConstants();
        for (PortmapTransport ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + PortmapTransport.class + " with value " + swigValue);
    }
}
