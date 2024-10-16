package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.alert_category_t;

/**
 * @author gubatron
 * @author aldenml
 */
public abstract class AbstractAlert<T extends alert> implements Alert<T> {

    protected final T alert;
    private final AlertType type;

    AbstractAlert(T alert) {
        this.alert = alert;
        this.type = AlertType.fromSwig(alert.type());
    }

    @Override
    public final T swig() {
        return alert;
    }

    /**
     * A timestamp is automatically created in the constructor (in milliseconds).
     *
     * @return
     */
    @Override
    public long timestamp() {
        return alert.get_timestamp();
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
    public AlertType type() {
        return type;
    }

    /**
     * Returns a string literal describing the type of the alert. It does
     * not include any information that might be bundled with the alert.
     *
     * @return
     */
    @Override
    public String what() {
        return alert.what();
    }

    /**
     * Generate a string describing the alert and the information bundled
     * with it. This is mainly intended for debug and development use. It is not suitable
     * to use this for applications that may be localized. Instead, handle each alert
     * type individually and extract and render the information from the alert depending
     * on the locale.
     *
     * @return the alert message
     */
    @Override
    public String message() {
        return alert.message();
    }

    /**
     * Returns a bitmask specifying which categories this alert belong to.
     *
     * @return the alert category
     */
    @Override
    public alert_category_t category() {
        return alert.category();
    }

    @Override
    public String toString() {
        return type() + " - " + what() + " - " + message();
    }
}
