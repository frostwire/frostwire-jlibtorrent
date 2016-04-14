package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionTest {

    private static Session session;

    public static synchronized Session session() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    @Test
    public void testIsValid() {
        assertTrue(session().isValid());
    }

    @Test
    public void testIsListening() {
        assertTrue(session().isListening());
    }
}
