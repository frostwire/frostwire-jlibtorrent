package com.frostwire.jlibtorrent;

import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The alerts thread used to end on any throwable (FrostWire #1559: an OutOfMemoryError reading an
 * alert batch), and after that no alert was processed until restart. It now survives one bad
 * iteration, and can be restarted if it ever dies.
 * Needs the native library, like the other session tests.
 */
public class AlertsLoopSurvivesFailuresTest {

    @Test
    public void aNotStartedManagerHasNoAlertsLoop() {
        SessionManager sm = new SessionManager();
        assertFalse(sm.isAlertsLoopAlive());
        assertFalse("nothing to restart without a session", sm.restartAlertsLoop());
        assertNull(sm.lastAlertsLoopFailure());
        assertEquals(0, sm.alertsLoopFailures());
    }

    @Test
    public void theLoopSurvivesAThrowableInsideItsBody() throws Exception {
        SessionManager sm = new SessionManager(false);
        try {
            sm.start();
        } catch (Throwable noNativeLibrary) {
            Assume.assumeNoException("needs the native library", noNativeLibrary);
        }
        try {
            final int[] thrown = {0};
            sm.alertsLoopFault = () -> {
                if (thrown[0] < 2) {
                    thrown[0]++;
                    throw new OutOfMemoryError("injected");
                }
            };

            long deadline = System.currentTimeMillis() + 5000;
            while (sm.alertsLoopFailures() < 2 && System.currentTimeMillis() < deadline) {
                Thread.sleep(20);
            }

            assertEquals(2, sm.alertsLoopFailures());
            assertTrue(sm.lastAlertsLoopFailure() instanceof OutOfMemoryError);
            assertTrue("the loop is still running after the failures", sm.isAlertsLoopAlive());
            assertFalse("a live loop is not restarted", sm.restartAlertsLoop());
        } finally {
            sm.stop();
        }
        assertFalse(sm.isAlertsLoopAlive());
    }

    @Test
    public void aDeadLoopCanBeRestartedWhileTheSessionRuns() throws Exception {
        SessionManager sm = new SessionManager(false);
        try {
            sm.start();
        } catch (Throwable noNativeLibrary) {
            Assume.assumeNoException("needs the native library", noNativeLibrary);
        }
        try {
            assertTrue(sm.isAlertsLoopAlive());
            sm.killAlertsLoopForTest();
            assertFalse(sm.isAlertsLoopAlive());

            assertTrue(sm.restartAlertsLoop());
            assertTrue(sm.isAlertsLoopAlive());
            assertNotNull(sm);
        } finally {
            sm.stop();
        }
    }
}
