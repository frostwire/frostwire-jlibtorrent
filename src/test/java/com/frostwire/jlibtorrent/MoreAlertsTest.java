package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AlertsDroppedAlert;
import com.frostwire.jlibtorrent.alerts.DhtReplyAlert;
import com.frostwire.jlibtorrent.alerts.FastresumeRejectedAlert;
import com.frostwire.jlibtorrent.alerts.I2pAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataAlert;
import com.frostwire.jlibtorrent.alerts.SaveResumeDataFailedAlert;
import com.frostwire.jlibtorrent.alerts.Socks5Alert;
import com.frostwire.jlibtorrent.alerts.TorrentDeleteFailedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentErrorAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * More alerts that real sessions post when something is missing, broken or overloaded.
 *
 * @author gubatron
 * @author aldenml
 */
public class MoreAlertsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File seedDir;
    private File root;
    private TorrentInfo ti;

    private void createTorrent() throws IOException {
        seedDir = folder.newFolder("seed");
        root = TestTorrents.content(seedDir, "more", "m.bin", 40_000);
        ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
    }

    @Test
    public void resumeDataOfMissingFilesIsRejected() throws IOException {
        createTorrent();
        File resume = new File(folder.getRoot(), "resume.dat");
        try (LoopbackSession seeder = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = seeder.expect(TorrentFinishedAlert.class);
            seeder.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the seeder");
            CompletableFuture<byte[]> saved = seeder.expect(SaveResumeDataAlert.class, a -> true,
                    a -> AddTorrentParams.writeResumeData(a.params()).bencode());
            seeder.sm.find(ti).saveResumeData(TorrentHandle.SAVE_INFO_DICT);
            Utils.writeByteArrayToFile(resume, LoopbackSession.get(saved, "the resume data"), false);
        }

        // the files the resume data talks about aren't in the new save path
        try (LoopbackSession s = LoopbackSession.start(true, new SettingsPack())) {
            CompletableFuture<String> rejected = s.expect(FastresumeRejectedAlert.class, a -> true,
                    a -> a.filePath() + "|" + a.operation() + "|" + a.error().value());
            s.sm.download(ti, folder.newFolder("elsewhere"), resume, null, null, new torrent_flags_t());
            String description = LoopbackSession.get(rejected, "the rejection");
            assertTrue(description, description.contains("m.bin") || description.contains("|"));
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void resumeDataThatIsNotModifiedIsNotSavedAgain() {
        try (LoopbackSession s = LoopbackSession.start()) {
            String magnet = "magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567";
            CompletableFuture<Boolean> added = s.expect(com.frostwire.jlibtorrent.alerts.AddTorrentAlert.class);
            s.sm.download(magnet, folder.getRoot(), new torrent_flags_t());
            LoopbackSession.get(added, "the magnet torrent");
            TorrentHandle th = s.sm.find(new Sha1Hash("0123456789abcdef0123456789abcdef01234567"));
            CompletableFuture<Boolean> saved = s.expect(SaveResumeDataAlert.class);
            th.saveResumeData();
            LoopbackSession.get(saved, "the first save");
            // nothing changed since: asking to save only if modified fails
            CompletableFuture<Integer> failed = s.expect(SaveResumeDataFailedAlert.class, a -> true, a -> a.error().value());
            th.saveResumeData(TorrentHandle.ONLY_IF_MODIFIED);
            assertTrue(LoopbackSession.get(failed, "the failure") != 0);
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void filesThatCannotBeDeletedAreReported() throws IOException {
        createTorrent();
        File leechDir = folder.newFolder("leech");
        File asFile = new File(leechDir, "more/m.bin");
        // a non-empty directory where the file should be: the torrent errors, and deleting it fails
        assertTrue(asFile.mkdirs());
        assertTrue(new File(asFile, "inner.txt").createNewFile());

        try (LoopbackSession seeder = LoopbackSession.start();
             LoopbackSession leecher = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = seeder.expect(TorrentFinishedAlert.class);
            seeder.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the seeder");

            CompletableFuture<Boolean> error = leecher.expect(TorrentErrorAlert.class);
            leecher.sm.download(ti, leechDir, null, null, Collections.singletonList(seeder.endpoint()), new torrent_flags_t());
            LoopbackSession.get(error, "the torrent error");

            CompletableFuture<Integer> failed = leecher.expect(TorrentDeleteFailedAlert.class, a -> true,
                    a -> {
                        assertEquals(ti.infoHashV1(), a.getInfoHash());
                        return a.getError().value();
                    });
            leecher.sm.remove(leecher.sm.find(ti), SessionHandle.DELETE_FILES);
            assertTrue(LoopbackSession.get(failed, "the failed deletion") != 0);
            assertEquals(Collections.<String>emptyList(), leecher.accessorFailures());
        }
    }

    @Test
    public void aFullAlertQueueDropsAlertsAndSaysSo() throws IOException {
        createTorrent();
        SettingsPack sp = new SettingsPack();
        sp.alertQueueSize(10);
        try (LoopbackSession s = LoopbackSession.start(true, sp)) {
            CompletableFuture<Boolean> dropped = s.expect(AlertsDroppedAlert.class);
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, seedDir);
            // a burst of requests while the alert thread is busy overflows the small queue
            LoopbackSession.awaitCondition("dropped alerts", () -> {
                s.sm.postSessionStats();
                s.sm.postTorrentUpdates();
                TorrentHandle th = s.sm.find(ti);
                if (th != null) {
                    th.swig().post_peer_info();
                    th.swig().post_trackers();
                    th.swig().post_file_progress(TorrentHandle.PIECE_GRANULARITY);
                    th.swig().post_piece_availability();
                    th.swig().post_download_queue();
                    th.postFileStatus();
                    th.postFilePriorities();
                }
                return dropped.isDone();
            });
            assertTrue(finished != null);
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void anUnreachableProxyIsReported() {
        SettingsPack sp = new SettingsPack();
        sp.setInteger(settings_pack.int_types.proxy_type.swigValue(), settings_pack.proxy_type_t.socks5.swigValue());
        sp.setString(settings_pack.string_types.proxy_hostname.swigValue(), "127.0.0.1");
        sp.setInteger(settings_pack.int_types.proxy_port.swigValue(), 1);
        try (LoopbackSession s = LoopbackSession.start(true, sp)) {
            // the proxy at port 1 refuses connections: the listen sockets report it
            LoopbackSession.awaitCondition("a socks5 alert", () -> s.hasSeen(Socks5Alert.class));
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void anUnreachableI2pRouterIsReported() {
        SettingsPack sp = new SettingsPack();
        sp.setString(settings_pack.string_types.i2p_hostname.swigValue(), "127.0.0.1");
        sp.setInteger(settings_pack.int_types.i2p_port.swigValue(), 1);
        try (LoopbackSession s = LoopbackSession.start(true, sp)) {
            LoopbackSession.awaitCondition("an i2p alert", () -> s.hasSeen(I2pAlert.class));
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void torrentsAnswerADhtLookupWithTheirPeers() throws IOException {
        createTorrent();
        try (LoopbackSession a = LoopbackSession.startDht(true);
             LoopbackSession b = LoopbackSession.startDht(true)) {
            new SessionHandle(b.sm.swig()).addDhtNode(new Pair<>("127.0.0.1", a.listenPort()));
            LoopbackSession.awaitCondition("the nodes to know each other", () -> {
                b.sm.postDhtStats();
                a.sm.postDhtStats();
                return b.sm.dhtNodes() > 0 && a.sm.dhtNodes() > 0;
            });
            CompletableFuture<Boolean> finished = a.expect(TorrentFinishedAlert.class);
            a.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the seeder");

            CompletableFuture<Integer> reply = b.expect(DhtReplyAlert.class, x -> true, DhtReplyAlert::numPeers);
            b.sm.download(ti, folder.newFolder("leech"), null, null, null, new torrent_flags_t());
            LoopbackSession.awaitCondition("the leecher's torrent", () -> b.sm.find(ti) != null);
            LoopbackSession.awaitCondition("a dht reply", () -> {
                b.sm.find(ti).forceDHTAnnounce();
                return reply.isDone();
            });
            assertTrue(LoopbackSession.get(reply, "the dht reply") >= 0);
            assertEquals(Collections.<String>emptyList(), b.accessorFailures());
        }
    }
}
