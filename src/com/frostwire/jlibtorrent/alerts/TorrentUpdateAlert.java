package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.torrent_update_alert;

/**
 * When a torrent changes its info-hash, this alert is posted. This only happens in very
 * specific cases. For instance, when a torrent is downloaded from a URL, the true info
 * hash is not known immediately. First the .torrent file must be downloaded and parsed.
 * <p/>
 * Once this download completes, the ``torrent_update_alert`` is posted to notify the client
 * of the info-hash changing.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentUpdateAlert extends TorrentAlert<torrent_update_alert> {

    public TorrentUpdateAlert(torrent_update_alert alert) {
        super(alert);
    }

    /**
     * The previous info-hash for the torrent.
     *
     * @return
     */
    public Sha1Hash getOldInfoHash() {
        return new Sha1Hash(alert.getOld_ih());
    }

    /**
     * The new info-hash for the torrent.
     *
     * @return
     */
    public Sha1Hash getNewInfoHash() {
        return new Sha1Hash(alert.getNew_ih());
    }
}
