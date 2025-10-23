package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.partial_piece_info;

/**
 * Summary statistics for an incomplete piece with ongoing block transfers.
 * <p>
 * {@code PartialPieceInfo} provides aggregated information about a piece that is currently
 * being downloaded. It tracks how many blocks are in each state (finished, writing, requested)
 * and the piece's size. This is useful for monitoring download progress, understanding which
 * pieces are being worked on, and diagnostics of transfer performance.
 * <p>
 * <b>Understanding Partial Pieces:</b>
 * <br/>
 * As downloads progress, pieces move through states:
 * <ul>
 *   <li><b>Partial Piece:</b> Has outstanding blocks (not all downloaded yet)</li>
 *   <li><b>Complete Piece:</b> All blocks downloaded, hash-verified, no longer tracked</li>
 *   <li><b>Block Count:</b> Varies by piece size (usually 16+ blocks of 16KB each)</li>
 *   <li><b>Last Piece:</b> May have fewer blocks if piece size doesn't divide evenly</li>
 * </ul>
 * <p>
 * <b>Block Distribution in a Piece:</b>
 * <pre>
 * Piece #10 (256 KB, 16 blocks)
 * ┌──────────────────────────────────────┐
 * │ Block States:                        │
 * │  FINISHED: 8 blocks (128 KB done)   │
 * │  WRITING:  2 blocks (32 KB queued)   │
 * │  REQUESTED: 4 blocks (64 KB in xfer) │
 * │  NONE:      2 blocks (not started)   │
 * └──────────────────────────────────────┘
 *
 * PartialPieceInfo summary:
 *   Piece index: 10
 *   Total blocks: 16
 *   Finished: 8
 *   Writing: 2
 *   Requested: 4
 * </pre>
 * <p>
 * <b>Accessing Partial Piece Information:</b>
 * <pre>
 * // From PieceInfoAlert - reports on all partial pieces
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PIECE_INFO.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PieceInfoAlert a = (PieceInfoAlert) alert;
 *         java.util.ArrayList&lt;PartialPieceInfo&gt; pieces = a.getPieceInfo();
 *
 *         for (PartialPieceInfo piece : pieces) {
 *             int idx = piece.pieceIndex();
 *             int total = piece.blocksInPiece();
 *             int done = piece.finished();
 *             int writing = piece.writing();
 *             int requested = piece.requested();
 *
 *             System.out.println(\"Piece \" + idx + \":\");
 *             System.out.println(\"  Total blocks: \" + total);
 *             System.out.println(\"  Finished: \" + done);
 *             System.out.println(\"  Writing: \" + writing);
 *             System.out.println(\"  Requested: \" + requested);
 *         }
 *     }
 * });
 * </pre>
 * <p>
 * <b>Interpreting PartialPieceInfo:</b>
 * <pre>
 * PartialPieceInfo piece = ...;
 *
 * // Which piece is this?
 * int pieceIdx = piece.pieceIndex();
 * System.out.println(\"Piece index: \" + pieceIdx);
 *
 * // How many blocks does this piece have?
 * // (Typically 16 for 256KB pieces, but last piece may be smaller)
 * int numBlocks = piece.blocksInPiece();
 * System.out.println(\"Total blocks: \" + numBlocks);
 *
 * // How many are completely done?
 * int finishedBlocks = piece.finished();
 * System.out.println(\"Finished: \" + finishedBlocks);
 *
 * // How many are queued for disk write?
 * int writingBlocks = piece.writing();
 * System.out.println(\"Writing to disk: \" + writingBlocks);
 *
 * // How many are currently being downloaded?
 * int requestedBlocks = piece.requested();
 * System.out.println(\"Currently downloading: \" + requestedBlocks);
 *
 * // How many are waiting to be downloaded?
 * int notStarted = numBlocks - finishedBlocks - writingBlocks - requestedBlocks;
 * System.out.println(\"Not yet started: \" + notStarted);
 * </pre>
 * <p>
 * <b>Calculating Download Progress:</b>
 * <pre>
 * java.util.ArrayList&lt;PartialPieceInfo&gt; pieces = ...;
 *
 * // Overall progress metrics
 * int totalPieces = pieces.size();
 * int totalBlocks = 0;
 * int finishedBlocks = 0;
 * int writingBlocks = 0;
 * int requestedBlocks = 0;
 *
 * for (PartialPieceInfo piece : pieces) {
 *     totalBlocks += piece.blocksInPiece();
 *     finishedBlocks += piece.finished();
 *     writingBlocks += piece.writing();
 *     requestedBlocks += piece.requested();
 * }
 *
 * System.out.println(\"Download Status:\");
 * System.out.println(\"  Partial pieces: \" + totalPieces);
 * System.out.println(\"  Total blocks: \" + totalBlocks);
 * System.out.println(\"  Finished: \" + finishedBlocks + \" (\" +
 *     String.format(\"%.1f%%\", (double)finishedBlocks/totalBlocks*100) + \")\");
 * System.out.println(\"  Writing: \" + writingBlocks);
 * System.out.println(\"  Active transfers: \" + requestedBlocks);
 * </pre>
 * <p>
 * <b>Piece State Progression:</b>
 * <pre>
 * Piece #5 download progression:
 *
 * Initial state (piece just started):
 *   Finished: 0, Writing: 0, Requested: 3, Not started: 13
 *
 * After 5 seconds (good peers, data flowing):
 *   Finished: 5, Writing: 1, Requested: 8, Not started: 2
 *
 * Near complete (most blocks done):
 *   Finished: 14, Writing: 1, Requested: 1, Not started: 0
 *   → Last block being downloaded
 *
 * All done:
 *   Piece moves out of "partial" list
 *   No longer reported in PieceInfoAlert
 * </pre>
 * <p>
 * <b>Monitoring Multiple Piece Progress:</b>
 * <pre>
 * // Track which piece is closest to completion
 * java.util.ArrayList&lt;PartialPieceInfo&gt; pieces = ...;
 *
 * PartialPieceInfo nextToFinish = null;
 * double maxProgress = 0;
 *
 * for (PartialPieceInfo piece : pieces) {
 *     double progress = (double) piece.finished() / piece.blocksInPiece();
 *     if (progress &gt; maxProgress) {
 *         maxProgress = progress;
 *         nextToFinish = piece;
 *     }
 * }
 *
 * if (nextToFinish != null) {
 *     System.out.println(\"Next piece to complete: \" + nextToFinish.pieceIndex());
 *     System.out.println(\"  Progress: \" +
 *         String.format(\"%.1f%%\", maxProgress * 100));
 * }
 * </pre>
 * <p>
 * <b>Piece Size Variations:</b>
 * <pre>
 * // Most pieces have the same size
 * // But the last piece may be smaller
 *
 * // Example: 1GB torrent with 16MB pieces
 * // Pieces 0-63: 16MB each (1024 blocks each)
 * // Piece 64: 0.5MB (32 blocks) - the remainder
 *
 * // PartialPieceInfo.blocksInPiece() reflects the actual block count
 * </pre>
 * <p>
 * <b>Performance Diagnostics:</b>
 * <pre>
 * // If a piece is stuck in "writing" state for too long
 * for (PartialPieceInfo piece : pieces) {
 *     if (piece.writing() &gt; 5) {
 *         System.out.println(\"Warning: Piece \" + piece.pieceIndex() +
 *             \" has \" + piece.writing() + \" blocks queued for disk write\");
 *         System.out.println(\"  May indicate slow disk or I/O bottleneck\");
 *     }
 * }
 *
 * // If requested blocks are stuck (not progressing)
 * if (pieces.size() &gt; 0) {
 *     int totalRequested = pieces.stream()
 *         .mapToInt(PartialPieceInfo::requested)
 *         .sum();
 *     if (totalRequested &gt; 10) {
 *         System.out.println(\"High number of pending requests: \" + totalRequested);
 *         System.out.println(\"  May indicate network congestion or stalled peers\");
 *     }
 * }
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>PartialPieceInfo only tracks pieces actively being downloaded</li>
 *   <li>Completed pieces are removed from tracking</li>
 *   <li>PieceInfoAlert is triggered frequently (suitable for UI updates)</li>
 *   <li>Block counts don't change mid-transfer; snapshots capture state</li>
 *   <li>Typical piece has 16-256 blocks depending on piece size</li>
 * </ul>
 *
 * @see BlockInfo - Detailed state of individual blocks
 * @see PeerRequest - Byte range within piece being transferred
 * @see PieceInfoAlert - Contains PartialPieceInfo for all partial pieces
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
