package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.PeerConnection;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.TorrentStatus;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractTorrentPlugin implements TorrentPlugin {

    @Override
    public boolean handleOperation(Operation op) {
        return false;
    }

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
    public void onState(TorrentStatus.State s) {

    }

    @Override
    public void onUnload() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onAddPeer(TcpEndpoint endp, int src, TorrentPlugin.Flags flags) {

    }
}
