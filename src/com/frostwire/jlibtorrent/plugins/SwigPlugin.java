package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.swig_plugin;
import com.frostwire.jlibtorrent.swig.swig_torrent_plugin;
import com.frostwire.jlibtorrent.swig.torrent_handle;

public final class SwigPlugin extends swig_plugin {

    private final Plugin p;

    public SwigPlugin(Plugin p) {
        this.p = p;
    }


    @Override
    public void on_tick() {
        p.onTick();
    }

//    @Override
//    public swig_torrent_plugin new_torrent2(torrent_handle th) {
//        return new SwigTorrentPlugin(p.newTorrent(new TorrentHandle(th)));
//    }
}
