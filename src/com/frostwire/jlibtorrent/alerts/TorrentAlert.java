package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.torrent_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class TorrentAlert<T extends torrent_alert> extends AbstractAlert<T> {

    public TorrentAlert(T alert) {
        super(alert);
    }

    public TorrentHandle getHandle() {
        return new TorrentHandle(alert.getHandle());
    }
}
