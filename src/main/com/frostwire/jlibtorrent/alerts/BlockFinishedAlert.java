package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_finished_alert;

/**
 * This alert is generated when a block request receives a response.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockFinishedAlert extends PeerAlert<block_finished_alert> {

    public BlockFinishedAlert(block_finished_alert alert) {
        super(alert);
    }

    public int getBlockIndex() {
        return alert.getBlock_index();
    }

    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
