package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.TimePoint;
import com.frostwire.jlibtorrent.swig.alert;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractAlert<T extends alert> implements Alert<T> {

    protected final T alert;

    public AbstractAlert(T alert) {
        this.alert = alert;
    }

    @Override
    public final T getSwig() {
        return alert;
    }

    /**
     * A timestamp is automatically created in the constructor.
     *
     * @return
     */
    @Override
    public TimePoint getTimestamp() {
        return new TimePoint(alert.timestamp());
    }

    // returns an integer that is unique to this alert type. It can be
    // compared against a specific alert by querying a static constant called ``alert_type``
    // in the alert. It can be used to determine the run-time type of an alert* in
    // order to cast to that alert type and access specific members.
    //
    // e.g:
    //
    // .. code:: c++
    //
    //	std::vector<alert*> alerts;
    //	ses.pop_alerts(&alerts);
    //	for (alert* i : alerts) {
    //		switch (a->type()) {
    //
    //			case read_piece_alert::alert_type:
    //			{
    //				read_piece_alert* p = (read_piece_alert*)a;
    //				if (p->ec) {
    //					// read_piece failed
    //					break;
    //				}
    //				// use p
    //				break;
    //			}
    //			case file_renamed_alert::alert_type:
    //			{
    //				// etc...
    //			}
    //		}
    //	}
    @Override
    public AlertType getType() {
        return AlertType.fromSwig(alert.type());
    }

    /**
     * Returns a string literal describing the type of the alert. It does
     * not include any information that might be bundled with the alert.
     *
     * @return
     */
    @Override
    public String getWhat() {
        return alert.what();
    }

    /**
     * Generate a string describing the alert and the information bundled
     * with it. This is mainly intended for debug and development use. It is not suitable
     * to use this for applications that may be localized. Instead, handle each alert
     * type individually and extract and render the information from the alert depending
     * on the locale.
     *
     * @return
     */
    @Override
    public String getMessage() {
        return alert.message();
    }

    /**
     * Returns a bitmask specifying which categories this alert belong to.
     *
     * @return
     */
    @Override
    public int getCategory() {
        return alert.category();
    }

    @Override
    public String toString() {
        return getType() + " - " + getWhat() + " - " + alert.message();
    }
}
