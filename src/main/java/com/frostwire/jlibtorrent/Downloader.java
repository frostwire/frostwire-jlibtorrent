package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.AlertType;

import java.io.File;

/**
 * This class provides a lens only functionality.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Downloader {

    private static final Logger LOG = Logger.getLogger(Downloader.class);

    private static final int[] LISTENER_TYPES = new int[]{AlertType.METADATA_RECEIVED.getSwig()};

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
                if (ti.numFiles() != priorities.length) {
                    throw new IllegalArgumentException("The priorities length should be equals to the number of files");
                }

                th.prioritizeFiles(priorities);
            } else {
                // did they just add the entire torrent (therefore not selecting any priorities)
                final Priority[] wholeTorrentPriorities = Priority.array(Priority.NORMAL, ti.numFiles());
                th.prioritizeFiles(wholeTorrentPriorities);
            }
        } else { // new download
            s.asyncAddTorrent(ti, saveDir, priorities, resumeFile);
        }
    }

    public void download(TorrentInfo ti, File saveDir) {
        download(ti, saveDir, null, null);
    }

    /**
     * @param uri
     * @param timeout in seconds
     * @return
     */
    public byte[] fetchMagnet(String uri, int timeout) {
        return s.fetchMagnet(uri, timeout);
    }
}
