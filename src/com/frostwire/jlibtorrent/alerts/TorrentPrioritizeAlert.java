package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.torrent_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentPrioritizeAlert extends TorrentAlert<torrent_alert> {

    private final TorrentHandle th;

    public TorrentPrioritizeAlert(TorrentHandle th) {
        super(null);
        this.th = th;
    }

    @Override
    public AlertType getType() {
        return AlertType.TORRENT_PRIORITIZE;
    }

    @Override
    public String getWhat() {
        return "";
    }

    @Override
    public int getCategory() {
        return com.frostwire.jlibtorrent.swig.alert.category_t.stats_notification.swigValue();
    }

    @Override
    public TorrentHandle getHandle() {
        return th;
    }
}
