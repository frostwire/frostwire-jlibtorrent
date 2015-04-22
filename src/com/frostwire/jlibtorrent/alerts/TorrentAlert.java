package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.torrent_alert;

/**
 * This is a base class for alerts that are associated with a
 * specific torrent. It contains a handle to the torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public abstract class TorrentAlert<T extends torrent_alert> extends AbstractAlert<T> {

    public TorrentAlert(T alert) {
        super(alert);
    }

    /**
     * The torrent_handle pointing to the torrent this
     * alert is associated with.
     *
     * @return
     */
    public TorrentHandle getHandle() {
        return new TorrentHandle(alert.getHandle());
    }

//    public String torrentName() {
//        return alert.torrent_name();
//    }
}
