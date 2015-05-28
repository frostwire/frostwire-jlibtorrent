package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.swig.swig_torrent_plugin;

public final class SwigTorrentPlugin extends swig_torrent_plugin {

    private final TorrentPlugin p;

    public SwigTorrentPlugin(TorrentPlugin p) {
        this.p = p;
    }

    @Override
    public void tick() {
        p.tick();
    }

    @Override
    public void on_piece_pass(int index) {
        p.onPiecePass(index);
    }
}
