package com.frostwire.libtorrent;

import com.frostwire.libtorrent.alerts.Alert;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] paths = {"/Users/aldenml/Downloads/test.pdf"};

        // trigger the native library's load
        System.out.println(LibTorrent.version());
        final Session s = new Session();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<Alert> alerts = s.waitForAlerts(1000);
                    for (Alert alert : alerts) {
                        System.out.println(alert.timestamp + " - " + alert.what);
                    }
                    System.out.println("loop");
                }
            }
        });

        t.setDaemon(true);
        t.start();

        s.startNetworking();

        System.out.println("Enter to exit");
        System.in.read();
    }
}
