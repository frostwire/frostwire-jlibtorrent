package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_handle.status_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_status;

import java.io.File;

public final class TorrentHandle {

    private static final long REQUEST_STATUS_RESOLUTION = 500;

    private final torrent_handle th;
    private final File torrentFile;

    private final TorrentInfo ti;
    private final String infoHash;

    private long lastStatusRequestTime;
    private TorrentStatus lastStatus;

    TorrentHandle(torrent_handle th, File torrentFile) {
        this.th = th;
        this.torrentFile = torrentFile;

        this.ti = new TorrentInfo(this.th.torrent_file());
        this.infoHash = this.ti.getInfoHash();
    }

    public torrent_handle getSwig() {
        return th;
    }

    /**
     * This could be null in case the torrent is built from a magnet
     *
     * @return
     */
    public File getTorrentFile() {
        return torrentFile;
    }

    public TorrentInfo getTorrentInfo() {
        return ti;
    }

    public boolean isPaused() {
        return th.status().getPaused();
    }

    public boolean isSeeding() {
        return th.status().getIs_seeding();
    }

    public boolean isFinished() {
        return th.status().getIs_finished();
    }

    /**
     * It is important not to call this method for each field in the status
     * for performance reasons.
     *
     * @return
     */
    public TorrentStatus getStatus(boolean force) {
        long now = System.currentTimeMillis();
        if (force || (now - lastStatusRequestTime) >= REQUEST_STATUS_RESOLUTION) {
            lastStatusRequestTime = now;
            lastStatus = new TorrentStatus(th.status());
        }

        return lastStatus;
    }

    public TorrentStatus getStatus() {
        return this.getStatus(false);
    }

    public String getSavePath() {
        torrent_status ts = th.status(status_flags_t.query_save_path.swigValue());
        return ts.getSave_path();
    }

    public String getInfoHash() {
        return infoHash;
    }

    public void pause() {
        th.pause();
    }

    public void resume() {
        th.resume();
    }
}
