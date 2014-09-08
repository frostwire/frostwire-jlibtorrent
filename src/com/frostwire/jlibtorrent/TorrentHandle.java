package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_handle;

public final class TorrentHandle {

    private final torrent_handle th;

    private final TorrentInfo ti;

    public TorrentHandle(torrent_handle th) {
        this.th = th;

        this.ti = new TorrentInfo(this.th.torrent_file());
    }

    public TorrentInfo getTorrentInfo() {
        return this.ti;
    }
}
