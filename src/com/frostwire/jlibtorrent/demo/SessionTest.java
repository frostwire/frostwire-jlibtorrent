package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionTest {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session(6881, 6981, "0.0.0.0");

        s.addListener(new AlertListener() {
            @Override


            public void alert(Alert<?> alert) {
                System.out.println(alert.getType() + " - " + alert.getSwig().what() + " - " + alert.getSwig().message());
            }
        });

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
