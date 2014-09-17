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

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_info;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentInfo {

    private final torrent_info ti;

    public TorrentInfo(torrent_info ti) {
        this.ti = ti;
    }

    public TorrentInfo(File torrentFile) {
        this(new torrent_info(torrentFile.getAbsolutePath()));
    }

    public torrent_info getSwig() {
        return this.ti;
    }

    public String getName() {
        return ti.name();
    }

    /**
     * The total number of bytes the torrent-file represents (all the files in it).
     *
     * @return
     */
    public long getTotalSize() {
        return this.ti.total_size();
    }

    /**
     * The number of byte for each piece.
     * <p/>
     * The difference between piece_size() and piece_length() is that piece_size() takes
     * the piece index as argument and gives you the exact size of that piece. It will always
     * be the same as piece_length() except in the case of the last piece, which may be smaller.
     *
     * @return
     */
    public int getPieceLength() {
        return this.ti.piece_length();
    }

    /**
     * The total number of pieces.
     *
     * @return
     */
    public int getNumPieces() {
        return this.ti.num_pieces();
    }

    public String getInfoHash() {
        return LibTorrent.info_hash2string(ti.info_hash());
    }

    public String mkString() {
        StringBuilder sb = new StringBuilder();

        sb.append("total_size = " + getTotalSize() + System.lineSeparator());
        sb.append("piece_length = " + getPieceLength() + System.lineSeparator());
        sb.append("num_pieces = " + getNumPieces() + System.lineSeparator());
        sb.append("info_hash = " + getInfoHash() + System.lineSeparator());

        return sb.toString();
    }
}
