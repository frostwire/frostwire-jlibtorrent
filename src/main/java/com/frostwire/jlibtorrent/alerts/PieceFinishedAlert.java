package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.piece_finished_alert;

/**
 * This alert is posted every time a piece completes downloading
 * and passes the hash check. This alert derives from {@link com.frostwire.jlibtorrent.alerts.TorrentAlert}
 * which contains the {@link com.frostwire.jlibtorrent.TorrentHandle} to the torrent the piece belongs to.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PieceFinishedAlert extends TorrentAlert<piece_finished_alert> {

    public PieceFinishedAlert(piece_finished_alert alert) {
        super(alert);
    }

    /**
     * The index of the piece that finished.
     *
     * @return
     */
    public int getPieceIndex() {
        return alert.getPiece_index();
    }
}
