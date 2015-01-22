package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_slice;

/**
 * Represents a window of a file in a torrent.
 * <p/>
 * The {@link #getFileIndex()} refers to the index of the file (in the {@link com.frostwire.jlibtorrent.TorrentInfo}).
 * To get the path and filename, use {@link com.frostwire.jlibtorrent.TorrentInfo#getFileAt(int)} and give the
 * {@link #getFileIndex()} as argument. The {@link #getOffset()} is the byte offset in the file where the range
 * starts, and {@link #getSize()} is the number of bytes this range is. The size + offset
 * will never be greater than the file size.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileSlice {

    private final file_slice e;

    public FileSlice(file_slice e) {
        this.e = e;
    }

    public file_slice getSwig() {
        return e;
    }

    /**
     * The index of the file.
     *
     * @return
     */
    public int getFileIndex() {
        return e.getFile_index();
    }

    /**
     * The offset from the start of the file, in bytes.
     *
     * @return
     */
    public long getOffset() {
        return e.getOffset();
    }

    /**
     * The size of the window, in bytes.
     *
     * @return
     */
    public long getSize() {
        return e.getSize();
    }
}
