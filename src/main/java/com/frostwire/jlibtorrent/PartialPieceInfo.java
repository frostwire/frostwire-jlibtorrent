package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.block_info_vector;
import com.frostwire.jlibtorrent.swig.partial_piece_info;

import java.util.ArrayList;

/**
 * This class holds information about pieces that have outstanding
 * requests or outstanding writes.
 *
 * @author gubatron
 * @author aldenml
 */
public final class PartialPieceInfo {

    private final partial_piece_info p;

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

    /**
     * This is an array (list) of ``blocks_in_piece`` number of
     * items. One for each block in the piece.
     *
     * @return all blocks information
     */
    public ArrayList<BlockInfo> blocks() {
        block_info_vector v = p.get_blocks();
        int size = (int) v.size();
        ArrayList<BlockInfo> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(new BlockInfo(v.get(i)));
        }

        return l;
    }

    /**
     * The download speed class this piece falls into.
     * This is used internally to cluster peers of the same
     * speed class together when requesting blocks.
     * <p>
     * Set to either ``fast``, ``medium``, ``slow`` or ``none``. It tells
     * which download rate category the peers downloading this piece falls
     * into. ``none`` means that no peer is currently downloading any part of
     * the piece. Peers prefer picking pieces from the same category as
     * themselves. The reason for this is to keep the number of partially
     * downloaded pieces down. Pieces set to ``none`` can be converted into
     * any of ``fast``, ``medium`` or ``slow`` as soon as a peer want to
     * download from it.
     *
     * @return the piece state
     */
    public State pieceState() {
        return State.fromSwig(p.getPiece_state().swigValue());
    }

    /**
     * The speed classes. These may be used by the piece picker to
     * coalesce requests of similar download rates
     */
    public enum State {

        /**
         *
         */
        NONE(partial_piece_info.state_t.none.swigValue()),

        /**
         *
         */
        SLOW(partial_piece_info.state_t.slow.swigValue()),

        /**
         *
         */
        MEDIUM(partial_piece_info.state_t.medium.swigValue()),

        /**
         *
         */
        FAST(partial_piece_info.state_t.fast.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        State(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        /**
         * @return the native value
         */
        public int swig() {
            return swigValue;
        }

        /**
         * @param swigValue the native value
         * @return the managed value
         */
        public static State fromSwig(int swigValue) {
            State[] enumValues = State.class.getEnumConstants();
            for (State ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
