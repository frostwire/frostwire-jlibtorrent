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

    @Override
    public TimePoint getTimestamp() {
        return new TimePoint(alert.timestamp());
    }

    @Override
    public AlertType getType() {
        return AlertType.fromSwig(alert.type());
    }

    @Override
    public String getWhat() {
        return alert.what();
    }

    @Override
    public String getMessage() {
        return alert.message();
    }

    @Override
    public int getCategory() {
        return alert.category();
    }

    @Override
    public boolean isDiscardable() {
        return alert.discardable();
    }

    @Override
    public String toString() {
        return getType() + " - " + getWhat() + " - " + alert.message();
    }
}
