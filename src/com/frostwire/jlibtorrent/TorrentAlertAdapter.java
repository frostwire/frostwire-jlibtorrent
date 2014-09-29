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
    public void alert(Alert<?> alert) {
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
            case TORRENT_UPDATE:
                onTorrentUpdate((TorrentUpdateAlert) alert);
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
            case FILE_COMPLETED:
                onFileCompleted((FileCompletedAlert) alert);
                break;
            case FILE_RENAMED:
                onFileRenamed((FileRenamedAlert) alert);
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

    public void onTorrentUpdate(TorrentUpdateAlert alert) {
    }

    public void onBlockFinished(BlockFinishedAlert alert) {
    }

    public void onMetadataReceived(MetadataReceivedAlert alert) {
    }

    public void onMetadataFailed(MetadataFailedAlert alert) {
    }

    public void onSaveResumeData(SaveResumeDataAlert alert) {
    }

    public void onFileCompleted(FileCompletedAlert alert) {
    }

    public void onFileRenamed(FileRenamedAlert alert) {
    }

    public void onFileError(FileErrorAlert alert) {
    }

    public void onTrackerAnnounce(TrackerAnnounceAlert alert) {
    }

    public void onReadPiece(ReadPieceAlert alert) {
    }
}
