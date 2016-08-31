package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_connection_handle;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PeerConnectionHandle {

    private final peer_connection_handle pc;

    PeerConnectionHandle(peer_connection_handle pc) {
        this.pc = pc;
    }

    /**
     * @return
     */
    public peer_connection_handle swig() {
        return pc;
    }
}
