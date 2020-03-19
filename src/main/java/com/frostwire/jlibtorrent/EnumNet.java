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
