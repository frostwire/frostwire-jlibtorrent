package com.frostwire.libtorrent;

import com.frostwire.libtorrent.swig.libtorrent;
import com.frostwire.libtorrent.swig.torrent_info;

public class TorrentInfo {

    private final torrent_info inf;

    public TorrentInfo(String filename) {
        this.inf = new torrent_info(filename);
    }

    public long totalSize() {
        return this.inf.total_size();
    }

    public int pieceLength() {
        return this.inf.piece_length();
    }

    public int numPieces() {
        return this.inf.num_pieces();
    }

    public String infoHash() {
        return libtorrent.to_hex(this.inf.info_hash().to_string());
    }

    public torrent_info getInf() {
        return this.inf;
    }

    public String mkString() {
        StringBuilder sb = new StringBuilder();

        sb.append("total_size = " + totalSize() + System.lineSeparator());
        sb.append("piece_length = " + pieceLength() + System.lineSeparator());
        sb.append("num_pieces = " + numPieces() + System.lineSeparator());
        sb.append("info_hash = " + infoHash() + System.lineSeparator());

        return sb.toString();
    }
}
