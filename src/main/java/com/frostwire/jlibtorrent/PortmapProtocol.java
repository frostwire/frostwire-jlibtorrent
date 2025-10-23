package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.portmap_protocol;

/**
 * Protocol for port mapping with UPnP and NAT-PMP.
 * <p>
 * {@code PortmapProtocol} identifies which port mapping protocol to use when
 * automatically opening ports on the router. UPnP (Universal Plug and Play) and
 * NAT-PMP (Network Address Translation Protocol Mapping) allow applications to
 * request the router to forward incoming connections to the application's port.
 * <p>
 * <b>Understanding Port Mapping:</b>
 * <br/>
 * Port mapping enables incoming peer connections through NAT/firewall:
 * <ul>
 *   <li><b>TCP:</b> BitTorrent peer connections (data transfer)</li>
 *   <li><b>UDP:</b> DHT and uTP protocol (peer discovery, alternative transport)</li>
 *   <li><b>NONE:</b> No port mapping attempted (static port or manual config)</li>
 * </ul>
 * <p>
 * <b>Port Mapping Protocols:</b>
 * <pre>
 * NONE:
 *   - No port mapping is attempted
 *   - Use if router doesn't support mapping or you manage ports manually
 *   - May reduce incoming connections if behind NAT
 *
 * TCP:
 *   - Map TCP port for BitTorrent peer connections
 *   - Allows incoming peers to connect for block transfers
 *   - Primary protocol for download/upload
 *
 * UDP:
 *   - Map UDP port for DHT and uTP
 *   - Enables direct peer discovery via DHT
 *   - Alternative transport protocol (faster for some scenarios)
 * </pre>
 * <p>
 * <b>Configuring Port Mapping:</b>
 * <pre>
 * // Example: Enable UPnP for both TCP and UDP
 * SettingsPack settings = new SettingsPack();
 * settings.setString("upnp_ignore_nonrouters", "true");
 * settings.setInteger("upnp_lsd_timeout", 250);
 *
 * // Port mapping happens automatically if enabled in settings
 * // Use PortmapProtocol via portmap_error_alert to diagnose issues
 * </pre>
 * <p>
 * <b>Port Mapping Error Handling:</b>
 * <pre>
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PORTMAP_ERROR.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PortmapErrorAlert a = (PortmapErrorAlert) alert;
 *         PortmapProtocol protocol = a.mapProtocol();
 *
 *         System.err.println("Port mapping failed for: \" + protocol);
 *         // TCP mapping failed: no incoming peer connections possible
 *         // UDP mapping failed: DHT and uTP may not work
 *     }
 * });
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Port mapping discovery takes 1-5 seconds on startup</li>
 *   <li>Improves connectivity significantly on NAT'd networks</li>
 *   <li>Not available if router doesn't support UPnP/NAT-PMP</li>
 *   <li>Manual port forwarding always works as alternative</li>
 * </ul>
 *
 * @see SettingsPack - Configure port mapping settings
 * @see PortmapAlert - Successful port mapping
 * @see PortmapErrorAlert - Port mapping failure
 *
 * @author gubatron
 * @author aldenml
 */
public enum PortmapProtocol {

    NONE(portmap_protocol.none.swigValue()),

    TCP(portmap_protocol.tcp.swigValue()),

    UDP(portmap_protocol.udp.swigValue());

    PortmapProtocol(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    public int swig() {
        return swigValue;
    }

    public static PortmapProtocol fromSwig(int swigValue) {
        PortmapProtocol[] enumValues = PortmapProtocol.class.getEnumConstants();
        for (PortmapProtocol ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("No enum " + PortmapProtocol.class + " with value " + swigValue);
    }
}
