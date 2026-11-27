package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.DhtImmutableItemAlert;
import com.frostwire.jlibtorrent.alerts.StateUpdateAlert;
import com.frostwire.jlibtorrent.alerts.TorrentRemovedAlert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * {@link SessionHandle} driven directly, on the native session of a loopback {@link SessionManager}.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionHandleTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void torrentsAreAddedFoundListedAndRemoved() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "handle", "a.bin", 30_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));

        try (LoopbackSession s = LoopbackSession.start()) {
            SessionHandle h = new SessionHandle(s.sm.swig());
            assertSame(s.sm.swig(), h.swig());
            assertTrue(h.isValid());
            assertTrue(h.isListening());
            assertTrue(h.getListenPort() > 0);
            assertEquals(s.listenPort(), h.getListenPort());
            h.getSslListenPort();
            assertTrue(h.torrents().isEmpty());
            assertNull(h.findTorrent(ti.infoHashV1()));

            AddTorrentParams p = new AddTorrentParams();
            p.torrentInfo(ti);
            p.savePath(saveDir.getAbsolutePath());
            ErrorCode ec = new ErrorCode(new com.frostwire.jlibtorrent.swig.error_code());
            TorrentHandle th = h.addTorrent(p, ec);
            assertFalse(ec.isError());
            assertTrue(th.isValid());
            assertEquals(ti.infoHashV1(), th.infoHash());

            TorrentHandle found = h.findTorrent(ti.infoHashV1());
            assertNotNull(found);
            assertTrue(found.isValid());
            List<TorrentHandle> all = h.torrents();
            assertEquals(1, all.size());
            assertEquals(ti.infoHashV1(), all.get(0).infoHash());

            // adding the same torrent twice is an error carried in the ErrorCode
            AddTorrentParams again = new AddTorrentParams();
            again.torrentInfo(ti);
            again.savePath(saveDir.getAbsolutePath());
            again.flags(again.flags().or_(TorrentFlags.DUPLICATE_IS_ERROR));
            ErrorCode dup = new ErrorCode(new com.frostwire.jlibtorrent.swig.error_code());
            h.addTorrent(again, dup);
            assertTrue(dup.isError());
            assertFalse(dup.message().isEmpty());

            CompletableFuture<Boolean> removed = s.expect(TorrentRemovedAlert.class);
            h.removeTorrent(th);
            LoopbackSession.get(removed, "the removal");
            assertTrue(h.torrents().isEmpty());
            h.removeTorrent(th); // an invalid handle is ignored
            h.removeTorrent(th, SessionHandle.DELETE_FILES);
            assertTrue(new File(root, "a.bin").exists());
        }
    }

    @Test
    public void asyncAddAndRemoveWithFlags() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "async", "a.bin", 30_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        try (LoopbackSession s = LoopbackSession.start()) {
            SessionHandle h = new SessionHandle(s.sm.swig());
            AddTorrentParams p = new AddTorrentParams();
            p.torrentInfo(ti);
            p.savePath(saveDir.getAbsolutePath());
            CompletableFuture<Boolean> added = s.expect(AddTorrentAlert.class);
            h.asyncAddTorrent(p);
            LoopbackSession.get(added, "the async add");
            TorrentHandle th = h.findTorrent(ti.infoHashV1());
            assertNotNull(th);
            CompletableFuture<Boolean> removed = s.expect(TorrentRemovedAlert.class);
            h.removeTorrent(th, SessionHandle.DELETE_PARTFILE);
            LoopbackSession.get(removed, "the removal");
        }
    }

    @Test
    public void pauseResumeSettingsAndState() {
        try (LoopbackSession s = LoopbackSession.start()) {
            SessionHandle h = new SessionHandle(s.sm.swig());
            assertFalse(h.isPaused());
            h.pause();
            assertTrue(h.isPaused());
            h.resume();
            assertFalse(h.isPaused());

            h.applySettings(new SettingsPack().downloadRateLimit(77_000));
            assertEquals(77_000, h.settings().downloadRateLimit());

            assertEquals(77_000, h.sessionState().getSettings().downloadRateLimit());
            assertEquals(77_000, h.sessionState(SessionHandle.SAVE_SETTINGS).getSettings().downloadRateLimit());

            assertFalse(h.isDhtRunning());
            CompletableFuture<Boolean> updates = s.expect(StateUpdateAlert.class);
            h.postTorrentUpdates();
            LoopbackSession.get(updates, "state updates");
            CompletableFuture<Boolean> updates2 = s.expect(StateUpdateAlert.class);
            h.postTorrentUpdates(TorrentHandle.QUERY_NAME);
            LoopbackSession.get(updates2, "state updates with flags");
            h.postSessionStats();
            h.postDhtStats();
        }
    }

    @Test
    public void networkSocketsAndPortMappings() {
        try (LoopbackSession s = LoopbackSession.start()) {
            SessionHandle h = new SessionHandle(s.sm.swig());
            h.reopenNetworkSockets();
            h.reopenNetworkSockets(SessionHandle.REOPEN_MAP_PORTS);
            int[] handles = h.addPortMapping(PortmapProtocol.TCP, 0, h.getListenPort());
            assertNotNull(handles);
            for (int handle : handles) {
                h.deletePortMapping(handle);
            }
            assertNotNull(SessionHandle.TCP);
            assertNotNull(SessionHandle.UDP);
            assertNotNull(SessionHandle.PAUSED);
            assertNotNull(SessionHandle.SAVE_DHT_STATE);
            assertNotNull(SessionHandle.SAVE_IP_FILTER);
            assertNotNull(SessionHandle.SAVE_EXTENSION_STATE);
            assertTrue(SessionHandle.DHT_ANNOUNCE_SEED != SessionHandle.DHT_ANNOUNCE_IMPLIED_PORT);
            assertTrue(SessionHandle.DHT_ANNOUNCE_SSL_TORRENT != SessionHandle.DHT_ANNOUNCE_SEED);
        }
    }

    @Test
    public void dhtRequestsOnASessionWithoutRoutingNodesStillAnswer() {
        try (LoopbackSession s = LoopbackSession.startDht(false)) {
            SessionHandle h = new SessionHandle(s.sm.swig());
            assertTrue(h.isDhtRunning());
            Sha1Hash target = new Sha1Hash(TestTorrents.bytes(20, 11));
            CompletableFuture<Boolean> item = s.expect(DhtImmutableItemAlert.class);
            h.dhtGetItem(target);
            LoopbackSession.get(item, "the (empty) item answer");
            h.dhtGetPeers(target);
            h.dhtAnnounce(target);
            h.dhtAnnounce(target, 1234, (byte) SessionHandle.DHT_ANNOUNCE_SEED);
        }
    }
}
