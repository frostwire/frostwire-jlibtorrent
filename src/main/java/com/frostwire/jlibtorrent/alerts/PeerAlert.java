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
public abstract class PeerAlert<T extends peer_alert> extends TorrentAlert<T> {

    public PeerAlert(T alert) {
        super(alert);
    }

    /**
     * The peer's IP address and port.
     *
     * @return
     */
    public TcpEndpoint getPeerIP() {
        return new TcpEndpoint(alert.getIp());
    }

    /**
     * the peer ID, if known.
     *
     * @return
     */
    public Sha1Hash getPeerId() {
        return new Sha1Hash(alert.getPid());
    }
}
