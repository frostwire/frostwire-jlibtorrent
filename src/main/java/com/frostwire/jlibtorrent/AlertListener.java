package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.alerts.Alert;

/**
 * @author gubatron
 * @author aldenml
 */
public interface AlertListener {

    /**
     * List of alert types filtered by this listener.
     * Return `null` if you intend to listen to all alerts.
     * TODO: Change to optional return type when Android supports Java 8.
     */
    public int[] types();

    public void alert(Alert<?> alert);
}
