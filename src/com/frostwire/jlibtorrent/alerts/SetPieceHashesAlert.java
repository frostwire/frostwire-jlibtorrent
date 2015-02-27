package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.set_piece_hashes_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SetPieceHashesAlert extends AbstractAlert<set_piece_hashes_alert> {

    public SetPieceHashesAlert(set_piece_hashes_alert alert) {
        super(alert);
    }

    public String getId() {
        return alert.getId();
    }

    public int getProgress() {
        return alert.getProgress();
    }

    public int getNumPieces() {
        return alert.getNum_pieces();
    }
}
