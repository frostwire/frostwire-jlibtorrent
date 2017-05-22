package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.partial_piece_info;

/**
 * This class holds information about pieces that have outstanding
 * requests or outstanding writes.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PartialPieceInfo {

    private final partial_piece_info p;

    /**
     * @param p the native object
     */
    public PartialPieceInfo(partial_piece_info p) {
        this.p = p;
    }

    /**
     * @return the native object
     */
    public partial_piece_info swig() {
        return p;
    }

    /**
     * The index of the piece in question. {@link #blocksInPiece()} is
     * the number of blocks in this particular piece. This number will
     * be the same for most pieces, but the last piece may have fewer
     * blocks than the standard pieces.
     *
     * @return the piece index
     */
    public int pieceIndex() {
        return p.getPiece_index();
    }

    /**
     * The number of blocks in this piece.
     *
     * @return the number of blocks
     */
    public int blocksInPiece() {
        return p.getBlocks_in_piece();
    }

    /**
     * The number of blocks that are in the finished state.
     *
     * @return the number of finished blocks
     */
    public int finished() {
        return p.getFinished();
    }

    /**
     * The number of blocks that are in the writing state.
     *
     * @return the number of blocks in writing state
     */
    public int writing() {
        return p.getWriting();
    }

    /**
     * The number of blocks that are in the requested state.
     *
     * @return the number of blocks in requested state
     */
    public int requested() {
        return p.getRequested();
    }
}
