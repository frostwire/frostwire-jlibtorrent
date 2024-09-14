package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.InfoHash;
import com.frostwire.jlibtorrent.swig.torrent_deleted_alert;

/**
 * This alert is generated when a request to delete the files of a torrent complete.
 * <p>
 * The ``info_hash`` is the info-hash of the torrent that was just deleted. Most of
 * the time the torrent_handle in the ``torrent_alert`` will be invalid by the time
 * this alert arrives, since the torrent is being deleted. The ``info_hash`` member
 * is hence the main way of identifying which torrent just completed the delete.
 * <p>
 * This alert is posted in the ``storage_notification`` category, and that bit
 * needs to be set in the alert_mask.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentDeletedAlert extends TorrentAlert<torrent_deleted_alert> {

    public TorrentDeletedAlert(torrent_deleted_alert alert) {
        super(alert);
    }

    @SuppressWarnings("unused")
    public InfoHash getInfoHashes() {
        return new InfoHash(alert.getInfo_hashes());
    }
}
