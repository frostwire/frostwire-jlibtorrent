package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.FileSlice;
import com.frostwire.jlibtorrent.FileSliceTracker;
import com.frostwire.jlibtorrent.TorrentInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PieceMap {


    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/ReSet_Resynthformation_SOSEP051_FrostClick_FrostWire_6_28_2014.torrent"};

        File torrentFile = new File(args[0]);

        TorrentInfo ti = new TorrentInfo(torrentFile);

        int numPieces = ti.getNumPieces();

        System.out.println("Num Pieces: " + numPieces);

        FileSliceTracker[] trackers = new FileSliceTracker[ti.getNumFiles()];

        for (int i = 0; i < numPieces; i++) {
            ArrayList<FileSlice> slices = ti.mapBlock(i, 0, ti.getPieceSize(i));
            for (FileSlice slice : slices) {
                int fileIndex = slice.getFileIndex();

                if (trackers[fileIndex] == null) {
                    trackers[fileIndex] = new FileSliceTracker(fileIndex);
                }

                trackers[fileIndex].addSlice(slice);
            }
        }

        int totalSlices = 0;

        for (FileSliceTracker tracker : trackers) {
            int numSlices = tracker.getNumSlices();
            System.out.println("File Index: " + tracker.getFileIndex() + ", slices: " + numSlices);
            totalSlices = totalSlices + numSlices;
        }

        System.out.println("Total Slices: " + totalSlices);
    }
}
