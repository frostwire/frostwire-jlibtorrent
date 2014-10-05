package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.FeedHandle;
import com.frostwire.jlibtorrent.FeedItem;
import com.frostwire.jlibtorrent.swig.rss_item_alert;

/**
 * This alert is posted every time a new RSS item (i.e. torrent) is received
 * from an RSS feed.
 * <p/>
 * It is only posted if the ``rss_notifications`` category is enabled in the
 * alert_mask.
 *
 * @author gubatron
 * @author aldenml
 */
public final class RssItemAlert extends AbstractAlert<rss_item_alert> {

    public RssItemAlert(rss_item_alert alert) {
        super(alert);
    }

    public FeedHandle getHandle() {
        return new FeedHandle(alert.getHandle());
    }

    public FeedItem getItem() {
        return new FeedItem(alert.getItem());
    }
}
