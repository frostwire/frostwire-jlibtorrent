package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_request;

/**
 * Represents a byte range within a piece. Internally this is
 * is used for incoming piece requests.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerRequest {

    private final peer_request r;

    // internal
    public PeerRequest(peer_request r) {
        this.r = r;
    }

    /**
     * @return native object
     */
    public peer_request swig() {
        return r;
    }

    /**
     * The index of the piece in which the range starts.
     *
     * @return the piece index
     */
    public int piece() {
        return r.getPiece();
    }

    /**
     * The offset within that piece where the range starts.
     *
     * @return the start offset
     */
    public int start() {
        return r.getStart();
    }

    /**
     * The size of the range, in bytes.
     *
     * @return the range length
     */
    public int length() {
        return r.getLength();
    }

    /**
     * @return string representation
     */
    @Override
    public String toString() {
        return "PeerRequest(piece: " + piece() + ", start: " + start() + ", length: " + length() + ")";
    }
}
