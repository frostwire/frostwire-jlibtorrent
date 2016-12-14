package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.block_info;

/**
 * Holds the state of a block in a piece. Who we requested
 * it from and how far along we are at downloading it.
 *
 * @author gubatron
 * @author aldenml
 */
public final class BlockInfo {

    private final block_info b;

    /**
     * @param b the native object
     */
    public BlockInfo(block_info b) {
        this.b = b;
    }

    /**
     * @return the native object
     */
    public block_info swig() {
        return b;
    }

    /**
     * The peer is the ip address of the peer this block was downloaded from.
     *
     * @return the peer tcp endpoint
     */
    public TcpEndpoint peer() {
        return new TcpEndpoint(b.peer());
    }

    /**
     * The number of bytes that have been received for this block.
     *
     * @return the number of bytes received
     */
    public int bytesProgress() {
        return (int) b.getBytes_progress();
    }

    /**
     * The total number of bytes in this block.
     *
     * @return otal number of bytes
     */
    public int blockSize() {
        return (int) b.getBlock_size();
    }

    /**
     * The state this block is in.
     *
     * @return the block's state
     */
    public BlockState state() {
        return BlockState.fromSwig((int) b.getState());
    }

    /**
     * The number of peers that is currently requesting this block. Typically
     * this is 0 or 1, but at the end of the torrent blocks may be requested
     * by more peers in parallel to speed things up.
     *
     * @return number of peers
     */
    public int numPeers() {
        return (int) b.getNum_peers();
    }

    /**
     * This is the enum used for {@link #state()}.
     */
    public enum BlockState {

        /**
         * This block has not been downloaded or requested form any peer.
         */
        NONE(block_info.block_state_t.none.swigValue()),

        /**
         * The block has been requested, but not completely downloaded yet.
         */
        REQUESTED(block_info.block_state_t.requested.swigValue()),

        /**
         * The block has been downloaded and is currently queued for being
         * written to disk.
         */
        WRITING(block_info.block_state_t.writing.swigValue()),

        /**
         * The block has been written to disk.
         */
        FINISHED(block_info.block_state_t.finished.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        BlockState(int swigValue) {
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
         * @return the state
         */
        public static BlockState fromSwig(int swigValue) {
            BlockState[] enumValues = BlockState.class.getEnumConstants();
            for (BlockState ev : enumValues) {
                if (ev.swig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
