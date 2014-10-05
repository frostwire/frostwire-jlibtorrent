package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.feed_item;

/**
 * represents one item from an RSS feed. Specifically a feed of torrents.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FeedItem {

    private final feed_item i;

    public FeedItem(feed_item i) {
        this.i = i;
    }

    public feed_item getSwig() {
        return i;
    }
}
