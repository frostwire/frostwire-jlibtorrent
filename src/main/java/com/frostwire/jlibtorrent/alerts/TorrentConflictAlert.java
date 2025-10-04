package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.swig.torrent_conflict_alert;

/**
 * This alert is posted when two separate torrents (magnet links) resolve to
 * the same torrent, thus causing the same torrent being added twice. In
 * that case, both torrents enter an error state with ``duplicate_torrent``
 * as the error code. This alert is posted containing the metadata.
 * <p>
 * The torrent this alert originated from was the one that downloaded the
 * metadata (i.e. the `handle` member from the torrent_alert base class).
 *
 */
public final class TorrentConflictAlert extends TorrentAlert<torrent_conflict_alert> {

    TorrentConflictAlert(torrent_conflict_alert alert) {
        super(alert);
    }

    /**
     * The handle to the torrent in conflict. The swarm associated with this
     * torrent handle did not download the metadata, but the downloaded
     * metadata collided with this swarm's info-hash.
     */
    public TorrentHandle getConflictingTorrent() {
        return new TorrentHandle(alert.getConflicting_torrent());
    }

    /**
     * The metadata that was received by one of the torrents in conflict.
     * One way to resolve the conflict is to remove both failing torrents
     * and re-add it using this metadata.
     */
    public TorrentInfo getMetadata() {
        return new TorrentInfo(alert.get_metadata());
    }
}