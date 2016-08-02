package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.alerts.Alert;

import java.util.Map;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionTest {

    public static void main(String[] args) throws Throwable {

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert);
            }
        };

        Session s = new Session(new SettingsPack(), false, l);

        System.out.println("Press ENTER to exit");
        System.in.read();

        s.abort();
    }
}
