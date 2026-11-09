package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.SocketType;
import com.frostwire.jlibtorrent.alerts.TrackerErrorAlert;
import com.frostwire.jlibtorrent.swig.libtorrent_errors;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.socket_type_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * jlibtorrent 2.1.2.0 builds libtorrent with WebTorrent (wss:// trackers, WebRTC peers). None of
 * this needs the network: the wss:// tracker is a closed port on 127.0.0.1.
 */
public class WebTorrentTest {

    // a closed port on this machine: the announce fails fast without leaving it
    private static final String WSS_TRACKER = "wss://127.0.0.1:9/announce";
    // a control: libtorrent drops trackers with a protocol it can't announce to when it loads a
    // .torrent (is_valid_tracker_url), which without WebTorrent includes wss://
    private static final String UNKNOWN_TRACKER = "nosuch://127.0.0.1:9/announce";

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void theNativeLibraryIsBuiltWithWebTorrent() {
        assertTrue("libtorrent built with TORRENT_USE_RTC", LibTorrent.hasWebTorrentSupport());
        assertEquals(socket_type_t.rtc.swigValue(), SocketType.RTC.swig());
        assertEquals(SocketType.RTC, SocketType.fromSwig(socket_type_t.rtc.swigValue()));
    }

    @Test
    public void webTorrentSettingsHaveLibtorrentsDefaultsAndReachTheSession() {
        SettingsPack defaults = new SettingsPack();
        assertEquals("stun.l.google.com:19302", defaults.getWebtorrentStunServer());
        assertEquals(60, defaults.minWebsocketAnnounceInterval());
        assertEquals(120, defaults.webtorrentConnectionTimeout());
        assertEquals(10, defaults.maxWebtorrentOffers());

        SettingsPack sp = offline();
        sp.setWebtorrentStunServer("stun.example.com:3478");
        sp.webtorrentConnectionTimeout(30).maxWebtorrentOffers(4).minWebsocketAnnounceInterval(90);
        SessionManager session = new SessionManager(false);
        session.start(new SessionParams(sp));
        try {
            settings_pack applied = session.swig().get_settings();
            assertEquals("stun.example.com:3478",
                    applied.get_str(settings_pack.string_types.webtorrent_stun_server.swigValue()));
            assertEquals(30, applied.get_int(settings_pack.int_types.webtorrent_connection_timeout.swigValue()));
            assertEquals(4, applied.get_int(settings_pack.int_types.max_webtorrent_offers.swigValue()));
            assertEquals(90, applied.get_int(settings_pack.int_types.min_websocket_announce_interval.swigValue()));
        } finally {
            session.stop();
        }
    }

    @Test
    public void aTorrentWithAWssTrackerLoadsAndAnnouncesOverWebSocket() throws Exception {
        File data = tmp.newFolder("data");
        File folder = new File(data, "folder");
        assertTrue(folder.mkdirs());
        byte[] bytes = new byte[40000];
        new Random(3).nextBytes(bytes);
        Files.write(new File(folder, "a.bin").toPath(), bytes);
        Files.write(new File(folder, "b.bin").toPath(), new byte[]{1, 2, 3});

        byte[] torrent = new TorrentBuilder().path(folder).pieceSize(16384)
                .addTracker(WSS_TRACKER, 0).addTracker(UNKNOWN_TRACKER, 1).generate().entry().bencode();
        TorrentInfo ti = new TorrentInfo(torrent);
        assertEquals("the wss:// tracker is kept, the unknown one dropped", 1, ti.trackers().size());
        assertEquals(WSS_TRACKER, ti.trackers().get(0).url());

        // the first error of the wss:// tracker: copied, alerts die at the next pop. 127.0.0.1:9 is
        // closed, so the WebSocket announce fails without leaving the machine
        AtomicReference<String> error = new AtomicReference<>();
        CountDownLatch failed = new CountDownLatch(1);

        SessionManager session = new SessionManager(false);
        session.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.TRACKER_ERROR.swig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                TrackerErrorAlert e = (TrackerErrorAlert) alert;
                String url = e.trackerUrl();
                if (WSS_TRACKER.equals(url)
                        && error.compareAndSet(null, e.error().value() + ": " + e.error().message())) {
                    failed.countDown();
                }
            }
        });
        session.start(new SessionParams(offline()));
        try {
            session.download(ti, data);
            TorrentHandle handle = null;
            for (int i = 0; i < 100 && (handle == null || !handle.isValid()); i++) {
                Thread.sleep(50);
                handle = session.find(ti);
            }
            assertNotNull(handle);
            List<String> urls = new ArrayList<>();
            for (AnnounceEntry e : handle.trackers()) {
                urls.add(e.url());
            }
            assertTrue(urls.toString(), urls.contains(WSS_TRACKER));

            assertTrue("libtorrent announces to the wss:// tracker", failed.await(30, TimeUnit.SECONDS));
            System.out.println("wss:// tracker error " + error.get());
            assertFalse("wss:// is a supported tracker protocol: " + error.get(),
                    error.get().startsWith(libtorrent_errors.unsupported_url_protocol.swigValue() + ": "));
        } finally {
            session.stop();
        }
    }

    private static SettingsPack offline() {
        SettingsPack sp = new SettingsPack();
        sp.setEnableDht(false);
        sp.setEnableLsd(false);
        sp.listenInterfaces("127.0.0.1:0");
        return sp;
    }
}
