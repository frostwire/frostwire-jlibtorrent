/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */


package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.BlockInfo;
import com.frostwire.jlibtorrent.PartialPieceInfo;
import com.frostwire.jlibtorrent.swig.*;

import java.util.ArrayList;


/**
 * posted when torrent_handle::post_download_queue() is called
 *
 */
public final class PieceInfoAlert extends TorrentAlert<piece_info_alert> {

    PieceInfoAlert(piece_info_alert alert) {
        super(alert);
    }

    /**
     * info about pieces being downloaded for the torrent.
     */
    public ArrayList<PartialPieceInfo> getPieceInfo() {
        partial_piece_info_vector v = alert.getPiece_info();
        int size = v.size();
        ArrayList<PartialPieceInfo> pieceInfo = new ArrayList<>(size);

        for (partial_piece_info e : v) {
            pieceInfo.add(new PartialPieceInfo(e));
        }

        return pieceInfo;
    }

    /**
     * storage for block_info pointers in partial_piece_info objects.
     */
    public ArrayList<BlockInfo> getBlockData() {
        block_info_vector v = alert.getBlock_data();
        int size = v.size();
        ArrayList<BlockInfo> blockData = new ArrayList<>(size);

        for (block_info e : v) {
            blockData.add(new BlockInfo(e));
        }

        return blockData;
    }
}
