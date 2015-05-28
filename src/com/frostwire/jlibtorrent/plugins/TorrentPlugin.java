package com.frostwire.jlibtorrent.plugins;

public interface TorrentPlugin {

    void tick();

    void onPiecePass(int index);
}
