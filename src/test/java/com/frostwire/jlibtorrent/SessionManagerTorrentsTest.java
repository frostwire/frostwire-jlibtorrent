package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link SessionManager} lookups, magnets and advertised endpoints on real sessions.
 *
 * @author gubatron
 * @author aldenml
 */
public class SessionManagerTorrentsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void torrentsAreFoundByEveryHash() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "hybrid", "h.bin", 40_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root)); // hybrid: v1 and v2
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, saveDir);
            LoopbackSession.get(finished, "the torrent");

            assertNotNull(s.sm.find(ti.infoHashV1()));
            assertNotNull(s.sm.find(ti.infoHashV2()));
            assertNotNull(s.sm.find(ti));
            assertEquals(1, s.sm.getTorrentHandles().length);
            assertNull(s.sm.find(Sha1Hash.max()));
            assertNull(s.sm.find(Sha256Hash.max()));
        }
    }

    @Test
    public void aBadMagnetIsRejected() {
        try (LoopbackSession s = LoopbackSession.start()) {
            try {
                s.sm.download("this is not a magnet", folder.getRoot(), new torrent_flags_t());
                fail("bad magnet");
            } catch (IllegalArgumentException expected) {
                assertFalse(expected.getMessage().isEmpty());
            }
        }
    }

    @Test
    public void fetchingAMagnetOfATorrentThatIsAlreadyAddedReturnsItsTorrentFile() throws IOException {
        File saveDir = folder.newFolder("save");
        File root = TestTorrents.content(saveDir, "known", "k.bin", 40_000);
        TorrentInfo ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
        try (LoopbackSession s = LoopbackSession.start()) {
            CompletableFuture<Boolean> finished = s.expect(TorrentFinishedAlert.class);
            s.sm.download(ti, saveDir);
            LoopbackSession.get(finished, "the torrent");

            byte[] data = s.sm.fetchMagnet("magnet:?xt=urn:btih:" + ti.infoHashV1().toHex(), 30, folder.newFolder("tmp"));
            assertNotNull(data);
            assertEquals(ti.infoHashV1(), TorrentInfo.bdecode(data).infoHashV1());
            // it must not remove a torrent it did not add
            assertNotNull(s.sm.find(ti));
        }
    }

    @Test
    public void aMagnetWithNoPeersTimesOutWithNothing() throws IOException {
        try (LoopbackSession s = LoopbackSession.start()) {
            byte[] data = s.sm.fetchMagnet("magnet:?xt=urn:btih:fedcba9876543210fedcba9876543210fedcba98", 1, folder.newFolder("tmp"));
            assertNull(data);
            assertEquals(0, s.sm.getTorrentHandles().length);
        }
    }

    @Test
    public void routableListenAddressesAreAdvertisedInMagnets() throws Exception {
        String ip = firstSiteAddress();
        SettingsPack sp = new SettingsPack();
        sp.setDhtBootstrapNodes("");
        sp.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_lsd.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_upnp.swigValue(), false);
        sp.setBoolean(settings_pack.bool_types.enable_natpmp.swigValue(), false);
        sp.listenInterfaces(ip == null ? "127.0.0.1:0" : ip + ":0");
        SessionManager sm = new SessionManager();
        try (LoopbackSession s = LoopbackSession.startWith(sm, new SessionParams(sp))) {
            if (ip == null) {
                // no address but the loopback one on this machine: nothing to advertise
                LoopbackSession.awaitCondition("the listen alert", () -> s.listenPortOrMinusOne() > 0);
                assertEquals("", sm.magnetPeers());
                assertTrue(sm.listenEndpoints().isEmpty());
            } else {
                LoopbackSession.awaitCondition("the listen endpoints", () -> !sm.listenEndpoints().isEmpty());
                assertTrue(sm.listenEndpoints().get(0), sm.listenEndpoints().get(0).startsWith(ip + ":"));
                assertTrue(sm.magnetPeers(), sm.magnetPeers().contains("&x.pe=" + ip + ":"));
            }
            assertEquals(Collections.<String>emptyList(), s.accessorFailures());
        }
    }

    /** A site-local IPv4 address of this machine, if it has one besides the loopback address. */
    private static String firstSiteAddress() throws Exception {
        Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
        while (nics != null && nics.hasMoreElements()) {
            NetworkInterface nic = nics.nextElement();
            if (!nic.isUp() || nic.isLoopback()) {
                continue;
            }
            Enumeration<InetAddress> addresses = nic.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress a = addresses.nextElement();
                if (a instanceof Inet4Address && !a.isLinkLocalAddress() && !a.isLoopbackAddress()) {
                    return a.getHostAddress();
                }
            }
        }
        return null;
    }
}
