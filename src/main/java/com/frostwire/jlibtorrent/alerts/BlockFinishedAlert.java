package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_finished_alert;

/**
 * This alert is generated when a block request receives a response.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockFinishedAlert extends PeerAlert<block_finished_alert> {

    BlockFinishedAlert(block_finished_alert alert) {
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
