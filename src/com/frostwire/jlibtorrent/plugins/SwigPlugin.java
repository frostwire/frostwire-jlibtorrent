package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.*;

public final class SwigPlugin extends swig_plugin {

    private final Plugin p;

    public SwigPlugin(Plugin p) {
        this.p = p;
    }

    @Override
    public swig_torrent_plugin new_torrent(torrent_handle th) {
        TorrentPlugin tp = p.newTorrent(new TorrentHandle(th));
        return tp != null ? new SwigTorrentPlugin(tp) : new swig_torrent_plugin();
    }

    @Override
    public void added() {
        p.added();
    }

    @Override
    public boolean on_unknown_torrent(sha1_hash info_hash, peer_connection pc, add_torrent_params p) {
        return this.p.onUnknownTorrent(new Sha1Hash(info_hash), new PeerConnection(pc), new AddTorrentParams(p));
    }

    @Override
    public void on_tick() {
        p.onTick();
    }
}
