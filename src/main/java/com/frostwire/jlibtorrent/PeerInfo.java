package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_info;

/**
 * Holds information and statistics about one peer
 * that libtorrent is connected to.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerInfo {

    private final peer_info p;

    PeerInfo(peer_info p) {
        this.p = p;
    }

    /**
     * @return the native object
     */
    public peer_info swig() {
        return p;
    }

    /**
     * This corresponds to a native string value. This string
     * could contains problematic values for UTF-8 conversions.
     * <p>
     * This describe the software at the other end of the connection.
     * In some cases this information is not available, then it will contain
     * a string that may give away something about which software is running
     * in the other end. In the case of a web seed, the server type and
     * version will be a part of this string.
     *
     * @return the raw bytes of the string
     */
    public byte[] client() {
        return Vectors.byte_vector2bytes(p.get_client());
    }

    /**
     * A bitfield, with one bit per piece in the torrent. Each bit tells you
     * if the peer has that piece (if it's set to 1) or if the peer miss that
     * piece (set to 0).
     *
     * @return the bitfield of pieces
     */
    public PieceIndexBitfield pieces() {
        return new PieceIndexBitfield(p.getPieces());
    }

    /**
     * The total number of bytes downloaded from this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes downloaded
     */
    public long totalDownload() {
        return p.getTotal_download();
    }

    /**
     * The total number of bytes uploaded to this peer.
     * These numbers do not include the protocol chatter, but only the
     * payload data.
     *
     * @return number of bytes uploaded
     */
    public long totalUpload() {
        return p.getTotal_upload();
    }
}
