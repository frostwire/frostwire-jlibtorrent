package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.block_uploaded_alert;

/**
 * This alert is posted when a block intended to be sent to a peer is placed in the
 * send buffer. Note that if the connection is closed before the send buffer is sent,
 * the alert may be posted without the bytes having been sent to the peer.
 * It belongs to the ``progress_notification`` category.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockUploadedAlert extends PeerAlert<block_uploaded_alert> {

    BlockUploadedAlert(block_uploaded_alert alert) {
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
