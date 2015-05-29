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
        PeerPlugin pp = p.newPeerConnection(new PeerConnection<peer_connection>(pc));
        return pp != null ? pin(new SwigPeerPlugin(pp)) : super.new_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_bt_peer_connection(bt_peer_connection pc) {
        PeerPlugin pp = p.newPeerConnection(new BtPeerConnection(pc));
        return pp != null ? pin(new SwigPeerPlugin(pp)) : super.new_bt_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_web_peer_connection(web_peer_connection pc) {
        PeerPlugin pp = p.newPeerConnection(new WebPeerConnection(pc));
        return pp != null ? pin(new SwigPeerPlugin(pp)) : super.new_web_peer_connection(pc);
    }

    @Override
    public swig_peer_plugin new_http_seed_connection(http_seed_connection pc) {
        PeerPlugin pp = p.newPeerConnection(new HttpSeedConnection(pc));
        return pp != null ? pin(new SwigPeerPlugin(pp)) : super.new_http_seed_connection(pc);
    }

    @Override
    public void on_piece_pass(int index) {
        p.onPiecePass(index);
    }

    @Override
    public void on_piece_failed(int index) {
        p.onPieceFailed(index);
    }

    @Override
    public void tick() {
        p.tick();
    }

    @Override
    public boolean on_pause() {
        return p.onPause();
    }

    @Override
    public boolean on_resume() {
        return p.onResume();
    }

    @Override
    public void on_files_checked() {
        p.onFilesChecked();
    }

    @Override
    public void on_state(int s) {
        p.onState(s);
    }

    @Override
    public void on_unload() {
        p.onUnload();
    }

    @Override
    public void on_load() {
        p.onLoad();
    }

    @Override
    public void on_add_peer(tcp_endpoint endp, int src, int flags) {
        p.onAddPeer(new TcpEndpoint(endp), src, flags);
    }

    private SwigPeerPlugin pin(SwigPeerPlugin p) {
        mem.add(p);
        return p;
    }
}
