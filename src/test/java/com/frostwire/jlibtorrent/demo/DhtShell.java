package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.ListenSucceededAlert;

import java.util.Scanner;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtShell {

    public static void main(String[] args) throws Throwable {

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        Session s = new Session("0.0.0.0", 33123, 10);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.getType();

                if (type == AlertType.LISTEN_SUCCEEDED) {
                    ListenSucceededAlert a = (ListenSucceededAlert) alert;
                    print(a.getMessage(), true);
                }
            }
        });

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");

            String line = in.nextLine().trim();

            if (is_quit(line)) {
                quit(s);
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

    private static boolean is_quit(String s) {
        s = s.split(" ")[0];
        return s.equals("quit") || s.equals("exit") || s.equals("stop");
    }

    private static void quit(Session s) {
        print("Exiting...");
        s.abort();
        System.exit(0);
    }

    private static boolean is_invalid(String s) {
        return !s.isEmpty();
    }

    private static void invalid(String s) {
        print("Invalid command: " + s + "\n" + "Try ? for help");
    }
}
