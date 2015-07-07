package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.anonymous_mode_alert;

/**
 * This alert is posted when a bittorrent feature is blocked because of the
 * anonymous mode. For instance, if the tracker proxy is not set up, no
 * trackers will be used, because trackers can only be used through proxies
 * when in anonymous mode.
 *
 * @author gubatron
 * @author aldenml
 */
public final class AnonymousModeAlert extends TorrentAlert<anonymous_mode_alert> {

    public AnonymousModeAlert(anonymous_mode_alert alert) {
        super(alert);
    }

    /**
     * specifies what error this is,  see kind_t.
     *
     * @return
     */
    public Kind getKind() {
        return Kind.fromSwig(alert.getKind());
    }

    public String getStr() {
        return alert.getStr();
    }

    public enum Kind {

        /**
         * means that there's no proxy set up for tracker
         * communication and the tracker will not be contacted.
         * The tracker which this failed for is specified in the ``str`` member.
         */
        TRACKER_NOT_ANONYMOUS(anonymous_mode_alert.kind_t.tracker_not_anonymous.swigValue()),

        /**
         *
         */
        UNKNOWN(-1);

        private Kind(int swigValue) {
            this.swigValue = swigValue;
        }

        private final int swigValue;

        public int getSwig() {
            return swigValue;
        }

        public static Kind fromSwig(int swigValue) {
            Kind[] enumValues = Kind.class.getEnumConstants();
            for (Kind ev : enumValues) {
                if (ev.getSwig() == swigValue) {
                    return ev;
                }
            }
            return UNKNOWN;
        }
    }
}
