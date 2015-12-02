package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.PieceFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.plugins.*;
import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.peer_connection_handle;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

import java.io.File;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PluginsTest {

    public static void main(String[] args) throws Throwable {

        final Session s = new Session();

        Plugin p = new AbstractPlugin() {

            @Override
            public boolean handleOperation(Operation op) {
                return true;
            }

            @Override
            public TorrentPlugin newTorrent(TorrentHandle t) {
                return new AbstractTorrentPlugin() {

                    @Override
                    public boolean handleOperation(Operation op) {
                        return true;
                    }

                    @Override
                    public PeerPlugin newPeerConnection(final PeerConnectionHandle pc) {
                        return new AbstractPeerPlugin() {

                            private peer_connection_handle p = pc.getSwig();

                            @Override
                            public boolean handleOperation(Operation op) {
                                return true;
                            }

                            @Override
                            public boolean onRequest(PeerRequest r) {
                                //System.out.println("peer on request: " + p.remote().address() + " " + r);
                                return false;
                            }

                            @Override
                            public boolean onPiece(PeerRequest piece, DiskBufferHolder data) {
                                //System.out.println("peer on piece: " + p.remote().address() + " " + piece);
                                return false;
                            }

                            @Override
                            public void tick() {
                                System.out.println("PeerPlugin: tick");
                            }
                        };
                    }

                    @Override
                    public void tick() {
                        System.out.println("TorrentPlugin: tick");
                    }

                    @Override
                    public void onPiecePass(int index) {
                        System.out.println("TorrentPlugin: onPiecePass(" + index + ")");
                    }

                    @Override
                    public void onAddPeer(TcpEndpoint endp, int src, TorrentPlugin.Flags flags) {
                        //System.out.println("onAddPeer: " + endp + " " + flags);
                    }
                };
            }

            @Override
            public void registerDhtPlugins(List<Pair<String, DhtPlugin>> plugins) {
                plugins.add(new Pair<String, DhtPlugin>("get_peers", new DhtPlugin() {
                    @Override
                    public boolean onMessage(udp_endpoint source, bdecode_node request, entry response) {
                        System.out.println("DHT get_peers from: " + source.address());
                        return false;
                    }
                }));
            }

            @Override
            public void onAlert(Alert a) {
                //System.out.println(a);
            }

            @Override
            public boolean onUnknownTorrent(Sha1Hash infoHash, PeerConnectionHandle pc, AddTorrentParams p) {
                System.out.println(infoHash);
                return false;
            }

            @Override
            public boolean onOptimisticUnchoke(PeerConnectionHandle[] peers) {
                if (peers.length > 0) {
                    System.out.println("onOptimisticUnchoke");
                }
                return false;
            }
        };

        s.addExtension(p);

        File torrentFile = new File("/Users/aldenml/Downloads/Lisa_Richards_Beating_of_the_Sun_FrostWire_MP3_Nov_09_2015.torrent");
        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void torrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                s.removeTorrent(th);
            }

            @Override
            public void pieceFinished(PieceFinishedAlert alert) {
                //System.out.println("Piece finished");
            }
        });

        th.resume();

        System.out.println("Press ENTER to exit");
        System.in.read();
    }
}
