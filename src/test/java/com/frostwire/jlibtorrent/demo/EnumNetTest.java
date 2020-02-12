package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.EnumNet;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.swig.ip_route_vector;

import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EnumNetTest {

    public static void main(String[] args) throws Throwable {

        SessionManager s = new SessionManager();

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert);
                //System.out.println("listen_interfaces: " + s.listenInterfaces());
            }
        });

        s.start();

        System.out.println("Enum interfaces");
        List<EnumNet.IpInterface> ipInterfaces = EnumNet.enumInterfaces(s);
        for (EnumNet.IpInterface iface : ipInterfaces) {
            System.out.println(iface);
        }

        System.out.println("Enum routers");
        List<EnumNet.IpRoute> ipRoutes = EnumNet.enumRoutes(s);
        for (EnumNet.IpRoute route : ipRoutes) {
            System.out.println(route);
        }

        ip_route_vector ip_routes = new ip_route_vector();
        for (EnumNet.IpRoute route : ipRoutes) {
            ip_routes.push_back(route.swig());
        }
        for (EnumNet.IpInterface iface : ipInterfaces) {
            System.out.println("Default gateway for iface=" + iface + " -> " + EnumNet.getGateway(s, iface, ip_routes));
        }

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.stop();
    }
}
