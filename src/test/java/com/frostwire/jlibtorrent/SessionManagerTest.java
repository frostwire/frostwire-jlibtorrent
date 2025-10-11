package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for SessionManager to verify null parameter handling.
 * Note: These tests require native library to be available.
 * 
 * @author gubatron
 * @author aldenml
 */
public class SessionManagerTest {

    /**
     * Test that verifies find(Sha1Hash) handles null parameter correctly
     * without throwing NullPointerException.
     * This test addresses the fix for the NPE reported in issue:
     * java.lang.NullPointerException: Attempt to invoke virtual method 
     * 'com.frostwire.jlibtorrent.swig.sha1_hash com.frostwire.jlibtorrent.Sha1Hash.swig()' 
     * on a null object reference
     */
    //@Test // Commented out as it requires native library
    public void testFindWithNullSha1Hash() {
        SessionManager sessionManager = new SessionManager();
        // Test that find with null Sha1Hash returns null without throwing NPE
        TorrentHandle result = sessionManager.find((Sha1Hash) null);
        assertNull("find(Sha1Hash) should return null when sha1 parameter is null", result);
    }

    /**
     * Test that verifies find(Sha256Hash) handles null parameter correctly
     * without throwing NullPointerException.
     * This test addresses the fix for the NPE reported in issue:
     * java.lang.NullPointerException: Attempt to invoke virtual method 
     * 'com.frostwire.jlibtorrent.swig.sha256_hash com.frostwire.jlibtorrent.Sha256Hash.swig()' 
     * on a null object reference at SessionManager.java:463
     */
    //@Test // Commented out as it requires native library
    public void testFindWithNullSha256Hash() {
        SessionManager sessionManager = new SessionManager();
        // Test that find with null Sha256Hash returns null without throwing NPE
        TorrentHandle result = sessionManager.find((Sha256Hash) null);
        assertNull("find(Sha256Hash) should return null when sha256 parameter is null", result);
    }
}
