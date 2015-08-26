package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EnumNet {

    public static void main(String[] args) throws Throwable {

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        io_service s = new io_service();

        error_code ec = new error_code();
        ip_interface_vector v = libtorrent.enum_net_interfaces(s, ec);
        if (ec.value() != 0) {
            System.out.println(ec.message());
            return;
        }
        long size = v.size();
        for (int i = 0; i < size; i++) {
            ip_interface f = v.get(i);
            System.out.println(f.getName() + ", mtu: " + f.getMtu());
        }

        ip_route_vector v1 = libtorrent.enum_routes(s, ec);
        if (ec.value() != 0) {
            System.out.println(ec.message());
            return;
        }
        size = v1.size();
        for (int i = 0; i < size; i++) {
            ip_route f = v1.get(i);
            System.out.println(f.getName() + ", mtu: " + f.getMtu());
        }

        address g = libtorrent.get_default_gateway(s, ec);
        System.out.println(g.to_string());
    }
}
