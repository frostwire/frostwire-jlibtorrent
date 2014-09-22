/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
