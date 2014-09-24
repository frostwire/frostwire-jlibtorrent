package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.read_piece_alert;

/**
 * This alert is posted when the asynchronous read operation initiated by
 * a call to torrent_handle::read_piece() is completed. If the read failed, the torrent
 * is paused and an error state is set and the buffer member of the alert
 * is 0. If successful, ``buffer`` points to a buffer containing all the data
 * of the piece. ``piece`` is the piece index that was read. ``size`` is the
 * number of bytes that was read.
 * <p/>
 * If the operation fails, ec will indicat what went wrong.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ReadPieceAlert extends TorrentAlert<read_piece_alert> {

    public ReadPieceAlert(read_piece_alert alert) {
        super(alert);
    }

    public int getPiece() {
        return alert.getPiece();
    }

    public int getSize() {
        return alert.getSize();
    }
}
