/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_handle.status_flags_t;
import com.frostwire.jlibtorrent.swig.torrent_info;
import com.frostwire.jlibtorrent.swig.torrent_status;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentHandle {

    private static final long REQUEST_STATUS_RESOLUTION_MILLIS = 500;

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
        if (force || (now - lastStatusRequestTime) >= REQUEST_STATUS_RESOLUTION_MILLIS) {
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

    public boolean needSaveResumeData() {
        return th.need_save_resume_data();
    }

    public void saveResumeData() {
        // 2 - save_info_dict
        th.save_resume_data();
    }

    public String getDisplayName() {
        torrent_info ti = null;
        if (!th.is_valid() || (ti = th.torrent_file()) == null) {
            return "Unknown";
        }

        String name;

        if (ti.num_files() == 1) {
            name = ti.files().file_name(0);
        } else {
            name = ti.name();
        }

        return name;
    }

    public boolean isPartial() {
        if (!th.is_valid()) {
            return false;
        }

        int_vector v = th.file_priorities();

        long size = v.size();
        for (int i = 0; i < size; i++) {
            if (v.get(i) == 0) {
                return true;
            }
        }

        return false;
    }
}
