package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.FileErrorAlert;
import com.frostwire.jlibtorrent.alerts.ScrapeFailedAlert;
import com.frostwire.jlibtorrent.alerts.ScrapeReplyAlert;
import com.frostwire.jlibtorrent.alerts.TorrentErrorAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TrackerAnnounceAlert;
import com.frostwire.jlibtorrent.alerts.TrackerErrorAlert;
import com.frostwire.jlibtorrent.alerts.TrackerReplyAlert;
import com.frostwire.jlibtorrent.alerts.TrackerWarningAlert;
import com.frostwire.jlibtorrent.alerts.TrackeridAlert;
import com.frostwire.jlibtorrent.alerts.UrlSeedAlert;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tracker, scrape and web seed alerts, against HTTP servers on the loopback interface that answer
 * as a tracker (or a broken one) would.
 *
 * @author gubatron
 * @author aldenml
 */
public class TrackerAndWebSeedAlertsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private HttpServer server;

    @After
    public void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private interface Responder {
        byte[] respond(HttpExchange ex);
    }

    private int serve(String path, int status, Responder body) throws IOException {
        if (server == null) {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 0), 0);
            server.start();
        }
        server.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange ex) throws IOException {
                byte[] data = body.respond(ex);
                ex.sendResponseHeaders(status, data.length == 0 ? -1 : data.length);
                if (data.length > 0) {
                    try (OutputStream out = ex.getResponseBody()) {
                        out.write(data);
                    }
                }
                ex.close();
            }
        });
        return server.getAddress().getPort();
    }

    private TorrentInfo torrent(File root, String trackerUrl) throws IOException {
        TorrentBuilder b = new TorrentBuilder().path(root).pieceSize(TestTorrents.PIECE_SIZE).flags(TorrentBuilder.V1_ONLY);
        if (trackerUrl != null) {
            b.addTracker(trackerUrl);
        }
        return TestTorrents.torrentInfo(b);
    }

    private static byte[] ascii(String s) {
        return s.getBytes(StandardCharsets.ISO_8859_1);
    }

    @Test
    public void aTrackerThatAnswersProducesReplyIdWarningAndScrapeAlerts() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "tracked", "a.bin", 40_000);
        final TorrentInfo[] ti = new TorrentInfo[1];

        Map<String, Object> announce = new LinkedHashMap<>();
        announce.put("interval", 1800);
        announce.put("min interval", 60);
        announce.put("complete", 1);
        announce.put("incomplete", 2);
        announce.put("tracker id", "tracker-42");
        announce.put("warning message", "mind the gap");
        announce.put("peers", "");
        byte[] announceBody = Entry.fromMap(announce).bencode();
        int port = serve("/announce", 200, ex -> announceBody);
        serve("/scrape", 200, ex -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] head = ascii("d5:filesd20:");
            out.write(head, 0, head.length);
            byte[] hash = Hex.decode(ti[0].infoHashV1().toHex());
            out.write(hash, 0, hash.length);
            byte[] tail = ascii("d8:completei3e10:downloadedi4e10:incompletei5eeee");
            out.write(tail, 0, tail.length);
            return out.toByteArray();
        });
        ti[0] = torrent(root, "http://127.0.0.1:" + port + "/announce");

        // the scrape path of a local tracker doesn't contain "announce", which the SSRF mitigation blocks
        SettingsPack local = new SettingsPack();
        local.setBoolean(com.frostwire.jlibtorrent.swig.settings_pack.bool_types.ssrf_mitigation.swigValue(), false);
        try (LoopbackSession s = LoopbackSession.start(true, local)) {
            CompletableFuture<String> announced = s.expect(TrackerAnnounceAlert.class, a -> true, TrackerAnnounceAlert::trackerUrl);
            CompletableFuture<Integer> reply = s.expect(TrackerReplyAlert.class, a -> true, TrackerReplyAlert::numPeers);
            CompletableFuture<String> id = s.expect(TrackeridAlert.class, a -> true, TrackeridAlert::trackerId);
            CompletableFuture<String> warning = s.expect(TrackerWarningAlert.class, a -> true, TrackerWarningAlert::warningMessage);
            s.sm.download(ti[0], saveDir);

            assertTrue(LoopbackSession.get(announced, "the announce").endsWith("/announce"));
            assertEquals(0, (int) LoopbackSession.get(reply, "the tracker reply"));
            assertEquals("tracker-42", LoopbackSession.get(id, "the tracker id"));
            assertEquals("mind the gap", LoopbackSession.get(warning, "the warning"));

            LoopbackSession.awaitCondition("the torrent", () -> s.sm.find(ti[0]) != null);

            // the announce resolved the tracker: its endpoints carry per info hash state
            LoopbackSession.awaitCondition("tracker endpoints", () -> {
                java.util.List<AnnounceEntry> trackers = s.sm.find(ti[0]).trackers();
                return !trackers.isEmpty() && !trackers.get(0).endpoints().isEmpty();
            });
            AnnounceEndpoint endpoint = s.sm.find(ti[0]).trackers().get(0).endpoints().get(0);
            assertEquals(java.util.Collections.<String>emptyList(), Accessors.touch(endpoint));
            assertTrue(endpoint.enabled());
            AnnounceInfohash v1 = endpoint.infohashV1();
            assertEquals(java.util.Collections.<String>emptyList(), Accessors.touch(v1));
            assertTrue(v1.isWorking());
            CompletableFuture<int[]> scrape = s.expect(ScrapeReplyAlert.class, a -> true,
                    a -> new int[]{a.getComplete(), a.getIncomplete()});
            s.sm.find(ti[0]).scrapeTracker();
            int[] counts = LoopbackSession.get(scrape, "the scrape reply");
            assertEquals(3, counts[0]);
            assertEquals(5, counts[1]);
            assertEquals(java.util.Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void aTrackerThatRefusesProducesErrorAlerts() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "refused", "a.bin", 40_000);
        byte[] failure = ascii("d14:failure reason9:go away!!e");
        int port = serve("/announce", 200, ex -> failure);
        serve("/scrape", 404, ex -> new byte[0]);
        TorrentInfo ti = torrent(root, "http://127.0.0.1:" + port + "/announce");

        SettingsPack local = new SettingsPack();
        local.setBoolean(com.frostwire.jlibtorrent.swig.settings_pack.bool_types.ssrf_mitigation.swigValue(), false);
        try (LoopbackSession s = LoopbackSession.start(true, local)) {
            CompletableFuture<String> error = s.expect(TrackerErrorAlert.class, a -> true, TrackerErrorAlert::errorMessage);
            s.sm.download(ti, saveDir);
            assertEquals("go away!!", LoopbackSession.get(error, "the tracker error"));

            LoopbackSession.awaitCondition("the torrent", () -> s.sm.find(ti) != null);
            CompletableFuture<Integer> failed = s.expect(ScrapeFailedAlert.class, a -> true, a -> a.error().value());
            CompletableFuture<String> message = s.expect(ScrapeFailedAlert.class, a -> true, ScrapeFailedAlert::errorMessage);
            s.sm.find(ti).scrapeTracker();
            assertEquals(404, (int) LoopbackSession.get(failed, "the failed scrape"));
            LoopbackSession.get(message, "the scrape failure message");
            assertEquals(java.util.Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void aWebSeedThatFailsProducesAUrlSeedAlert() throws IOException {
        File seedDir = folder.newFolder("seed");
        File root = TestTorrents.content(seedDir, "web", "w.bin", 40_000);
        int port = serve("/", 500, ex -> new byte[0]);
        TorrentInfo ti = torrent(root, null);

        try (LoopbackSession s = LoopbackSession.start(true, new SettingsPack())) {
            CompletableFuture<String> failed = s.expect(UrlSeedAlert.class, a -> true, UrlSeedAlert::serverUrl);
            CompletableFuture<Integer> code = s.expect(UrlSeedAlert.class, a -> true, a -> a.error().value());
            CompletableFuture<String> message = s.expect(UrlSeedAlert.class, a -> true, UrlSeedAlert::errorMessage);
            s.sm.download(ti, folder.newFolder("dl"));
            LoopbackSession.awaitCondition("the torrent", () -> s.sm.find(ti) != null);
            s.sm.find(ti).addUrlSeed("http://127.0.0.1:" + port + "/");

            assertTrue(LoopbackSession.get(failed, "the failing web seed").startsWith("http://127.0.0.1:" + port));
            LoopbackSession.get(code, "the web seed error code");
            LoopbackSession.get(message, "the web seed error message");
            assertEquals(java.util.Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void aFileThatCannotBeWrittenStopsTheTorrentWithAnError() throws IOException {
        File seedDir = folder.newFolder("seed");
        File root = TestTorrents.content(seedDir, "broken", "b.bin", 40_000);
        TorrentInfo ti = torrent(root, null);

        File leechDir = folder.newFolder("leech");
        // a directory where the file should be: opening it for writing fails
        assertTrue(new File(leechDir, "broken/b.bin").mkdirs());

        try (LoopbackSession seeder = LoopbackSession.start();
             LoopbackSession leecher = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = seeder.expect(TorrentFinishedAlert.class);
            seeder.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the seeder");

            CompletableFuture<String> fileError = leecher.expect(FileErrorAlert.class, a -> true,
                    a -> a.filename() + "|" + a.operation() + "|" + a.error().value());
            CompletableFuture<String> torrentError = leecher.expect(TorrentErrorAlert.class, a -> true,
                    a -> a.filename() + "|" + a.error().value());
            leecher.sm.download(ti, leechDir, null, null, Collections.singletonList(seeder.endpoint()), new torrent_flags_t());
            String fe = LoopbackSession.get(fileError, "the file error");
            assertTrue(fe, fe.startsWith(new File(leechDir, "broken/b.bin").getAbsolutePath()) || fe.contains("b.bin"));
            LoopbackSession.get(torrentError, "the torrent error");
            assertEquals(java.util.Collections.<String>emptyList(), leecher.accessorFailures());
        }
    }

    @Test
    public void anExternalAddressReportedByATrackerIsKnownAndAdvertisedInMagnets() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "external", "a.bin", 40_000);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] head = ascii("d8:intervali1800e11:external ip4:");
        out.write(head, 0, head.length);
        out.write(new byte[]{(byte) 203, 0, 113, 7}, 0, 4); // 203.0.113.7, reserved for documentation
        byte[] tail = ascii("5:peers0:e");
        out.write(tail, 0, tail.length);
        byte[] body = out.toByteArray();
        int port = serve("/announce", 200, ex -> body);
        TorrentInfo ti = torrent(root, "http://127.0.0.1:" + port + "/announce");

        try (LoopbackSession s = LoopbackSession.start(true, new SettingsPack())) {
            assertEquals(null, s.sm.externalAddress());
            assertEquals("", s.sm.magnetPeers());
            CompletableFuture<String> external = s.expect(com.frostwire.jlibtorrent.alerts.ExternalIpAlert.class, a -> true,
                    a -> a.externalAddress().toString());
            s.sm.download(ti, saveDir);
            assertEquals("203.0.113.7", LoopbackSession.get(external, "the external address"));
            LoopbackSession.awaitCondition("the manager to record it", () -> s.sm.externalAddress() != null);
            assertEquals("203.0.113.7", s.sm.externalAddress());
            assertTrue(s.sm.magnetPeers(), s.sm.magnetPeers().startsWith("&x.pe=203.0.113.7:"));
            assertEquals(java.util.Collections.<String>emptyList(), s.accessorFailures());
        }
    }
}
