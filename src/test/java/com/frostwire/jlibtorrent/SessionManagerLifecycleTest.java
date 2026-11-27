package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;
import com.frostwire.jlibtorrent.alerts.SessionStatsAlert;
import com.frostwire.jlibtorrent.alerts.StateUpdateAlert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Lifecycle and settings of a real {@link SessionManager}, plus its behaviour while it isn't running.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionManagerLifecycleTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void aStoppedManagerAnswersWithNeutralValues() {
        SessionManager sm = new SessionManager();
        assertFalse(sm.isRunning());
        assertNull(sm.swig());
        assertFalse(sm.isPaused());
        assertFalse(sm.isDhtRunning());
        assertNull(sm.settings());
        assertNull(sm.listenInterfaces());
        assertEquals(0, sm.downloadRateLimit());
        assertEquals(0, sm.uploadRateLimit());
        assertEquals(0, sm.maxActiveDownloads());
        assertEquals(0, sm.maxActiveSeeds());
        assertEquals(0, sm.maxConnections());
        assertEquals(0, sm.maxPeers());
        assertNull(sm.saveState());
        assertEquals("", sm.magnetPeers());
        assertNull(sm.find(Sha1Hash.min()));
        assertNull(sm.find(Sha256Hash.min()));
        assertNull(sm.find((Sha1Hash) null));
        assertNull(sm.find((Sha256Hash) null));
        assertNull(sm.fetchMagnet("magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567", 1, folder.getRoot()));
        assertNull(sm.dhtGetItem(Sha1Hash.min(), 1));
        assertNull(sm.dhtGetItem(new byte[32], new byte[0], 1));
        assertNull(sm.dhtPutItem(new Entry("x")));
        assertTrue(sm.dhtGetPeers(Sha1Hash.min(), 1).isEmpty());
        assertEquals(0, sm.getTorrentHandles().length);
        assertEquals(0, sm.totalDownload());
        assertEquals(0, sm.totalUpload());
        assertEquals(0, sm.downloadRate());
        assertEquals(0, sm.uploadRate());
        assertEquals(0, sm.dhtNodes());
        assertTrue(sm.isFirewalled());
        assertNull(sm.externalAddress());
        assertTrue(sm.listenEndpoints().isEmpty());
        assertNull(sm.lastAlertError());
        assertNotNull(sm.stats());

        // none of these may fail without a session
        sm.pause();
        sm.resume();
        sm.stop();
        sm.postSessionStats();
        sm.postDhtStats();
        sm.postTorrentUpdates();
        sm.startDht();
        sm.stopDht();
        sm.applySettings(new SettingsPack());
        sm.downloadRateLimit(1);
        sm.uploadRateLimit(1);
        sm.maxActiveDownloads(1);
        sm.maxActiveSeeds(1);
        sm.maxConnections(1);
        sm.maxPeers(1);
        sm.listenInterfaces("127.0.0.1:0");
        sm.dhtPutItem(new byte[32], new byte[64], new Entry("x"), new byte[0]);
        sm.dhtAnnounce(Sha1Hash.min());
        sm.dhtAnnounce(Sha1Hash.min(), 1, 0);
        sm.moveStorage(folder.getRoot());
        sm.reopenNetworkSockets();
        sm.download("magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567", folder.getRoot(), new com.frostwire.jlibtorrent.swig.torrent_flags_t());
        assertEquals(0, sm.getTorrentHandles().length);
    }

    @Test
    public void startStopAreIdempotentAndCallTheHooksInOrder() {
        final List<String> calls = new ArrayList<>();
        SessionManager sm = new SessionManager() {
            @Override
            protected void onBeforeStart() {
                calls.add("beforeStart");
            }

            @Override
            protected void onAfterStart() {
                calls.add("afterStart");
            }

            @Override
            protected void onBeforeStop() {
                calls.add("beforeStop");
            }

            @Override
            protected void onAfterStop() {
                calls.add("afterStop");
            }

            @Override
            protected void onApplySettings(SettingsPack sp) {
                calls.add("applySettings");
            }
        };
        try (LoopbackSession s = LoopbackSession.start(sm, new SettingsPack())) {
            assertTrue(sm.isRunning());
            assertNotNull(sm.swig());
            sm.start(); // already running: nothing happens
            sm.applySettings(new SettingsPack().downloadRateLimit(5000));
            assertEquals(Collections.unmodifiableList(java.util.Arrays.asList("beforeStart", "afterStart", "applySettings")), calls);
        }
        assertFalse(sm.isRunning());
        sm.stop(); // already stopped
        assertEquals(java.util.Arrays.asList("beforeStart", "afterStart", "applySettings", "beforeStop", "afterStop"), calls);
    }

    @Test
    public void pauseAndResume() {
        try (LoopbackSession s = LoopbackSession.start()) {
            assertFalse(s.sm.isPaused());
            s.sm.pause();
            assertTrue(s.sm.isPaused());
            s.sm.pause(); // second pause is a no-op
            assertTrue(s.sm.isPaused());
            s.sm.resume();
            assertFalse(s.sm.isPaused());
        }
    }

    @Test
    public void limitsAreAppliedToTheNativeSession() {
        try (LoopbackSession s = LoopbackSession.start()) {
            SessionManager sm = s.sm;
            sm.downloadRateLimit(111_000);
            assertEquals(111_000, sm.downloadRateLimit());
            sm.uploadRateLimit(222_000);
            assertEquals(222_000, sm.uploadRateLimit());
            sm.maxActiveDownloads(7);
            assertEquals(7, sm.maxActiveDownloads());
            sm.maxActiveSeeds(8);
            assertEquals(8, sm.maxActiveSeeds());
            sm.maxConnections(99);
            assertEquals(99, sm.maxConnections());
            sm.maxPeers(1234);
            assertEquals(1234, sm.maxPeers());
            assertEquals("127.0.0.1:0", sm.listenInterfaces());
            sm.listenInterfaces("127.0.0.1:0,[::1]:0");
            assertEquals("127.0.0.1:0,[::1]:0", sm.listenInterfaces());
            assertEquals(111_000, sm.settings().downloadRateLimit());

            try {
                sm.applySettings(null);
                fail("null settings");
            } catch (IllegalArgumentException expected) {
                // expected
            }
        }
    }

    @Test
    public void statsAreUpdatedFromSessionStatsAlerts() {
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> stats = s.expect(SessionStatsAlert.class);
            CompletableFuture<Boolean> updates = s.expect(StateUpdateAlert.class);
            s.sm.postSessionStats();
            s.sm.postTorrentUpdates();
            LoopbackSession.get(stats, "a session stats alert");
            LoopbackSession.get(updates, "a state update alert");
            assertNotNull(s.sm.stats());
            assertTrue(s.sm.downloadRate() >= 0);
            assertTrue(s.sm.uploadRate() >= 0);
            assertTrue(s.sm.totalDownload() >= 0);
            assertTrue(s.sm.totalUpload() >= 0);
            assertEquals(0, s.sm.dhtNodes());

            CompletableFuture<Boolean> dht = s.expect(DhtStatsAlert.class);
            s.sm.postDhtStats();
            LoopbackSession.get(dht, "a dht stats alert");
        }
    }

    @Test
    public void listenStateOfALoopbackSession() {
        try (LoopbackSession s = LoopbackSession.start()) {
            assertTrue(s.listenPort() > 0);
            // loopback addresses are never advertised
            assertTrue(s.sm.listenEndpoints().isEmpty());
            assertEquals("", s.sm.magnetPeers());
            assertNull(s.sm.externalAddress());
            assertTrue(s.sm.isFirewalled());
            s.sm.reopenNetworkSockets();
        }
    }

    @Test
    public void savedStateRestoresTheSettings() {
        byte[] state;
        try (LoopbackSession s = LoopbackSession.start()) {
            s.sm.downloadRateLimit(54_321);
            state = s.sm.saveState();
        }
        assertNotNull(state);
        assertTrue(state.length > 0);
        assertEquals(54_321, new SessionParams(state).getSettings().downloadRateLimit());
    }

    @Test
    public void restartKeepsTheSettings() {
        try (LoopbackSession s = LoopbackSession.start()) {
            s.sm.maxPeers(4321);
            s.sm.restart();
            assertTrue(s.sm.isRunning());
            assertEquals(4321, s.sm.maxPeers());
        }
    }

    @Test
    public void aFailingListenerIsRecordedAndDoesNotStopTheLoop() {
        try (LoopbackSession s = LoopbackSession.start()) {
            final AtomicInteger calls = new AtomicInteger();
            AlertListener bad = new AlertListener() {
                @Override
                public int[] types() {
                    return new int[]{AlertType.SESSION_STATS.swig()};
                }

                @Override
                public void alert(Alert<?> alert) {
                    calls.incrementAndGet();
                    throw new IllegalStateException("listener failure");
                }
            };
            s.sm.addListener(bad);
            s.sm.postSessionStats();
            LoopbackSession.awaitCondition("the failing listener", () -> s.sm.lastAlertError() != null);
            assertTrue(s.sm.lastAlertError() instanceof IllegalStateException);
            assertEquals("listener failure", s.sm.lastAlertError().getMessage());

            s.sm.removeListener(bad);
            int before = calls.get();
            CompletableFuture<Boolean> stats = s.expect(SessionStatsAlert.class);
            s.sm.postSessionStats();
            LoopbackSession.get(stats, "another stats alert");
            assertEquals(before, calls.get());
            s.sm.removeListener(null); // ignored
            s.sm.addListener(null); // ignored
        }
    }

    @Test
    public void dhtCanBeSwitchedOnAndOff() {
        try (LoopbackSession s = LoopbackSession.start()) {
            assertFalse(s.sm.isDhtRunning());
            s.sm.startDht();
            assertTrue(s.sm.isDhtRunning());
            s.sm.startDht(); // already on
            s.sm.stopDht();
            assertFalse(s.sm.isDhtRunning());
            s.sm.stopDht(); // already off
        }
    }
}
