package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_request;

/**
 * represents a byte range within a piece. Internally this is
 * is used for incoming piece requests.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PeerRequest {

    private final peer_request r;

    public PeerRequest(peer_request r) {
        this.r = r;
    }

    public peer_request getSwig() {
        return r;
    }

    /**
     * the index of the piece in which the range starts.
     *
     * @return
     */
    public int getPiece() {
        return r.getPiece();
    }

    /**
     * the offset within that piece where the range starts.
     *
     * @return
     */
    public int getStart() {
        return r.getStart();
    }

    /**
     * the size of the range, in bytes.
     *
     * @return
     */
    public int getLength() {
        return r.getLength();
    }
}
