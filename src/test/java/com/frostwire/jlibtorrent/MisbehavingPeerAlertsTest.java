package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.BlockTimeoutAlert;
import com.frostwire.jlibtorrent.alerts.HashFailedAlert;
import com.frostwire.jlibtorrent.alerts.InvalidRequestAlert;
import com.frostwire.jlibtorrent.alerts.PeerBanAlert;
import com.frostwire.jlibtorrent.alerts.PeerErrorAlert;
import com.frostwire.jlibtorrent.alerts.PeerSnubbedAlert;
import com.frostwire.jlibtorrent.alerts.PeerUnsnubbedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.alerts.UnwantedBlockAlert;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.torrent_flags_t;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Alerts about peers that break the protocol, produced by a hand-written peer ({@link RawPeer})
 * talking to real sessions on loopback.
 *
 * @author gubatron
 * @author aldenml
 */
public class MisbehavingPeerAlertsTest {

    private static final int PIECE = TestTorrents.PIECE_SIZE;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File seedDir;
    private File root;
    private TorrentInfo ti;

    private void createTorrent() throws IOException {
        seedDir = folder.newFolder("seed");
        root = TestTorrents.content(seedDir, "bad", "b.bin", 3 * PIECE);
        ti = TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
    }

    private byte[] infoHash() {
        return Hex.decode(ti.infoHashV1().toHex());
    }

    @Test
    public void aPeerThatAsksForWhatDoesNotExistIsReportedAndDropped() throws Exception {
        createTorrent();
        try (LoopbackSession seeder = LoopbackSession.start(true, new SettingsPack())) {
            CompletableFuture<Boolean> finished = seeder.expect(TorrentFinishedAlert.class);
            seeder.sm.download(ti, seedDir);
            LoopbackSession.get(finished, "the seeder");

            CompletableFuture<Boolean> invalid = seeder.expect(InvalidRequestAlert.class);
            CompletableFuture<Boolean> error = seeder.expect(PeerErrorAlert.class);
            try (RawPeer peer = RawPeer.connect(seeder.listenPort())) {
                byte[] remote = peer.handshake(infoHash());
                assertTrue(Arrays.equals(infoHash(), remote));
                peer.send(RawPeer.INTERESTED, new byte[0]);
                assertNotNull("the seeder unchokes an interested peer", peer.readUntil(RawPeer.UNCHOKE));

                peer.sendRequest(9999, 0, 16384); // no such piece
                LoopbackSession.get(invalid, "the invalid request");

                peer.sendRawLength(0x7fffff00); // a message no client would send
                LoopbackSession.get(error, "the protocol error");
            }
            assertEquals(Collections.<String>emptyList(), seeder.accessorFailures());
        }
    }

    @Test
    public void aPeerThatSendsGarbageIsReportedAndBanned() throws Exception {
        createTorrent();
        try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"));
             LoopbackSession leecher = LoopbackSession.start(true, new SettingsPack())) {
            CompletableFuture<Boolean> unwanted = leecher.expect(UnwantedBlockAlert.class);
            CompletableFuture<Boolean> hashFailed = leecher.expect(HashFailedAlert.class);
            CompletableFuture<Boolean> banned = leecher.expect(PeerBanAlert.class);
            leecher.sm.download(ti, folder.newFolder("leech"), null, null,
                    Collections.singletonList(new TcpEndpoint("127.0.0.1", server.getLocalPort())), new torrent_flags_t());

            server.setSoTimeout(30_000);
            try (RawPeer peer = new RawPeer(server.accept())) {
                peer.readHandshake();
                peer.sendHandshake(infoHash());
                // the leecher may close the connection as soon as it sees the unwanted block, which
                // is the answer the test is after: every write may hit a closed socket
                try {
                    // a block nobody asked for, sent before the leecher has any reason to ask
                    peer.sendPiece(0, 0, new byte[PIECE]);
                    peer.send(RawPeer.BITFIELD, new byte[]{(byte) 0xe0}); // all three pieces
                    peer.send(RawPeer.UNCHOKE, new byte[0]);

                    // then answer every request with zeros, which never match the piece hashes
                    RawPeer.Message m;
                    while (!banned.isDone() && (m = peer.read()) != null) {
                        if (m.id == RawPeer.REQUEST) {
                            peer.sendPiece(m.int32(0), m.int32(4), new byte[m.int32(8)]);
                        }
                    }
                } catch (IOException banned_) {
                    // the leecher closes the connection once it bans us
                }
            }
            LoopbackSession.get(unwanted, "the unwanted block");
            LoopbackSession.get(hashFailed, "the failed piece");
            LoopbackSession.get(banned, "the ban");
            assertEquals(Collections.<String>emptyList(), leecher.accessorFailures());
        }
    }

    @Test
    public void aPeerThatNeverAnswersIsSnubbedAndItsRequestsTimeOut() throws Exception {
        createTorrent();
        SettingsPack sp = new SettingsPack();
        sp.setInteger(settings_pack.int_types.request_timeout.swigValue(), 2);
        sp.setInteger(settings_pack.int_types.peer_timeout.swigValue(), 120);
        try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"));
             LoopbackSession leecher = LoopbackSession.start(true, sp)) {
            CompletableFuture<Boolean> timeout = leecher.expect(BlockTimeoutAlert.class);
            CompletableFuture<Boolean> snubbed = leecher.expect(PeerSnubbedAlert.class);
            CompletableFuture<Boolean> unsnubbed = leecher.expect(PeerUnsnubbedAlert.class);
            leecher.sm.download(ti, folder.newFolder("leech"), null, null,
                    Collections.singletonList(new TcpEndpoint("127.0.0.1", server.getLocalPort())), new torrent_flags_t());

            server.setSoTimeout(30_000);
            try (RawPeer peer = new RawPeer(server.accept())) {
                peer.readHandshake();
                peer.sendHandshake(infoHash());
                peer.send(RawPeer.BITFIELD, new byte[]{(byte) 0xe0});
                peer.send(RawPeer.UNCHOKE, new byte[0]);

                // stay silent until the leecher gives up on the requests...
                RawPeer.Message first = peer.readUntil(RawPeer.REQUEST);
                assertNotNull("the leecher asks for blocks", first);
                LoopbackSession.get(snubbed, "the snub");
                LoopbackSession.get(timeout, "the block timeout");

                // ...then deliver valid blocks: a peer that delivers is no longer snubbed
                byte[] content = Utils.readFileToByteArray(new File(root, "b.bin"));
                RawPeer.Message request = first;
                try {
                    while (!unsnubbed.isDone() && request != null) {
                        int piece = request.int32(0);
                        int begin = request.int32(4);
                        int length = request.int32(8);
                        peer.sendPiece(piece, begin, Arrays.copyOfRange(content,
                                piece * PIECE + begin, piece * PIECE + begin + length));
                        request = peer.readUntil(RawPeer.REQUEST);
                    }
                } catch (IOException closed) {
                    // the leecher may close the connection when it has what it needs
                }
                LoopbackSession.get(unsnubbed, "the unsnub");
            }
            assertEquals(Collections.<String>emptyList(), leecher.accessorFailures());
        }
    }
}
