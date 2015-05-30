package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigTorrentPlugin extends swig_torrent_plugin {

    private static final Logger LOG = Logger.getLogger(SwigTorrentPlugin.class);

    private final TorrentPlugin p;
    final torrent t;

    private final List<SwigPeerPlugin> mem;

    public SwigTorrentPlugin(TorrentPlugin p, torrent t) {
        this.p = p;
        this.t = t;

        this.mem = new LinkedList<SwigPeerPlugin>();
    }

    @Override
    public swig_peer_plugin new_peer_connection(peer_connection pc) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.NEW_PEER_CONNECTION)) {
                PeerPlugin pp = p.newPeerConnection(new PeerConnection<peer_connection>(pc));

                if (pp != null) {
                    return pin(new SwigPeerPlugin(pp, pc));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_peer_connection(peer_connection))", e);
        }

        return super.new_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_bt_peer_connection(bt_peer_connection pc) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.NEW_PEER_CONNECTION)) {
                PeerPlugin pp = p.newPeerConnection(new BtPeerConnection(pc));

                if (pp != null) {
                    return pin(new SwigPeerPlugin(pp, pc));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_peer_connection(bt_peer_connection))", e);
        }

        return super.new_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_web_peer_connection(web_peer_connection pc) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.NEW_PEER_CONNECTION)) {
                PeerPlugin pp = p.newPeerConnection(new WebPeerConnection(pc));

                if (pp != null) {
                    return pin(new SwigPeerPlugin(pp, pc));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_peer_connection(web_peer_connection))", e);
        }

        return super.new_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_http_seed_connection(http_seed_connection pc) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.NEW_PEER_CONNECTION)) {
                PeerPlugin pp = p.newPeerConnection(new HttpSeedConnection(pc));

                if (pp != null) {
                    return pin(new SwigPeerPlugin(pp, pc));
                }
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (new_peer_connection(http_seed_connection))", e);
        }

        return super.new_peer_connection(pc);
    }

    @Override
    public void on_piece_pass(int index) {
        try {
            p.onPiecePass(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece_pass)", e);
        }
    }

    @Override
    public void on_piece_failed(int index) {
        try {
            p.onPieceFailed(index);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece_failed)", e);
        }
    }

    @Override
    public void tick() {
        try {
            p.tick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (tick)", e);
        }
    }

    @Override
    public boolean on_pause() {
        try {
            return p.onPause();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_pause)", e);
        }

        return false;
    }

    @Override
    public boolean on_resume() {
        try {
            return p.onResume();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_resume)", e);
        }

        return false;
    }

    @Override
    public void on_files_checked() {
        try {
            p.onFilesChecked();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_files_checked)", e);
        }
    }

    @Override
    public void on_state(int s) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.ON_STATE)) {
                p.onState(TorrentStatus.State.fromSwig(s));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_state)", e);
        }
    }

    @Override
    public void on_unload() {
        try {
            p.onUnload();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_unload)", e);
        }
    }

    @Override
    public void on_load() {
        try {
            p.onLoad();
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_load)", e);
        }
    }

    @Override
    public void on_add_peer(tcp_endpoint endp, int src, int flags) {
        try {
            if (p.handleOperation(TorrentPlugin.Operation.ON_ADD_PEER)) {
                p.onAddPeer(new TcpEndpoint(endp), src, TorrentPlugin.Flags.fromSwig(flags));
            }
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_add_peer)", e);
        }
    }

    private SwigPeerPlugin pin(SwigPeerPlugin p) {
        mem.add(p);
        return p;
    }
}
