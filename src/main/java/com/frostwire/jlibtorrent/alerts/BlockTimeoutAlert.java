package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_timeout_alert;

/**
 * This alert is generated when a block request times out.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockTimeoutAlert extends PeerAlert<block_timeout_alert> {

    BlockTimeoutAlert(block_timeout_alert alert) {
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
