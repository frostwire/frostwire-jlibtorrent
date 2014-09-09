package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_handle.status_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_status;

public final class TorrentHandle {

    private final torrent_handle th;

    private final TorrentInfo ti;

    public TorrentHandle(torrent_handle th) {
        this.th = th;

        this.ti = new TorrentInfo(this.th.torrent_file());
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

    public TorrentStatus getStatus() {
        return new TorrentStatus(th.status());
    }

    public String getSavePath() {
        torrent_status ts = th.status(status_flags_t.query_save_path.swigValue());
        return ts.getSave_path();
    }

    public void pause() {
        th.pause();
    }

    public void resume() {
        th.resume();
    }
}
