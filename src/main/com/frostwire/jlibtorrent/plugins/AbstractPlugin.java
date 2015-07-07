package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.swig.bdecode_node;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractPlugin implements Plugin {

    @Override
    public boolean handleOperation(Operation op) {
        return false;
    }

    @Override
    public TorrentPlugin newTorrent(Torrent t) {
        return null;
    }

    @Override
    public void added() {
    }

    @Override
    public void onAlert(Alert a) {
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
