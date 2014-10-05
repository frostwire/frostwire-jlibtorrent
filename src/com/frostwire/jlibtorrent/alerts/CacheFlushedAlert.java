package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.cache_flushed_alert;

/**
 * This alert is posted when the disk cache has been flushed for a specific
 * torrent as a result of a call to torrent_handle::flush_cache(). This
 * alert belongs to the ``storage_notification`` category, which must be
 * enabled to let this alert through. The alert is also posted when removing
 * a torrent from the session, once the outstanding cache flush is complete
 * and the torrent does no longer have any files open.
 *
 * @author gubatron
 * @author aldenml
 */
public final class CacheFlushedAlert extends TorrentAlert<cache_flushed_alert> {

    public CacheFlushedAlert(cache_flushed_alert alert) {
        super(alert);
    }
}
