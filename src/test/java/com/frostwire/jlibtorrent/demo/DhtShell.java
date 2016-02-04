package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.alerts.Alert;

import java.io.Console;
import java.util.Scanner;

/**
 * @author gubatron
 * @author aldenml
 */
public final class DhtShell {

    public static void main(String[] args) throws Throwable {

        System.out.println("Using libtorrent version: " + LibTorrent.fullVersion());

        Session s = new Session();

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

    private static boolean is_quit(String s) {
        s = s.split(" ")[0];
        return s.equals("quit") || s.equals("exit") || s.equals("stop");
    }

    private static void quit(Session s) {
        System.out.println("Exiting...");
        s.abort();
        System.exit(0);
    }

    private static boolean is_invalid(String s) {
        return !s.isEmpty();
    }

    private static void invalid(String s) {
        System.out.println("Invalid command: " + s);
        System.out.println("Try ? for help");
    }
}
