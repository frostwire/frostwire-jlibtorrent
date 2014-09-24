package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.peer_alert;

/**
 * The peer alert is a base class for alerts that refer to a specific peer. It includes all
 * the information to identify the peer. i.e. ``ip`` and ``peer-id``.
 *
 * @author gubatron
 * @author aldenml
 */
public class PeerAlert<T extends peer_alert> extends TorrentAlert<T> {

    public PeerAlert(T alert) {
        super(alert);
    }
}
