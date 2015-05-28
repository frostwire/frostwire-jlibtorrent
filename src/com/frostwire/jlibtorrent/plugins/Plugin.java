package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.TorrentHandle;

public interface Plugin {

    TorrentPlugin newTorrent(TorrentHandle th);

    void onTick();
}
