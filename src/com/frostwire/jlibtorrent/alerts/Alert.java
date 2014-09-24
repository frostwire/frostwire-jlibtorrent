package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Alert<T extends alert> {

    public T getSwig();

    public AlertType getType();

    public int getCategory();

    public String getWhat();
}
