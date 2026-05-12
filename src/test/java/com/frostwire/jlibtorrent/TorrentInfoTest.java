package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
     * {@code pieceSize} because hybrid torrents retain v1 piece semantics.
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
}
