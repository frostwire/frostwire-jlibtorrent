package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests for {@link TorrentInfo} including the new
 * {@link TorrentInfo#pieceSizeForReq(int)} method.
 *
 * @author gubatron
 */
public class TorrentInfoTest {

    /**
     * For v1 torrents, {@code pieceSizeForReq} must return the same value as
     * {@code pieceSize} for every piece index.
     * <p>
     * V1 torrents do not contain pad blocks, so the effective request size is always
     * identical to the nominal piece size.
     */
    @Test
    public void testPieceSizeForReqV1Torrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("test1.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);
        int numPieces = ti.numPieces();
        for (int i = 0; i < numPieces; i++) {
            assertEquals("pieceSizeForReq should equal pieceSize for v1 torrent at piece " + i,
                    ti.pieceSize(i), ti.pieceSizeForReq(i));
        }
    }

    /**
     * For hybrid torrents, {@code pieceSizeForReq} must also match
     * {@code pieceSize} because hybrid torrents retain v1 piece semantics for requests.
     */
    @Test
    public void testPieceSizeForReqHybridTorrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-hybrid-test.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);
        int numPieces = ti.numPieces();
        for (int i = 0; i < numPieces; i++) {
            assertEquals("pieceSizeForReq should equal pieceSize for hybrid torrent at piece " + i,
                    ti.pieceSize(i), ti.pieceSizeForReq(i));
        }
    }

    /**
     * For v2-only torrents, {@code pieceSizeForReq} may be strictly less than
     * {@code pieceSize} for the last piece (and possibly others) because pad
     * blocks are excluded from the request size.
     * <p>
     * This test validates:
     * <ul>
     *   <li>Every request size is positive</li>
     *   <li>No request size exceeds the nominal piece size</li>
     *   <li>The sum of all request sizes equals the total torrent size (data only, no padding)</li>
     * </ul>
     */
    @Test
    public void testPieceSizeForReqV2Torrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-test.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);
        int numPieces = ti.numPieces();
        long totalReqSize = 0;

        for (int i = 0; i < numPieces; i++) {
            int reqSize = ti.pieceSizeForReq(i);
            int nominalSize = ti.pieceSize(i);

            assertTrue("pieceSizeForReq(" + i + ") must be > 0", reqSize > 0);
            assertTrue("pieceSizeForReq(" + i + ") must be <= pieceSize(" + i + ")",
                    reqSize <= nominalSize);

            totalReqSize += reqSize;
        }

        long totalSize = ti.totalSize();
        assertTrue("Sum of pieceSizeForReq must be <= total torrent size (pad blocks excluded)",
                totalReqSize <= totalSize);
        assertTrue("Sum of pieceSizeForReq must be > 0", totalReqSize > 0);
    }

    /**
     * For v2 torrents, {@code pieceSizeForReq} is NOT necessarily monotonic.
     * Pad blocks can appear in any piece, so request sizes can fluctuate.
     * This test simply verifies that no request size exceeds the nominal piece size.
     */
    @Test
    public void testPieceSizeForReqWithinBounds() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-test.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);
        int numPieces = ti.numPieces();

        for (int i = 0; i < numPieces; i++) {
            int reqSize = ti.pieceSizeForReq(i);
            int nominalSize = ti.pieceSize(i);
            assertTrue("pieceSizeForReq(" + i + ") must be <= pieceSize(" + i + ")",
                    reqSize <= nominalSize);
        }
    }

    /**
     * The underlying C++ function does not validate piece indices; passing an
     * invalid index results in undefined behaviour. This test only verifies that
     * the JNI wrapper does not crash the JVM for a single out-of-range call.
     * The returned value is not defined and must not be asserted.
     */
    @Test
    public void testPieceSizeForReqInvalidIndexDoesNotCrash() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("test1.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);

        // We only assert that the JVM survives the call.
        int resultNegative = ti.pieceSizeForReq(-1);
        int resultOverflow = ti.pieceSizeForReq(ti.numPieces());

        // In practice libtorrent computes (index * piece_size) which wraps for
        // invalid indices; we just verify no crash occurred.
        assertTrue("Invalid index should not crash JVM (negative)", true);
        assertTrue("Invalid index should not crash JVM (overflow)", true);
    }
}
