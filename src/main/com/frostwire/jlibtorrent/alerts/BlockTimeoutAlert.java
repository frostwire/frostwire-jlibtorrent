package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_timeout_alert;

/**
 * This alert is generated when a block request times out.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockTimeoutAlert extends PeerAlert<block_timeout_alert> {

    public BlockTimeoutAlert(block_timeout_alert alert) {
        super(alert);
    }

    public int getBlockIndex() {
        return alert.getBlock_index();
    }

    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
