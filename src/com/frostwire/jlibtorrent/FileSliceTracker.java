package com.frostwire.jlibtorrent;

import java.util.TreeMap;

/**
 * @author gubatron
 * @author aldenml
 */
public final class FileSliceTracker {

    private final int fileIndex;

    private final TreeMap<Long, Pair<FileSlice, Boolean>> slices;

    public FileSliceTracker(int fileIndex) {
        this.fileIndex = fileIndex;

        this.slices = new TreeMap<Long, Pair<FileSlice, Boolean>>();
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void addSlice(FileSlice slice) throws IllegalArgumentException {
        if (slice.getFileIndex() != fileIndex) {
            throw new IllegalArgumentException("Invalid file index");
        }

        slices.put(slice.getOffset(), new Pair<FileSlice, Boolean>(slice, Boolean.FALSE));
    }

    public int getNumSlices() {
        return slices.size();
    }

    public boolean isComplete(long offset) {
        return Boolean.TRUE.equals(slices.get(offset).second);
    }

    public void setComplete(long offset, boolean complete) {
        Pair<FileSlice, Boolean> p = slices.get(offset);
        slices.put(offset, new Pair<FileSlice, Boolean>(p.first, Boolean.valueOf(complete)));
    }
}
