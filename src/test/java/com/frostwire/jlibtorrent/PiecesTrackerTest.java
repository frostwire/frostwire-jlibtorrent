package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * {@link PiecesTracker}: how many bytes and pieces of a file are complete from its first piece on.
 * The expectations come from an independent model of the file layout (offsets and sizes).
 *
 * @author gubatron
 * @author aldenml
 */
public class PiecesTrackerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private TorrentInfo torrent() throws IOException {
        File root = TestTorrents.content(folder.getRoot(), "tracked", "a.bin", 30000, "b.bin", 70000);
        return TestTorrents.torrentInfo(TestTorrents.builder(root).flags(TorrentBuilder.V1_ONLY));
    }

    private static long overlap(long aStart, long aEnd, long bStart, long bEnd) {
        return Math.max(0, Math.min(aEnd, bEnd) - Math.max(aStart, bStart));
    }

    /** Bytes of the file covered by consecutive complete pieces from the file's first piece. */
    private static long[] model(TorrentInfo ti, boolean[] complete, int file) {
        FileStorage fs = ti.files();
        long start = fs.fileOffset(file);
        long end = start + fs.fileSize(file);
        int pieceLength = ti.pieceLength();
        int first = (int) (start / pieceLength);
        int last = (int) ((end - 1) / pieceLength);
        long bytes = 0;
        long pieces = 0;
        for (int p = first; p <= last && complete[p]; p++) {
            bytes += overlap(start, end, (long) p * pieceLength, (long) (p + 1) * pieceLength);
            pieces++;
        }
        return new long[]{bytes, pieces};
    }

    @Test
    public void countsMatchTheTorrent() throws IOException {
        TorrentInfo ti = torrent();
        PiecesTracker t = new PiecesTracker(ti);
        assertEquals(2, t.numFiles());
        assertEquals(7, t.numPieces());
        for (int i = 0; i < 7; i++) {
            assertFalse(t.isComplete(i));
        }
        assertEquals(0, t.getSequentialDownloadedBytes(0));
        assertEquals(0, t.getSequentialDownloadedPieces(0));
    }

    @Test
    public void sequentialBytesStopAtTheFirstMissingPiece() throws IOException {
        TorrentInfo ti = torrent();
        PiecesTracker t = new PiecesTracker(ti);
        boolean[] complete = new boolean[ti.numPieces()];

        t.setComplete(0, true);
        complete[0] = true;
        assertTrue(t.isComplete(0));
        check(ti, t, complete);

        // random completion patterns, always compared with the model
        Random r = new Random(7);
        for (int round = 0; round < 50; round++) {
            int piece = r.nextInt(ti.numPieces());
            boolean value = r.nextBoolean();
            t.setComplete(piece, value);
            complete[piece] = value;
            check(ti, t, complete);
        }

        for (int p = 0; p < ti.numPieces(); p++) {
            t.setComplete(p, true);
            complete[p] = true;
        }
        check(ti, t, complete);
        assertEquals(30000 + 70000, t.getSequentialDownloadedBytes(0) + t.getSequentialDownloadedBytes(1));
    }

    private static void check(TorrentInfo ti, PiecesTracker t, boolean[] complete) {
        for (int file = 0; file < ti.numFiles(); file++) {
            long[] expected = model(ti, complete, file);
            assertEquals("bytes of file " + file, expected[0], t.getSequentialDownloadedBytes(file));
            assertEquals("pieces of file " + file, expected[1], t.getSequentialDownloadedPieces(file));
        }
    }
}
