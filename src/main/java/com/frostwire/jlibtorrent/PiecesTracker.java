package com.frostwire.jlibtorrent;

import java.util.ArrayList;

/**
 * Tracks download completion status for files and pieces in a torrent.
 * <p>
 * {@code PiecesTracker} provides a convenient way to monitor which pieces are complete
 * and calculate sequential download progress for individual files. It maps the relationship
 * between files and pieces, allowing efficient queries about download status.
 * <p>
 * <b>Understanding File-Piece Mapping:</b>
 * <br/>
 * In BitTorrent, files are divided into pieces (typically 256KB or 512KB each), and pieces
 * may span multiple files. This tracker maintains:
 * <ul>
 *   <li><b>File-Piece Map:</b> Which pieces constitute each file</li>
 *   <li><b>Piece Sizes:</b> How many bytes of each piece belong to each file</li>
 *   <li><b>Completion Status:</b> Boolean array of piece completion</li>
 * </ul>
 * <p>
 * <b>Creating a PiecesTracker:</b>
 * <pre>
 * // Create from torrent metadata
 * TorrentInfo ti = new TorrentInfo(torrentFile);
 * PiecesTracker tracker = new PiecesTracker(ti);
 *
 * System.out.println(\"Files: \" + tracker.numFiles());
 * System.out.println(\"Pieces: \" + tracker.numPieces());
 * </pre>
 * <p>
 * <b>Tracking Download Completion:</b>
 * <pre>
 * PiecesTracker tracker = new PiecesTracker(ti);
 *
 * // Mark pieces as complete (typically done by alert listener)
 * sm.addListener(new AlertListener() {
 *     public int[] types() {
 *         return new int[] {AlertType.PIECE_FINISHED.swig()};
 *     }
 *
 *     public void alert(Alert&lt;?&gt; alert) {
 *         PieceFinishedAlert pfa = (PieceFinishedAlert) alert;
 *         int piece = pfa.pieceIndex();
 *         tracker.setComplete(piece, true);
 *     }
 * });
 * </pre>
 * <p>
 * <b>Calculating Sequential Download Progress:</b>
 * <pre>
 * PiecesTracker tracker = new PiecesTracker(ti);
 *
 * // For each file, find how much has been downloaded sequentially
 * for (int fileIndex = 0; fileIndex &lt; tracker.numFiles(); fileIndex++) {
 *     long downloaded = tracker.getSequentialDownloadedBytes(fileIndex);
 *     int completePieces = tracker.getSequentialDownloadedPieces(fileIndex);
 *     long totalSize = ti.files().fileSize(fileIndex);
 *
 *     double percent = (double) downloaded / totalSize * 100;
 *     System.out.println(\"File \" + fileIndex + \": \" +\n *         String.format(\"%.1f%% (%d bytes, %d pieces)\",\n *             percent, downloaded, completePieces));\n * }
 * </pre>
 * <p>
 * <b>Sequential vs Total Progress:</b>
 * <p>
 * This tracker focuses on <b>sequential</b> download progress - how much of the file
 * has been downloaded starting from the beginning without gaps:
 * <pre>
 * // Example: File with 5 pieces
 * // Pieces downloaded: [✓ ✓ ✗ ✓ ✓]
 * //                     0 1 2 3 4
 *
 * // Sequential progress = 2 pieces (0, 1) - stops at first gap
 * int sequential = tracker.getSequentialDownloadedPieces(fileIndex);\n * // Result: 2\n *
 * // Total progress = 4 pieces downloaded (but not sequentially)
 * </pre>
 * <p>
 * <b>Monitoring Progress for Streaming:</b>
 * <pre>
 * // Useful for video/audio streaming that requires sequential data
 * PiecesTracker tracker = new PiecesTracker(ti);\n *
 * while (torrentActive) {
 *     for (int i = 0; i &lt; tracker.numFiles(); i++) {
 *         long seqBytes = tracker.getSequentialDownloadedBytes(i);
 *         long totalBytes = ti.files().fileSize(i);
 *
 *         // Can stream once we have 10% downloaded sequentially
 *         if (seqBytes &gt;= totalBytes * 0.1) {
 *             startStreaming(i);
 *         }
 *     }
 *     Thread.sleep(1000);  // Check every second
 * }
 * </pre>
 * <p>
 * <b>File-Piece Structure Example:</b>
 * <pre>
 * // 3 files, 4 pieces total
 * // File 0 (10MB) → spans pieces 0-1
 * // File 1 (5MB)  → spans pieces 1-2
 * // File 2 (15MB) → spans pieces 2-3
 *
 * tracker.setComplete(0, true);
 * tracker.setComplete(1, true);
 * // Download stalled at piece 2
 *
 * // Sequential downloads:
 * tracker.getSequentialDownloadedBytes(0);  // Full 10MB
 * tracker.getSequentialDownloadedBytes(1);  // Partial 5MB (piece 1 done, piece 2 missing)
 * tracker.getSequentialDownloadedBytes(2);  // 0 bytes (blocked on piece 2)
 * </pre>
 * <p>
 * <b>Performance Notes:</b>
 * <ul>
 *   <li>Initialization maps all files and pieces (O(n) operation)</li>
 *   <li>Status queries are O(1) for individual pieces</li>
 *   <li>Sequential download calculation is O(pieces_in_file)</li>
 *   <li>Useful for priority-based downloading based on user streaming needs</li>
 * </ul>
 *
 * @see TorrentInfo - Source of file/piece mapping data
 * @see FileStorage - File size and path information
 * @see FileSlice - Individual file chunks within pieces
 * @see PieceFinishedAlert - Listen for piece completion
 *
 * @author gubatron
 * @author aldenml
 */
public final class PiecesTracker {

    private final int numFiles;
    private final int numPieces;

    private final int[][] files;
    private final long[][] sizes;
    private final boolean[] complete;

    public PiecesTracker(TorrentInfo ti) {
        this.numFiles = ti.numFiles();
        this.numPieces = ti.numPieces();

        this.files = new int[numFiles][];
        this.sizes = new long[numFiles][];
        this.complete = new boolean[numPieces];

        FileStorage fs = ti.files();

        for (int fileIndex = 0; fileIndex < numFiles; fileIndex++) {
            long size = fs.fileSize(fileIndex);
            int firstPiece = ti.mapFile(fileIndex, 0, 1).piece();
            int lastPiece = ti.mapFile(fileIndex, size - 1, 1).piece();

            int numSlices = lastPiece - firstPiece + 1;
            files[fileIndex] = new int[numSlices];
            sizes[fileIndex] = new long[numSlices];

            for (int pieceIndex = firstPiece; pieceIndex <= lastPiece; pieceIndex++) {
                int index = pieceIndex - firstPiece;

                files[fileIndex][index] = pieceIndex;

                ArrayList<FileSlice> slices = ti.mapBlock(pieceIndex, 0, ti.pieceSize(pieceIndex));
                for (FileSlice slice : slices) {
                    if (slice.fileIndex() == fileIndex) {
                        sizes[fileIndex][index] = slice.size();
                    }
                }
            }
        }
    }

    public int numFiles() {
        return numFiles;
    }

    public int numPieces() {
        return numPieces;
    }

    public boolean isComplete(int pieceIndex) {
        return complete[pieceIndex];
    }

    public void setComplete(int pieceIndex, boolean complete) {
        this.complete[pieceIndex] = complete;
    }

    public long getSequentialDownloadedBytes(int fileIndex) {
        int[] pieces = files[fileIndex];

        long downloaded = 0;

        for (int i = 0; i < pieces.length; i++) {
            int pieceIndex = pieces[i];

            if (complete[pieceIndex]) {
                downloaded += sizes[fileIndex][i];
            } else {
                break;
            }
        }

        return downloaded;
    }

    public int getSequentialDownloadedPieces(int fileIndex) {
        int[] pieces = files[fileIndex];

        int count = 0;

        for (int i = 0; i < pieces.length; i++) {
            int pieceIndex = pieces[i];

            if (complete[pieceIndex]) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }
}
