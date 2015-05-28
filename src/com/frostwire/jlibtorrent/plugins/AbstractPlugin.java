package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TorrentHandle;

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
}
