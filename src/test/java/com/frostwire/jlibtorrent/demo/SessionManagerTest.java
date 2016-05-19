package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.SessionManager;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionManagerTest {

    public static void main(String[] args) throws Throwable {
        SessionManager s = new SessionManager();

        s.start();

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
