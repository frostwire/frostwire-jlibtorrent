package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PluginTest {

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
            }
        });

        s.start();

        SessionHandle h = new SessionHandle(s.swig());
        h.addExtension(new Plugin() {
            @Override
            public boolean onDhtRequest(String query, UdpEndpoint source, BDecodeNode message, Entry response) {
                System.out.println("DHT MSG: " + query + ", from: " + source);
                return false;
            }
        });

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.stop();
    }
}
