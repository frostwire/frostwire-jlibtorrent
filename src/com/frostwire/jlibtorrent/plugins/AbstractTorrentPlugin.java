package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.TcpEndpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractTorrentPlugin implements TorrentPlugin {

    @Override
    public PeerPlugin newPeerConnection(PeerConnection pc) {
        return null;
    }

    @Override
    public void onPiecePass(int index) {

    }

    @Override
    public void onPieceFailed(int index) {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean onPause() {
        return false;
    }

    @Override
    public boolean onResume() {
        return false;
    }

    @Override
    public void onFilesChecked() {

    }

    @Override
    public void onState(int s) {

    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onAddPeer(TcpEndpoint endp, int src, int flags) {

    }
}
