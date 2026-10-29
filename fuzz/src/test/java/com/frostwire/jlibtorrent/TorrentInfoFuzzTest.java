package com.frostwire.jlibtorrent;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Fuzzes {@link TorrentInfo#TorrentInfo(byte[])} and then reads everything a client reads from a
 * parsed torrent, through the Java wrappers and the SWIG layer.
 * <p>
 * Input: .torrent files from the web, from users and from peers (metadata exchange). The
 * constructor documents {@link IllegalArgumentException} for data it can't decode; anything else,
 * a hang, or a native crash (see {@link FuzzInputs}) is a finding.
 */
class TorrentInfoFuzzTest {

    @FuzzTest
    void parseAndRead(FuzzedDataProvider data) {
        byte[] bytes = FuzzInputs.record(data.consumeRemainingAsBytes());
        TorrentInfo ti;
        try {
            ti = new TorrentInfo(bytes);
        } catch (IllegalArgumentException expected) {
            return;
        }
        ti.isValid();
        ti.name();
        ti.comment();
        ti.creator();
        ti.creationDate();
        ti.isPrivate();
        ti.isI2p();
        ti.totalSize();
        ti.trackers().forEach(t -> t.url());
        ti.webSeeds().forEach(w -> w.url());
        ti.collections();
        ti.nodes();
        // null when the torrent has no hash of that version (v2-only / v1-only)
        Sha1Hash v1 = ti.infoHashV1();
        Sha256Hash v2 = ti.infoHashV2();
        if (v1 == null && v2 == null) {
            throw new IllegalStateException("a parsed torrent has neither a v1 nor a v2 info-hash");
        }
        if (v1 != null) {
            v1.toHex();
        }
        if (v2 != null) {
            v2.toHex();
        }
        ti.similarTorrents();
        int pieces = ti.numPieces();
        for (int p : new int[]{0, pieces / 2, pieces - 1}) {
            if (p >= 0 && p < pieces) {
                ti.pieceSize(p);
                ti.pieceSizeForReq(p);
            }
        }
        FileStorage fs = ti.files();
        int files = Math.min(fs.numFiles(), 64);
        for (int i = 0; i < files; i++) {
            fs.filePath(i);
            fs.fileName(i);
            fs.fileSize(i);
            fs.fileOffset(i);
            fs.padFileAt(i);
        }
        ti.makeMagnetUri();
        byte[] encoded = ti.bencode();
        new TorrentInfo(encoded);
    }
}
