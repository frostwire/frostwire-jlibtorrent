package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.EnumNet;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.alerts.Alert;

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
                //System.out.println(alert);
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

        System.out.println("Default gateway: " + EnumNet.defaultGateway(s, "", false));

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.stop();
    }
}
