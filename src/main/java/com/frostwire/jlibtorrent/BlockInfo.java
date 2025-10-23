package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.block_info;

/**
 * Download progress and state information for a single block within a piece.
 * <p>
 * {@code BlockInfo} represents the current state of a block (typically 16KB) being
 * downloaded. It tracks which peer is providing the data, how many bytes have been
 * received, the total block size, the block's state (none/requested/writing/finished),
 * and how many peers are requesting the same block (useful for parallel downloads).
 * <p>
 * <b>Understanding Block States:</b>
 * <br/>
 * Blocks progress through multiple states as they're downloaded and written to disk:
 * <ul>
 *   <li><b>NONE:</b> Block not yet requested (awaiting download)</li>
 *   <li><b>REQUESTED:</b> Block requested from peer, awaiting transfer</li>
 *   <li><b>WRITING:</b> Block received, queued for disk write</li>
 *   <li><b>FINISHED:</b> Block written to disk and hash-verified</li>
 * </ul>
 * <p>
 * <b>Block State Transitions:</b>
 * <pre>
 * NONE
 *  │
 *  ├─ (peer requests it) → REQUESTED
 *  │                          │
 *  │                   (data received) → WRITING
 *  │                                        │
 *  │                              (written to disk) → FINISHED
 *  │
 *  └─ (timeout) → back to NONE (retry)
 * </pre>
 * <p>
 * <b>Monitoring Block Progress with BlockInfo:</b>
 * <pre>
 * // Get information about blocks in incomplete pieces
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PIECE_INFO.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PieceInfoAlert a = (PieceInfoAlert) alert;
 *
 *         // Block-level detail: which peer, how many bytes, state
 *         java.util.ArrayList&lt;BlockInfo&gt; blockData = a.getBlockData();
 *         for (BlockInfo block : blockData) {
 *             TcpEndpoint peer = block.peer();
 *             int bytes = block.bytesProgress();
 *             int total = block.blockSize();
 *             BlockInfo.BlockState state = block.state();
 *
 *             System.out.println(\"Block from \" + peer);
 *             System.out.println(\"  Progress: \" + bytes + \" / \" + total + \" bytes\");
 *             System.out.println(\"  State: \" + state);
 *             System.out.println(\"  Peers: \" + block.numPeers());
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>Block Information Properties:</b>
 * <pre>
 * BlockInfo block = ...;  // From PieceInfoAlert
 *
 * // Which peer is providing this block?
 * TcpEndpoint peer = block.peer();
 * System.out.println(\"Downloaded from: \" + peer);
 *
 * // Download progress (partial blocks show intermediate state)
 * int received = block.bytesProgress();
 * int total = block.blockSize();
 * double progress = (double) received / total * 100;
 * System.out.println(String.format(\"Progress: %.1f%%\", progress));
 *
 * // Block size (typically 16KB, or less for last block)
 * System.out.println(\"Block size: \" + total + \" bytes\");
 *
 * // Current state
 * BlockInfo.BlockState state = block.state();
 * if (state == BlockInfo.BlockState.REQUESTED) {
 *     System.out.println(\"Block is downloading...\");
 * } else if (state == BlockInfo.BlockState.WRITING) {
 *     System.out.println(\"Block downloaded, writing to disk...\");
 * } else if (state == BlockInfo.BlockState.FINISHED) {
 *     System.out.println(\"Block complete and verified!\");
 * }
 *
 * // How many peers are downloading this block?
 * // (Parallel downloads of same block to speed up finish)
 * int peers = block.numPeers();
 * System.out.println(\"Downloading from \" + peers + \" peer(s)\");
 * </pre>
 * <p>
 * <b>Block State Enum - BlockState:</b>
 * <pre>
 * BlockInfo.BlockState.NONE:
 *   - Block not yet requested
 *   - Peer field is unspecified
 *   - bytesProgress() is zero
 *   - Awaiting selection by piece picker
 *
 * BlockInfo.BlockState.REQUESTED:
 *   - Block has been requested from a peer
 *   - Data transfer is in progress
 *   - bytesProgress() shows partial data (0 to blockSize)
 *   - Waiting for network delivery
 *
 * BlockInfo.BlockState.WRITING:
 *   - Block received completely
 *   - Queued for disk write
 *   - Hash verification in progress
 *   - Short-lived state (usually milliseconds)
 *
 * BlockInfo.BlockState.FINISHED:
 *   - Block written to disk
 *   - Hash verified (matches piece hash)
 *   - Fully part of the download
 *   - Stable end-state
 * </pre>
 * <p>
 * <b>Analyzing Download Performance:</b>
 * <pre>
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PIECE_INFO.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PieceInfoAlert a = (PieceInfoAlert) alert;
 *         java.util.ArrayList&lt;BlockInfo&gt; blocks = a.getBlockData();
 *
 *         // Count blocks in each state
 *         int requested = 0, writing = 0, finished = 0;
 *         double avgProgress = 0;
 *
 *         for (BlockInfo block : blocks) {
 *             switch (block.state()) {
 *                 case REQUESTED:
 *                     requested++;
 *                     avgProgress += (double) block.bytesProgress() / block.blockSize();
 *                     break;
 *                 case WRITING:
 *                     writing++;
 *                     break;
 *                 case FINISHED:
 *                     finished++;
 *                     break;
 *             }
 *         }
 *
 *         System.out.println(\"Active downloads (REQUESTED): \" + requested);
 *         System.out.println(\"  Average progress: \" +
 *             String.format(\"%.1f%%\", avgProgress / Math.max(requested, 1) * 100));
 *         System.out.println(\"Queued for write (WRITING): \" + writing);
 *         System.out.println(\"Verified (FINISHED): \" + finished);
 *     }
 * });
 * </pre>
 * <p>
 * <b>Parallel Block Downloads:</b>
 * <p>
 * At the end of a download, the same block may be requested from multiple peers
 * to speed up completion:
 * <pre>
 * // Near end of torrent - speed up final pieces
 * for (BlockInfo block : blockData) {
 *     int peers = block.numPeers();
 *     if (peers &gt; 1) {
 *         System.out.println(\"Fast-tracking: \" + peers + \" peers uploading same block\");
 *         // Multiple peers racing to transfer same block
 *         // First one to complete wins, others are cancelled
 *     }
 * }
 * </pre>
 * <p>
 * <b>Error Handling and Timeouts:</b>
 * <pre>
 * // If a peer stalls while downloading a block:
 * for (BlockInfo block : blockData) {
 *     if (block.state() == BlockInfo.BlockState.REQUESTED) {
 *         int received = block.bytesProgress();
 *         int total = block.blockSize();
 *
 *         // Stalled: no progress for a while
 *         if (received &gt; 0 &amp;&amp; received &lt; total) {
 *             TcpEndpoint stalled = block.peer();
 *             System.out.println(\"Peer \" + stalled + \" has stalled\");
 *             // Library may disconnect and retry with another peer
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>BlockInfo objects are read-only snapshots; state changes reflect in new alerts</li>
 *   <li>Block size is typically 16,384 bytes (16 KB)</li>
 *   <li>Last block of last piece may be smaller</li>
 *   <li>PIECE_INFO alerts are triggered frequently; suitable for UI updates</li>
 * </ul>
 *
 * @see PeerRequest - Represents a byte range request
 * @see PartialPieceInfo - Summary of all blocks in a piece
 * @see PieceInfoAlert - Contains BlockInfo for all partial pieces
 * @see BlockState - Enumeration of possible block states
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
     * This is the enum used for .
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
