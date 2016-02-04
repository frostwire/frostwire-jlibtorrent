package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;

import java.util.Scanner;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtShell {

    public static void main(String[] args) throws Throwable {

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        AlertListener l = new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.getType();
                log(alert.getMessage());

                if (type == AlertType.LISTEN_SUCCEEDED) {
                    ListenSucceededAlert a = (ListenSucceededAlert) alert;
                    log(a.getMessage());
                }

                if (type == AlertType.LISTEN_FAILED) {
                    ListenFailedAlert a = (ListenFailedAlert) alert;
                    log(a.getMessage());
                }

                if (type == AlertType.DHT_PUT) {
                    DhtPutAlert a = (DhtPutAlert) alert;
                    log(a.getMessage());
                }
            }
        };

        Session s = new Session("0.0.0.0", 33123, 10, false, l);
        DHT dht = new DHT(s);

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");

            String line = in.nextLine().trim();

            if (is_quit(line)) {
                quit(s);
            } else if (is_put(line)) {
                put(dht, line);
            } else if (is_invalid(line)) {
                invalid(line);
            }
        }
    }

    private static void print(String s, boolean dollar) {
        System.out.println();
        System.out.println(s);
        if (dollar) {
            System.out.print("$ ");
        }
    }

    private static void print(String s) {
        print(s, false);
    }

    private static void log(String s) {
        print(s, true);
    }

    private static boolean is_quit(String s) {
        s = s.split(" ")[0];
        return s.equals("quit") || s.equals("exit") || s.equals("stop");
    }

    private static void quit(Session s) {
        print("Exiting...");
        s.abort();
        System.exit(0);
    }

    private static boolean is_put(String s) {
        return s.startsWith("put ");
    }

    private static void put(DHT dht, String s) {
        String data = s.split(" ")[1];
        String key = dht.put(new Entry(data));
        print("Wait for completion of put for key: " + key);
    }

    private static boolean is_invalid(String s) {
        return !s.isEmpty();
    }

    private static void invalid(String s) {
        print("Invalid command: " + s + "\n" + "Try ? for help");
    }
}
