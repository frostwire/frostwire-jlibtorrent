package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.UPnP;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UPnPTest2 {

    private static final int[] ALERT_TYPES = new int[]{AlertType.PORTMAP.getSwig(), AlertType.PORTMAP_ERROR.getSwig()};

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();


        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return ALERT_TYPES;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
            }
        });

        System.out.println("Waiting 5 seconds to map a port");
        Thread.sleep(5000);

        UPnP upnp = s.getUPnP();

        System.out.println("Router model: " + upnp.routerModel());

        int index = upnp.addMapping(UPnP.ProtocolType.TCP, 7676, 7676);
        Thread.sleep(5000);

        UPnP.Mapping m = upnp.getMapping(index);
        System.out.println(m);

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
