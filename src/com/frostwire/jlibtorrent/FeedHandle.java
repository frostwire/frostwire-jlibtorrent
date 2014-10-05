package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.feed_handle;

/**
 * The ``feed_handle`` refers to a specific RSS feed that is watched by the session.
 *
 * @author gubatron
 * @author aldenml
 */
public final class FeedHandle {

    private final feed_handle h;

    public FeedHandle(feed_handle h) {
        this.h = h;
    }

    public feed_handle getSwig() {
        return h;
    }
}
