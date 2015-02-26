package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.PTime;
import com.frostwire.jlibtorrent.swig.alert;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Alert<T extends alert> {

    public T getSwig();

    /**
     * A timestamp is automatically created in the constructor.
     *
     * @return
     */
    public PTime getTimestamp();

    public AlertType getType();

    public String getWhat();

    public String getMessage();

    public int getCategory();

    public boolean isDiscardable();
}
