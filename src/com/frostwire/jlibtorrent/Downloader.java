package com.frostwire.jlibtorrent;

import java.io.File;

/**
 * This class provides a lens only functionality.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Downloader {

    private final Session s;

    public Downloader(Session s) {
        this.s = s;
    }

    public TorrentHandle find(Sha1Hash infoHash) {
        return s.findTorrent(infoHash);
    }

    public TorrentHandle find(String infoHash) {
        return s.findTorrent(new Sha1Hash(infoHash));
    }

    public void download(TorrentInfo ti, File saveDir, Priority[] priorities, File resumeFile) {
        TorrentHandle th = s.findTorrent(ti.getInfoHash());

        if (th != null) {
            // found a download with the same hash, just adjust the priorities if needed
            if (priorities != null) {
                if (ti.getNumFiles() != priorities.length) {
                    throw new IllegalArgumentException("The priorities length should be equals to the number of files");
                }

                th.prioritizeFiles(priorities);
            }
        } else { // new download
            s.asyncAddTorrent(ti, saveDir, priorities, resumeFile);
        }
    }

    public void download(File torrent, File saveDir, boolean[] selection) {
        Priority[] priorities = null;

        if (selection != null) {
            priorities = Priority.array(Priority.IGNORE, selection.length);

            for (int i = 0; i < priorities.length; i++) {
                if (selection[i]) {
                    priorities[i] = Priority.NORMAL;
                }
            }
        }

        download(new TorrentInfo(torrent), saveDir, priorities, null);
    }
}
