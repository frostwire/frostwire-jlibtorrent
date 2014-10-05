package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.unwanted_block_alert;

/**
 * This alert is generated when a block is received that was not requested or
 * whose request timed out.
 *
 * @author gubatron
 * @author aldenml
 */
public final class UnwantedBlockAlert extends PeerAlert<unwanted_block_alert> {

    public UnwantedBlockAlert(unwanted_block_alert alert) {
        super(alert);
    }

    public int getBlockIndex() {
        return alert.getBlock_index();
    }

    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
