package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.swig.peer_alert;

/**
 * The peer alert is a base class for alerts that refer to a specific peer. It includes all
 * the information to identify the peer. i.e. ``ip`` and ``peer-id``.
 *
 * @author gubatron
 * @author aldenml
 */
public class PeerAlert<T extends peer_alert> extends TorrentAlert<T> {

    PeerAlert(T alert) {
        super(alert);
    }

    /**
     * The peer's IP address and port.
     *
     * @return the endpoint
     */
    public TcpEndpoint endpoint() {
        return new TcpEndpoint(alert.getEndpoint());
    }

    /**
     * the peer ID, if known.
     *
     * @return the id
     */
    public Sha1Hash peerId() {
        return new Sha1Hash(alert.getPid());
    }
}
