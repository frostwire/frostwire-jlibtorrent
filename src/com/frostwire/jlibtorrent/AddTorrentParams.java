package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.add_torrent_params;

/**
 * @author gubatron
 * @author aldenml
 */
public final class AddTorrentParams {

    private final add_torrent_params p;

    public AddTorrentParams(add_torrent_params p) {
        this.p = p;
    }

    public add_torrent_params getSwig() {
        return p;
    }
}
