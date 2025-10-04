package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.piece_availability_alert;

public final class PieceAvailabilityAlert extends TorrentAlert<piece_availability_alert> {

    PieceAvailabilityAlert(piece_availability_alert alert) {
        super(alert);
    }

    /**
     * info about pieces being downloaded for the torrent.
     */
    public int[] getPieceAvailability() {
        int_vector v = alert.getPiece_availability();
        return Vectors.int_vector2ints(v);
    }
}