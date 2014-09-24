package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.metadata_failed_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when the metadata has been completely received and the info-hash
// failed to match it. i.e. the metadata that was received was corrupt. libtorrent will
// automatically retry to fetch it in this case. This is only relevant when running a
// torrent-less download, with the metadata extension provided by libtorrent.
public final class MetadataFailedAlert extends TorrentAlert<metadata_failed_alert> {

    public MetadataFailedAlert(metadata_failed_alert alert) {
        super(alert);
    }
}
