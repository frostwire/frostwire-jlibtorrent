package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_downloading_alert;

/**
 * This alert is generated when a block request is sent to a peer.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockDownloadingAlert extends PeerAlert<block_downloading_alert> {

    BlockDownloadingAlert(block_downloading_alert alert) {
        super(alert);
    }

    /**
     * @return the block index
     */
    public int blockIndex() {
        return alert.getBlock_index();
    }

    /**
     * @return the piece index
     */
    public int pieceIndex() {
        return alert.getPiece_index();
    }
}
