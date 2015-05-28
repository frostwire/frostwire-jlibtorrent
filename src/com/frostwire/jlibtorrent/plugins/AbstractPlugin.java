package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.swig.bdecode_node;

/**
 * @author gubatron
 * @author aldenml
 */
public class AbstractPlugin implements Plugin {


    @Override
    public TorrentPlugin newTorrent(TorrentHandle th) {
        return null;
    }

    @Override
    public void added() {

    }

    @Override
    public boolean onUnknownTorrent(Sha1Hash infoHash, PeerConnection pc, AddTorrentParams p) {
        return false;
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean onOptimisticUnchoke(TorrentPeer[] peers) {
        return false;
    }

    @Override
    public void saveState(Entry e) {

    }

    @Override
    public void loadState(bdecode_node n) {

    }
}
