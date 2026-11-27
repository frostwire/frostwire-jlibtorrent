package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.FileProgressAlert;
import com.frostwire.jlibtorrent.alerts.FileRenameFailedAlert;
import com.frostwire.jlibtorrent.alerts.ListenFailedAlert;
import com.frostwire.jlibtorrent.alerts.PeerBlockedAlert;
import com.frostwire.jlibtorrent.alerts.PeerInfoAlert;
import com.frostwire.jlibtorrent.alerts.PieceAvailabilityAlert;
import com.frostwire.jlibtorrent.alerts.PieceInfoAlert;
import com.frostwire.jlibtorrent.alerts.StorageMovedFailedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TrackerListAlert;
import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.ip_filter;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Alerts that report errors, or that a torrent posts on request, produced with real events on
 * loopback sessions.
 *
 * @author gubatron
 * @author aldenml
 */
public class ErrorAndPostedAlertsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File saveDir;
    private File root;
    private TorrentInfo ti;

    private LoopbackSession seeder() throws IOException {
        saveDir = folder.newFolder("save");
        root = TestTorrents.content(saveDir, "alerts", "one.bin", 70_000, "two.bin", 30_000);
        ti = TestTorrents.torrentInfo(new TorrentBuilder().path(root).pieceSize(65536).flags(TorrentBuilder.V1_ONLY));
        LoopbackSession s = LoopbackSession.start(true, new SettingsPack());
        CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
        s.sm.download(ti, saveDir);
        LoopbackSession.get(finished, "the torrent to be checked");
        return s;
    }

    @Test
    public void torrentsPostTheirStateOnRequest() throws IOException {
        try (LoopbackSession s = seeder()) {
            TorrentHandle th = s.sm.find(ti);

            CompletableFuture<Integer> peers = s.expect(PeerInfoAlert.class, a -> true, a -> a.getPeerInfo().size());
            th.swig().post_peer_info();
            assertEquals(0, (int) LoopbackSession.get(peers, "the peer info"));

            CompletableFuture<Integer> queue = s.expect(PieceInfoAlert.class, a -> true,
                    a -> a.getPieceInfo().size() + a.getBlockData().size());
            th.swig().post_download_queue();
            assertEquals(0, (int) LoopbackSession.get(queue, "the download queue"));

            th.addTracker(new AnnounceEntry("udp://127.0.0.1:9/announce"));
            CompletableFuture<Integer> trackers = s.expect(TrackerListAlert.class, a -> true, a -> a.getTrackers().size());
            th.swig().post_trackers();
            assertEquals(1, (int) LoopbackSession.get(trackers, "the tracker list"));

            CompletableFuture<Integer> availability = s.expect(PieceAvailabilityAlert.class, a -> true,
                    a -> a.getPieceAvailability().length);
            th.swig().post_piece_availability();
            LoopbackSession.get(availability, "the piece availability");

            CompletableFuture<long[]> progress = s.expect(FileProgressAlert.class, a -> true, FileProgressAlert::getFiles);
            th.swig().post_file_progress(TorrentHandle.PIECE_GRANULARITY);
            long[] files = LoopbackSession.get(progress, "the file progress");
            assertEquals(2, files.length);
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void failedRenamesAndMovesAreReported() throws IOException {
        try (LoopbackSession s = seeder()) {
            TorrentHandle th = s.sm.find(ti);

            // file 1 can't take the name of file 0
            CompletableFuture<Integer> renameFailed = s.expect(FileRenameFailedAlert.class, a -> true, a -> a.getError().value());
            th.renameFile(1, ti.files().filePath(0));
            assertTrue(LoopbackSession.get(renameFailed, "the failed rename") != 0);

            // the destination is a regular file, not a directory
            File notADir = folder.newFile("not-a-dir");
            CompletableFuture<Integer> moveFailed = s.expect(StorageMovedFailedAlert.class, a -> true, a -> a.error().value());
            th.moveStorage(new File(notADir, "inside").getAbsolutePath());
            assertTrue(LoopbackSession.get(moveFailed, "the failed move") != 0);
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    @Test
    public void anAddressInTheIpFilterIsBlocked() throws IOException {
        try (LoopbackSession seeder = seeder();
             LoopbackSession leecher = LoopbackSession.start()) {
            ip_filter filter = new ip_filter();
            address loopback = address.from_string("127.0.0.1", new com.frostwire.jlibtorrent.swig.error_code());
            filter.add_rule(loopback, loopback, 1); // 1: blocked
            seeder.sm.swig().set_ip_filter(filter);

            CompletableFuture<PeerBlockedAlert.Reason> blocked = seeder.expect(PeerBlockedAlert.class, a -> true, PeerBlockedAlert::reason);
            leecher.sm.download(ti, folder.newFolder("leech"), null, null, Collections.singletonList(seeder.endpoint()), new torrent_flags_t());
            assertEquals(PeerBlockedAlert.Reason.IP_FILTER, LoopbackSession.get(blocked, "the blocked peer"));
            assertEquals(Collections.<String>emptyList(), seeder.accessorFailures());
        }
    }

    @Test
    public void anAddressThatIsNotOurFailsToListen() {
        try (LoopbackSession s = LoopbackSession.start()) {
            // 192.0.2.0/24 is reserved for documentation: no interface of this machine has it
            CompletableFuture<ListenFailedAlert> failed = s.expect(ListenFailedAlert.class, a -> true, a -> a);
            CompletableFuture<String> described = s.expect(ListenFailedAlert.class, a -> true,
                    a -> a.listenInterface() + "|" + a.port() + "|" + a.socketType() + "|" + a.operation() + "|" + a.address());
            s.sm.listenInterfaces("192.0.2.123:6881");
            LoopbackSession.get(failed, "the listen failure");
            String d = LoopbackSession.get(described, "the description");
            // the interface is the device name, empty when the interface was given as an address
            assertEquals("|6881|TCP|SOCK_BIND|192.0.2.123", d);
            assertTrue(s.hasSeen(ListenFailedAlert.class));
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }
}
