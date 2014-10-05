package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_downloading_alert;

/**
 * This alert is generated when a block request is sent to a peer.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockDownloadingAlert extends PeerAlert<block_downloading_alert> {

    public BlockDownloadingAlert(block_downloading_alert alert) {
        super(alert);
    }

    public String getPeerSpeedmsg() {
        return alert.getPeer_speedmsg();
    }

    public int getBlockIndex() {
        return alert.getBlock_index();
    }

    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
