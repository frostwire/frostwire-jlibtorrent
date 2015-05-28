package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bt_peer_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public final class BtPeerConnection extends PeerConnection<bt_peer_connection> {

    public BtPeerConnection(bt_peer_connection pc) {
        super(pc);
    }
}
