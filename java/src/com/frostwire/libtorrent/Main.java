package com.frostwire.libtorrent;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] paths = {"/Users/aldenml/Downloads/test.pdf"};

        // trigger the native library's load
        System.out.println(LibTorrent.version());
        final Session s = new Session();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                s.waitForAlerts(10000);
            }
        });

        t.setDaemon(true);
        t.start();

        s.startNetworking();

        System.out.println("ENTER to exit");
        Thread.sleep(5000);
    }
}
