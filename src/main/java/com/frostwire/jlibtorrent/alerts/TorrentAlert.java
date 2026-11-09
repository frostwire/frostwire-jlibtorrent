package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.swig.torrent_alert;
import com.frostwire.jlibtorrent.swig.torrent_handle;

/**
 * This is a base class for alerts that are associated with a
 * specific torrent. It contains a handle to the torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public class TorrentAlert<T extends torrent_alert> extends AbstractAlert<T> {

    TorrentAlert(T alert) {
        super(alert);
    }

    /**
     * The torrent_handle pointing to the torrent this
     * alert is associated with.
     *
     * @return
     */
    public TorrentHandle handle() {
        return new TorrentHandle(alert.getHandle());
    }

    /**
     * The name of the torrent. libtorrent 2.1 removed the name it kept in every torrent alert, so
     * this asks the session ({@code torrent_handle::status(query_name)}): a blocking call, and
     * empty once the torrent has been removed.
     *
     * @return the name of the torrent download, or "" if the handle is no longer valid
     * @deprecated use {@code handle().status()} or your own copy of the name
     */
    @Deprecated
    public String torrentName() {
        torrent_handle h = alert.getHandle();
        if (h == null || !h.is_valid()) {
            return "";
        }
        try {
            return h.status(torrent_handle.query_name).getName();
        } catch (RuntimeException e) {
            return "";
        }
    }
}
