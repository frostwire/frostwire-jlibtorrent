package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.metadata_received_alert;

/**
 * This alert is generated when the metadata has been completely received and the torrent
 * can start downloading. It is not generated on torrents that are started with metadata, but
 * only those that needs to download it from peers (when utilizing the libtorrent extension).
 * <p/>
 * There are no additional data members in this alert.
 * <p/>
 * Typically, when receiving this alert, you would want to save the torrent file in order
 * to load it back up again when the session is restarted.
 *
 * @author gubatron
 * @author aldenml
 */
public final class MetadataReceivedAlert extends TorrentAlert<metadata_received_alert> {

    public MetadataReceivedAlert(metadata_received_alert alert) {
        super(alert);
    }
}
