package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Alert<T extends alert> {

    public T getSwig();

    public AlertType getType();

    public String getWhat();

    public int getCategory();
}
