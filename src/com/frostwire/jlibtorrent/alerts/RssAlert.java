package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.FeedHandle;
import com.frostwire.jlibtorrent.swig.rss_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public final class RssAlert extends AbstractAlert<rss_alert> {

    public RssAlert(rss_alert alert) {
        super(alert);
    }

    /**
     * the handle to the feed which generated this alert.
     *
     * @return
     */
    public FeedHandle getHandle() {
        return new FeedHandle(alert.getHandle());
    }

    /**
     * a short cut to access the url of the feed, without
     * having to call feed_handle::get_settings().
     *
     * @return
     */
    public String getUrl() {
        return alert.getUrl();
    }

    /**
     * one of the values from rss_alert::state_t.
     *
     * @return
     */
    public State getState() {
        return State.fromSwig(alert.getState());
    }

    /**
     * an error code used for when an error occurs on the feed.
     *
     * @return
     */
    public ErrorCode getError() {
        return new ErrorCode(alert.getError());
    }

    public enum State {

        /**
         * An update of this feed was just initiated, it will either succeed
         * or fail soon.
         */
        STATE_UPDATING(rss_alert.state_t.state_updating.swigValue()),

        /**
         * The feed just completed a successful update, there may be new items
         * in it. If you're adding torrents manually, you may want to request
         * the feed status of the feed and look through the ``items`` vector.
         */
        STATE_UPDATED(rss_alert.state_t.state_updated.swigValue()),

        /**
         * An error just occurred. See the ``error`` field for information on
         * what went wrong.
         */
        STATE_ERROR(rss_alert.state_t.state_error.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private State(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static State fromSwig(int swigValue) {
            State[] enumValues = State.class.getEnumConstants();
            for (State ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
