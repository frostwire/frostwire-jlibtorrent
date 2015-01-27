package com.frostwire.jlibtorrent;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author gubatron
 * @author aldenml
 */
public final class FileSliceTracker {

    private final int fileIndex;
    private final TreeMap<Integer, Pair<FileSlice, Boolean>> slices;

    public FileSliceTracker(int fileIndex) {
        this.fileIndex = fileIndex;
        this.slices = new TreeMap<Integer, Pair<FileSlice, Boolean>>();
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public long getSequentialDownloaded() {
        Iterator<Pair<FileSlice, Boolean>> it = slices.values().iterator();

        long downloaded = 0;
        boolean done = false;

        while (!done && it.hasNext()) {
            Pair<FileSlice, Boolean> p = it.next();

            if (Boolean.TRUE.equals(p.second)) { // complete
                downloaded = downloaded + p.first.getSize();
            } else {
                done = true;
            }
        }

        return downloaded;
    }

    public void addSlice(int pieceIndex, FileSlice slice) throws IllegalArgumentException {
        if (slice.getFileIndex() != fileIndex) {
            throw new IllegalArgumentException("Invalid file index");
        }

        slices.put(pieceIndex, new Pair<FileSlice, Boolean>(slice, Boolean.FALSE));
    }

    public int getNumSlices() {
        return slices.size();
    }

    public int[] getPieces() {
        Integer[] arr = slices.keySet().toArray(new Integer[0]);
        int[] r = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }

        return r;
    }

    public boolean isComplete(int pieceIndex) throws IllegalArgumentException {
        Pair<FileSlice, Boolean> p = slices.get(pieceIndex);
        if (p == null) {
            throw new IllegalArgumentException("piece index is not contained in the internal structure");
        }
        return Boolean.TRUE.equals(p.second);
    }

    public void setComplete(int pieceIndex, boolean complete) throws IllegalArgumentException {
        Pair<FileSlice, Boolean> p = slices.get(pieceIndex);
        if (p == null) {
            throw new IllegalArgumentException("piece index is not contained in the internal structure");
        }
        slices.put(pieceIndex, new Pair<FileSlice, Boolean>(p.first, Boolean.valueOf(complete)));
    }
}
