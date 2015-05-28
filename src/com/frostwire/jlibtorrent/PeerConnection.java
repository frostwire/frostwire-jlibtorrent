package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public class PeerConnection {

    private final peer_connection pc;

    public PeerConnection(peer_connection pc) {
        this.pc = pc;
    }

    public peer_connection getSwig() {
        return pc;
    }
}
