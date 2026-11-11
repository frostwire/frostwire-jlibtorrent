package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.Alerts;
import com.frostwire.jlibtorrent.alerts.FilePrioritiesAlert;
import com.frostwire.jlibtorrent.alerts.FileStatusAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.create_flags_t;
import com.frostwire.jlibtorrent.swig.file_priorities_alert;
import com.frostwire.jlibtorrent.swig.file_status_alert;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.ip_ban_alert;
import com.frostwire.jlibtorrent.swig.string_vector;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * libtorrent 2.1 keeps only the info dictionary in torrent_info. The rest of a .torrent file
 * (trackers, web seeds, DHT nodes, comment, creator, creation date, v2 piece layers) comes back
 * from load_torrent_buffer() in an add_torrent_params, which TorrentInfo keeps. These check that
 * none of it is lost on the way into a session or back out to a .torrent file, and that the
 * alerts libtorrent 2.1 added reach Java.
 */
public class TorrentFileMetadataTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String TRACKER_A = "udp://tracker-a.example.com:6969/announce";
    private static final String TRACKER_B = "http://tracker-b.example.com/announce";
    private static final String WEB_SEED = "http://webseed.example.com/files/";

    private File content(File data, int sizeA, int sizeB) throws Exception {
        File folder = new File(data, "folder");
        assertTrue(folder.mkdirs());
        Random random = new Random(7);
        byte[] a = new byte[sizeA];
        byte[] b = new byte[sizeB];
        random.nextBytes(a);
        random.nextBytes(b);
        Files.write(new File(folder, "a.bin").toPath(), a);
        Files.write(new File(folder, "b.bin").toPath(), b);
        return folder;
    }

    private byte[] torrent(File folder, create_flags_t flags) throws Exception {
        TorrentBuilder b = new TorrentBuilder().path(folder).pieceSize(16384)
                .addTracker(TRACKER_A, 0)
                .addTracker(TRACKER_B, 1)
                .addUrlSeed(WEB_SEED)
                .addNode(new Pair<>("dht.example.com", 6881))
                .comment("the comment")
                .creator("the creator");
        if (flags != null) {
            b.flags(flags);
        }
        return b.generate().entry().bencode();
    }

    private static List<String> urls(List<AnnounceEntry> trackers) {
        List<String> l = new ArrayList<>();
        for (AnnounceEntry e : trackers) {
            l.add(e.url());
        }
        return l;
    }

    @Test
    public void loadingATorrentKeepsTheMetadataOutsideTheInfoDictionary() throws Exception {
        TorrentInfo ti = new TorrentInfo(torrent(content(tmp.newFolder("data"), 40000, 5000), null));

        ArrayList<AnnounceEntry> trackers = ti.trackers();
        assertEquals(2, trackers.size());
        assertEquals(TRACKER_A, trackers.get(0).url());
        assertEquals(0, trackers.get(0).tier());
        assertEquals(TRACKER_B, trackers.get(1).url());
        assertEquals(1, trackers.get(1).tier());
        assertEquals(1, ti.webSeeds().size());
        assertEquals(WEB_SEED, ti.webSeeds().get(0).url());
        assertEquals("dht.example.com", ti.nodes().get(0).first);
        assertEquals(6881, (int) ti.nodes().get(0).second);
        assertEquals("the comment", ti.comment());
        assertEquals("the creator", ti.creator());
        assertTrue("TorrentBuilder stamps a creation date", ti.creationDate() > 0);

        // the torrent_info itself has none of it: a TorrentInfo made from it reports nothing
        TorrentInfo bare = new TorrentInfo(ti.swig());
        assertEquals(0, bare.trackers().size());
        assertEquals(0, bare.webSeeds().size());
        assertEquals("", bare.comment());
    }

    @Test
    public void aMagnetMadeFromATorrentInfoCarriesItsName() throws Exception {
        TorrentInfo ti = new TorrentInfo(torrent(content(tmp.newFolder("data"), 40000, 5000), null));

        String magnet = ti.makeMagnetUri();

        assertTrue(magnet, magnet.startsWith("magnet:?xt=urn:btih:" + ti.infoHashV1().toHex()));
        assertTrue("the link names the torrent: " + magnet, magnet.contains("&dn=folder"));
    }

    @Test
    public void bencodeWritesTheWholeTorrentBack() throws Exception {
        byte[] original = torrent(content(tmp.newFolder("data"), 40000, 5000), null);
        TorrentInfo ti = new TorrentInfo(original);
        ti.addTracker("udp://added.example.com:1337/announce", 2);

        TorrentInfo reopened = new TorrentInfo(ti.bencode());
        assertEquals(ti.infoHashV1(), reopened.infoHashV1());
        assertEquals(ti.infoHashV2(), reopened.infoHashV2());
        assertEquals(urls(ti.trackers()), urls(reopened.trackers()));
        assertEquals(2, reopened.trackers().get(2).tier());
        assertEquals(WEB_SEED, reopened.webSeeds().get(0).url());
        assertEquals("dht.example.com", reopened.nodes().get(0).first);
        assertEquals("the comment", reopened.comment());
        assertEquals("the creator", reopened.creator());
        assertEquals(ti.creationDate(), reopened.creationDate());
    }

    @Test
    public void aV2TorrentFileKeepsItsPieceLayers() throws Exception {
        // a.bin spans 3 pieces, so the .torrent needs its piece layer; libtorrent 2.0 kept it in
        // torrent_info, 2.1 in add_torrent_params::merkle_trees
        byte[] original = torrent(content(tmp.newFolder("data"), 40000, 5000),
                TorrentBuilder.V2_ONLY);
        TorrentInfo ti = new TorrentInfo(original);
        TorrentInfo reopened = new TorrentInfo(ti.bencode());
        assertEquals(ti.infoHashV2(), reopened.infoHashV2());
        // the same info dictionary and piece layers (the rest may be written differently: 2.1
        // writes a single web seed as a list, create_torrent as a string)
        Entry before = Entry.bdecode(original);
        Entry after = Entry.bdecode(reopened.bencode());
        for (String key : new String[]{"info", "piece layers", "announce", "announce-list"}) {
            assertArrayEquals(key, before.dictionary().get(key).bencode(),
                    after.dictionary().get(key).bencode());
        }
    }

    @Test
    public void applyToMergesTrackersAndWebSeedsLikeLibtorrent20() throws Exception {
        TorrentInfo ti = new TorrentInfo(torrent(content(tmp.newFolder("data"), 40000, 5000), null));
        add_torrent_params p = add_torrent_params.create_instance();
        string_vector extra = new string_vector();
        extra.add("udp://extra.example.com:80/announce");
        extra.add(TRACKER_B); // already in the .torrent: not added twice
        p.set_trackers(extra);
        int_vector tiers = new int_vector();
        tiers.add(5);
        tiers.add(9);
        p.set_tracker_tiers(tiers);

        ti.applyTo(p);

        string_vector t = p.get_trackers();
        int_vector ts = p.get_tracker_tiers();
        assertEquals(3, t.size());
        assertEquals(TRACKER_A, t.get(0));
        assertEquals(TRACKER_B, t.get(1));
        assertEquals("udp://extra.example.com:80/announce", t.get(2));
        assertEquals(0, (int) ts.get(0));
        assertEquals(1, (int) ts.get(1));
        assertEquals(5, (int) ts.get(2));
        assertEquals(WEB_SEED, p.get_url_seeds().get(0));
        assertEquals("the comment", p.getComment());
        assertEquals(ti.infoHashV1(), new TorrentInfo(p.ti_ptr()).infoHashV1());
    }

    @Test
    public void aDownloadGetsTheTrackersAndWebSeedsOfItsTorrentFile() throws Exception {
        File data = tmp.newFolder("data");
        TorrentInfo ti = new TorrentInfo(torrent(content(data, 40000, 5000), null));

        SettingsPack settings = new SettingsPack();
        settings.setEnableDht(false);
        settings.setEnableLsd(false);
        settings.listenInterfaces("127.0.0.1:0");
        SessionManager session = new SessionManager(false);
        session.start(new SessionParams(settings));
        try {
            session.download(ti, data);
            TorrentHandle handle = null;
            for (int i = 0; i < 100 && (handle == null || !handle.isValid()); i++) {
                Thread.sleep(50);
                handle = session.find(ti);
            }
            assertNotNull(handle);

            List<String> trackers = urls(handle.trackers());
            assertTrue(trackers.toString(), trackers.contains(TRACKER_A));
            assertTrue(trackers.toString(), trackers.contains(TRACKER_B));
            assertTrue(handle.urlSeeds().toString(), handle.urlSeeds().contains(WEB_SEED));

            String magnet = handle.makeMagnetUri();
            assertTrue(magnet, magnet.startsWith("magnet:?xt=urn:btih:" + ti.infoHashV1().toHex()));
            assertTrue(magnet, magnet.contains("&tr="));
            assertTrue(magnet, magnet.contains("&ws="));

            // the session's torrent_info is the info dictionary only, but torrentFileWithHashes()
            // rebuilds the trackers and web seeds from the handle
            TorrentInfo saved = handle.torrentFileWithHashes();
            assertNotNull(saved);
            assertTrue(urls(saved.trackers()).contains(TRACKER_A));
            assertEquals(WEB_SEED, saved.webSeeds().get(0).url());
        } finally {
            session.stop();
        }
    }

    @Test
    public void theAlertsLibtorrent21AddedAreNumberedAndDispatched() throws Exception {
        assertEquals(108, Alerts.NUM_ALERT_TYPES);
        assertEquals(105, file_priorities_alert.alert_type);
        assertEquals(106, file_status_alert.alert_type);
        assertEquals(107, ip_ban_alert.alert_type);
        for (AlertType t : AlertType.values()) {
            if (t != AlertType.UNKNOWN) {
                assertEquals(t.name(), t, AlertType.fromSwig(t.swig()));
            }
        }

        File data = tmp.newFolder("data");
        TorrentInfo ti = new TorrentInfo(torrent(content(data, 40000, 5000), null));
        // an alert is only valid until the next pop_alerts: copy what it says in the listener
        AtomicReference<Priority[]> priorities = new AtomicReference<>();
        AtomicReference<int[]> openFiles = new AtomicReference<>();
        CountDownLatch both = new CountDownLatch(2);

        SettingsPack settings = new SettingsPack();
        settings.setEnableDht(false);
        settings.setEnableLsd(false);
        settings.listenInterfaces("127.0.0.1:0");
        SessionManager session = new SessionManager(false);
        session.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return new int[]{AlertType.FILE_PRIORITIES.swig(), AlertType.FILE_STATUS.swig()};
            }

            @Override
            public void alert(Alert<?> alert) {
                if (alert instanceof FilePrioritiesAlert) {
                    priorities.set(((FilePrioritiesAlert) alert).priorities());
                    both.countDown();
                } else if (alert instanceof FileStatusAlert) {
                    openFiles.set(((FileStatusAlert) alert).openFileIndices());
                    both.countDown();
                }
            }
        });
        session.start(new SessionParams(settings));
        try {
            Priority[] wanted = new Priority[ti.numFiles()];
            for (int i = 0; i < wanted.length; i++) {
                wanted[i] = i == 0 ? Priority.SEVEN : Priority.IGNORE;
            }
            session.download(ti, data);
            TorrentHandle handle = null;
            for (int i = 0; i < 100 && (handle == null || !handle.isValid()); i++) {
                Thread.sleep(50);
                handle = session.find(ti);
            }
            assertNotNull(handle);
            handle.prioritizeFiles(wanted);
            // file priorities change through the disk thread: wait until the torrent has them
            for (int i = 0; i < 100 && handle.filePriorities()[0] != Priority.SEVEN; i++) {
                Thread.sleep(50);
            }
            handle.postFilePriorities();
            handle.postFileStatus();
            assertTrue("both alerts arrive", both.await(10, TimeUnit.SECONDS));

            Priority[] got = priorities.get();
            assertEquals(ti.numFiles(), got.length);
            for (int i = 0; i < got.length; i++) {
                assertEquals("file " + i, wanted[i], got[i]);
            }
            for (int index : openFiles.get()) {
                assertTrue(index >= 0 && index < ti.numFiles());
            }
        } finally {
            session.stop();
        }
    }
}
