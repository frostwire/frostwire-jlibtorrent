package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.file_slice;

/**
 * Represents a window of a file in a torrent.
 * <p>
 * The  refers to the index of the file (in the {@link com.frostwire.jlibtorrent.TorrentInfo}).
 * To get the path and filename, use {@link TorrentInfo#files()}. The  is the byte offset in the
 * file where the range starts, and  is the number of bytes this range is. The {@code size + offset}
 * will never be greater than the file size.
 * <p>
 * Implementation note: This class does not store internally a reference to the native swig
 * {@link com.frostwire.jlibtorrent.swig.file_slice}. This is because we are dealing with only three integral
 * values and we want to avoid keeping a reference to a memory in the native heap.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FileSlice {

    private final int fileIndex;
    private final long offset;
    private final long size;

    /**
     * @param fs the native object
     */
    public FileSlice(file_slice fs) {
        this.fileIndex = fs.getFile_index();
        this.offset = fs.getOffset();
        this.size = fs.getSize();
    }

    /**
     * The index of the file.
     *
     * @return the index
     */
    public int fileIndex() {
        return fileIndex;
    }

    /**
     * The offset from the start of the file, in bytes.
     *
     * @return the offset
     */
    public long offset() {
        return offset;
    }

    /**
     * The size of the window, in bytes.
     *
     * @return the size
     */
    public long size() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("FileSlice(fileIndex: %d, offset: %d, size: %d)", fileIndex, offset, size);
    }
}
