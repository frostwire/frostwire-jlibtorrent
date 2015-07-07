package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_slice;

/**
 * Represents a window of a file in a torrent.
 * <p/>
 * The {@link #getFileIndex()} refers to the index of the file (in the {@link com.frostwire.jlibtorrent.TorrentInfo}).
 * To get the path and filename, use {@link com.frostwire.jlibtorrent.TorrentInfo#getFileAt(int)} and give the
 * {@link #getFileIndex()} as argument. The {@link #getOffset()} is the byte offset in the file where the range
 * starts, and {@link #getSize()} is the number of bytes this range is. The {@code size + offset}
 * will never be greater than the file size.
 * <p/>
 * Implementation note: This class does not store internally a reference to the native swig file_slice. This is because
 * we are dealing with only three integral values and we want to avoid keeping a reference to a memory in the
 * native heap.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileSlice {

    private final int fileIndex;
    private final long offset;
    private final long size;

    public FileSlice(file_slice e) {
        this.fileIndex = e.getFile_index();
        this.offset = e.getOffset();
        this.size = e.getSize();
    }

    /**
     * The index of the file.
     *
     * @return
     */
    public int getFileIndex() {
        return fileIndex;
    }

    /**
     * The offset from the start of the file, in bytes.
     *
     * @return
     */
    public long getOffset() {
        return offset;
    }

    /**
     * The size of the window, in bytes.
     *
     * @return
     */
    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("FileSlice(fileIndex: %d, offset: %d, size: %d)", fileIndex, offset, size);
    }
}
