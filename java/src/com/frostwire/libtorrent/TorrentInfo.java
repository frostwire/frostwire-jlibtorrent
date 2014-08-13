package com.frostwire.libtorrent;

public class TorrentInfo {

    private final long h;

    public TorrentInfo(String filename, int flags) {
        this.h = create(filename, flags);
    }

    public long totalSize() {
        return this.totalSize(h);
    }

    public int pieceLength() {
        return this.pieceLength(h);
    }

    public int numPieces() {
        return this.numPieces(h);
    }

    public String infoHash() {
        return this.infoHash(h);
    }

    public String mkString() {
        StringBuilder sb = new StringBuilder();

        sb.append("total_size = " + totalSize() + System.lineSeparator());
        sb.append("piece_length = " + pieceLength() + System.lineSeparator());
        sb.append("num_pieces = " + numPieces() + System.lineSeparator());
        sb.append("info_hash = " + infoHash() + System.lineSeparator());

        return sb.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.h != 0) {
            release(this.h);
        }
        super.finalize();
    }

    private native long create(String filename, int flags);

    private native void release(long h);

    private native long totalSize(long h);

    private native int pieceLength(long h);

    private native int numPieces(long h);

    private native String infoHash(long h);
}
