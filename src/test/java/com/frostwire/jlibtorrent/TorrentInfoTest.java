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
        assertEquals("Sum of pieceSizeForReq must equal total torrent size (no pad blocks)",
                totalSize, totalReqSize);
    }

    /**
     * Verifies that for a v2 torrent, {@code pieceSizeForReq} is monotonically
     * non-increasing: earlier pieces are full size, later pieces are the same or smaller.
     * This is a structural sanity check for v2 pad-block handling.
     */
    @Test
    public void testPieceSizeForReqMonotonic() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-test.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);
        int numPieces = ti.numPieces();

        int previous = Integer.MAX_VALUE;
        for (int i = 0; i < numPieces; i++) {
            int reqSize = ti.pieceSizeForReq(i);
            assertTrue("pieceSizeForReq must be non-increasing (piece " + i + ")",
                    reqSize <= previous);
            previous = reqSize;
        }
    }

    /**
     * Tests that out-of-range piece indices return 0 from
     * {@code pieceSizeForReq} rather than crashing.
     */
    @Test
    public void testPieceSizeForReqInvalidIndex() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("test1.torrent");
        TorrentInfo ti = new TorrentInfo(torrentFileBytes);

        assertEquals("pieceSizeForReq(-1) should return 0", 0, ti.pieceSizeForReq(-1));
        assertEquals("pieceSizeForReq(numPieces) should return 0", 0, ti.pieceSizeForReq(ti.numPieces()));
    }
}
