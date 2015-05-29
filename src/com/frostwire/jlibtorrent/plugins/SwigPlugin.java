package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigPlugin extends swig_plugin {

    private final Plugin p;

    //private

    public SwigPlugin(Plugin p) {
        this.p = p;
    }

    @Override
    public swig_torrent_plugin new_torrent(torrent t) {
        System.out.println("is_aborted = " + t.is_aborted());
        System.out.println("queue_position = " + t.queue_position());
        //TorrentPlugin tp = p.newTorrent(new TorrentHandle(th));
        //return tp != null ? new SwigTorrentPlugin(tp) : new swig_torrent_plugin();
        return super.new_torrent(t);
    }

    @Override
    public void added() {
        p.added();
    }

    @Override
    public void on_alert(alert a) {
        //System.out.println(a.message());
        //super.on_alert(a);
    }

    @Override
    public boolean on_unknown_torrent(sha1_hash info_hash, peer_connection pc, add_torrent_params p) {
        return this.p.onUnknownTorrent(new Sha1Hash(info_hash), new PeerConnection(pc), new AddTorrentParams(p));
    }

    @Override
    public void on_tick() {
        p.onTick();
    }

    @Override
    public boolean on_optimistic_unchoke(torrent_peer_ptr_vector peers) {
        int size = (int) peers.size();
        TorrentPeer[] arr = new TorrentPeer[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new TorrentPeer(peers.get(i));
        }

        return p.onOptimisticUnchoke(arr);
    }

    @Override
    public void save_state(entry e) {
        p.saveState(new Entry(e));
    }

    @Override
    public void load_state(bdecode_node n) {
        p.loadState(n);
    }
}
