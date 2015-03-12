package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.swig.upnp;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UPnPTest {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
            }
        });

        System.out.println("Waiting 5 seconds to map a port");
        Thread.sleep(5000);

        upnp upnp = s.getSwig().get_upnp();

        System.out.println("Router model: " + upnp.router_model());

        upnp.add_mapping(com.frostwire.jlibtorrent.swig.upnp.protocol_type.tcp, 7676, 7676);

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
