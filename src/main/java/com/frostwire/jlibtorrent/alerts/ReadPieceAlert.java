package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.swig.read_piece_alert;

/**
 * This alert is posted when the asynchronous read operation initiated by
 * a call to {@link com.frostwire.jlibtorrent.TorrentHandle#readPiece(int)}
 * is completed. If the read failed, the torrent
 * is paused and an error state is set and the buffer member of the alert
 * is 0. If successful, {@link #bufferPtr()} points to a buffer containing all the data
 * of the piece. {@link #piece()} is the piece index that was read. {@link #size()}
 * is the number of bytes that was read.
 * <p/>
 * If the operation fails, {@link #ec()} will indicate what went wrong.
 *
 * @author gubatron
 * @author aldenml
 */
public final class ReadPieceAlert extends TorrentAlert<read_piece_alert> {

    public ReadPieceAlert(read_piece_alert alert) {
        super(alert);
    }

    public ErrorCode ec() {
        return new ErrorCode(alert.getEc());
    }

    public long bufferPtr() {
        return alert.buffer_ptr();
    }

    public int piece() {
        return alert.getPiece();
    }

    public int size() {
        return alert.getSize();
    }
}
