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

import com.frostwire.jlibtorrent.alerts.*;

/**
 * @author gubatron
 * @author aldenml
 */
public class TorrentAlertAdapter implements AlertListener {

    protected final TorrentHandle th;

    public TorrentAlertAdapter(TorrentHandle th) {
        this.th = th;
    }

    @Override
    public final boolean accept(Alert<?> alert) {
        if (!(alert instanceof TorrentAlert<?>)) {
            return false;
        }

        TorrentAlert<?> ta = (TorrentAlert<?>) alert;

        return ta.getSwig().getHandle().op_eq(th.getSwig());
    }

    @Override
    public void onAlert(Alert<?> alert) {
        AlertType type = alert.getType();
        switch (type) {
            case TORRENT_ADDED:
                onTorrentAdded((TorrentAddedAlert) alert);
                break;
            case TORRENT_FINISHED:
                onTorrentFinished((TorrentFinishedAlert) alert);
                break;
            case TORRENT_REMOVED:
                onTorrentRemoved((TorrentRemovedAlert) alert);
                break;
            case BLOCK_FINISHED:
                onBlockFinished((BlockFinishedAlert) alert);
                break;
            case METADATA_RECEIVED:
                onMetadataReceived((MetadataReceivedAlert) alert);
                break;
            case METADATA_FAILED:
                onMetadataFailed((MetadataFailedAlert) alert);
                break;
            case SAVE_RESUME_DATA:
                onSaveResumeData((SaveResumeDataAlert) alert);
                break;
            case FILE_ERROR:
                onFileError((FileErrorAlert) alert);
                break;
            case TRACKER_ANNOUNCE:
                onTrackerAnnounce((TrackerAnnounceAlert) alert);
                break;
            case READ_PIECE:
                onReadPiece((ReadPieceAlert) alert);
                break;
        }
    }

    public void onTorrentAdded(TorrentAddedAlert alert) {
    }

    public void onTorrentFinished(TorrentFinishedAlert alert) {
    }

    public void onTorrentRemoved(TorrentRemovedAlert alert) {
    }

    public void onBlockFinished(BlockFinishedAlert alert) {
    }

    public void onMetadataReceived(MetadataReceivedAlert alert) {
    }

    public void onMetadataFailed(MetadataFailedAlert alert) {
    }

    public void onSaveResumeData(SaveResumeDataAlert alert) {
    }

    public void onFileError(FileErrorAlert alert) {
    }

    public void onTrackerAnnounce(TrackerAnnounceAlert alert) {
    }

    public void onReadPiece(ReadPieceAlert alert) {
    }
}
