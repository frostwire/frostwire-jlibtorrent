package com.frostwire.libtorrent;

public class TorrentInfo {

    private final long h;

    public TorrentInfo(String filename, int flags) {
        this.h = create(filename, flags);
    }

    public long total_size() {
        return this.total_size(h);
    }

    public int piece_length() {
        return this.piece_length(h);
    }

    public int num_pieces() {
        return this.num_pieces(h);
    }

    public String info_hash() {
        return info_hash(h);
    }

    public String mkString() {
        StringBuilder sb = new StringBuilder();

        sb.append("total_size = " + total_size() + System.lineSeparator());
        sb.append("piece_length = " + piece_length() + System.lineSeparator());
        sb.append("num_pieces = " + num_pieces() + System.lineSeparator());
        sb.append("info_hash = " + info_hash() + System.lineSeparator());

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

    private native long total_size(long h);

    private native int piece_length(long h);

    private native int num_pieces(long h);

    private native String info_hash(long h);
}
