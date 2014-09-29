package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.torrent_info;

import java.io.File;

/**
 * This class represents the information stored in a .torrent file
 *
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
        return ti.num_pieces();
    }

    /**
     * returns the info-hash of the torrent.
     *
     * @return
     */
    public Sha1Hash getInfoHash() {
        return new Sha1Hash(ti.info_hash());
    }

    public String mkString() {
        StringBuilder sb = new StringBuilder();

        sb.append("total_size = " + getTotalSize() + System.lineSeparator());
        sb.append("piece_length = " + getPieceLength() + System.lineSeparator());
        sb.append("num_pieces = " + getNumPieces() + System.lineSeparator());
        sb.append("info_hash = " + getInfoHash() + System.lineSeparator());

        return sb.toString();
    }

    /**
     * Generates a magnet URI from the specified torrent. If the torrent
     * is invalid, null is returned.
     * <p/>
     * For more information about magnet links, see magnet-links_.
     *
     * @return
     */
    public String makeMagnetUri() {
        return ti.is_valid() ? libtorrent.make_magnet_uri(ti) : null;
    }

    /**
     * The file_storage object contains the information on how to map the pieces to
     * files. It is separated from the torrent_info object because when creating torrents
     * a storage object needs to be created without having a torrent file. When renaming files
     * in a storage, the storage needs to make its own copy of the file_storage in order
     * to make its mapping differ from the one in the torrent file.
     *
     * @return
     */
    public FileStorage getFiles() {
        return new FileStorage(ti.files());
    }

    /**
     * Returns true if this torrent_info object has a torrent loaded.
     * <p/>
     * This is primarily used to determine if a magnet link has had its
     * metadata resolved yet or not.
     *
     * @return
     */
    public boolean isValid() {
        return ti.is_valid();
    }

    public String getHash() {
        return getInfoHash().toString();
    }
}
