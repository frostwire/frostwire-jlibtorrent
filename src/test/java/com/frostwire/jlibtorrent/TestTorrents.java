package com.frostwire.jlibtorrent;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Builds small deterministic content and the torrents that describe it, for the tests that need a
 * real torrent without any network.
 *
 * @author gubatron
 * @author aldenml
 */
final class TestTorrents {

    static final int PIECE_SIZE = 16384;

    private TestTorrents() {
    }

    /** Deterministic pseudo random bytes. */
    static byte[] bytes(int size, long seed) {
        byte[] b = new byte[size];
        new Random(seed).nextBytes(b);
        return b;
    }

    /**
     * Writes {@code dir/<name>/} holding the given files (name, size) and returns the content
     * directory (the torrent's root, named {@code name}).
     */
    static File content(File parent, String name, Object... nameAndSize) throws IOException {
        File root = new File(parent, name);
        if (!root.mkdirs() && !root.isDirectory()) {
            throw new IOException("can't create " + root);
        }
        for (int i = 0; i < nameAndSize.length; i += 2) {
            Utils.writeByteArrayToFile(new File(root, (String) nameAndSize[i]),
                    bytes((Integer) nameAndSize[i + 1], i + 1), false);
        }
        return root;
    }

    /** A builder for {@code root} with a fixed piece size. */
    static TorrentBuilder builder(File root) {
        return new TorrentBuilder().path(root).pieceSize(PIECE_SIZE);
    }

    static TorrentInfo torrentInfo(TorrentBuilder builder) throws IOException {
        return TorrentInfo.bdecode(builder.generate().entry().bencode());
    }
}
