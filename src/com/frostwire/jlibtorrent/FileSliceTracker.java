package com.frostwire.jlibtorrent;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author gubatron
 * @author aldenml
 */
public final class FileSliceTracker {

    private final int fileIndex;

    private final SortedSlices sortedSlices;

    private boolean frozen;
    private FileSlice[] slices;
    private boolean[] complete;

    public FileSliceTracker(int fileIndex) {
        this.fileIndex = fileIndex;

        this.sortedSlices = new SortedSlices();
        this.frozen = false;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void addSlice(FileSlice slice) {
        if (frozen) {
            throw new InvalidStateException("Can't track slice if already frozen");
        }

        if (slice.getFileIndex() != fileIndex) {
            throw new IllegalArgumentException("Invalid file index");
        }

        sortedSlices.add(slice);
    }

    public void freeze() {
        frozen = true;
        slices = sortedSlices.toArray(new FileSlice[0]);
        complete = new boolean[slices.length];
    }

    public int getNumSlices() {
        checkFrozen();

        return slices.length;
    }

    public boolean isComplete(int index) {
        checkFrozen();

        return complete[index];
    }

    public void setComplete(int index, boolean complete) {
        checkFrozen();

        this.complete[index] = complete;
    }

    private void checkFrozen() {
        if (!frozen) {
            throw new InvalidStateException("Tracker needs to be frozen");
        }
    }

    private static final class SortedSlices extends PriorityQueue<FileSlice> {

        public SortedSlices() {
            super(new FileSliceComparator());
        }
    }

    private static final class FileSliceComparator implements Comparator<FileSlice> {

        @Override
        public int compare(FileSlice o1, FileSlice o2) {
            return Long.compare(o1.getOffset(), o2.getOffset());
        }
    }
}
