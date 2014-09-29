package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * @author gubatron
 * @author aldenml
 */
public interface AlertListener {

    public boolean accept(Alert<?> alert);

    public void alert(Alert<?> alert);
}
