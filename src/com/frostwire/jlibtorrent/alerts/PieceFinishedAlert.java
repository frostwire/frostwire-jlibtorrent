package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.piece_finished_alert;

/**
 * this alert is posted every time a piece completes downloading
 * and passes the hash check. This alert derives from torrent_alert
 * which contains the torrent_handle to the torrent the piece belongs to.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PieceFinishedAlert extends TorrentAlert<piece_finished_alert> {

    public PieceFinishedAlert(piece_finished_alert alert) {
        super(alert);
    }

    /**
     * the index of the piece that finished.
     *
     * @return
     */
    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
