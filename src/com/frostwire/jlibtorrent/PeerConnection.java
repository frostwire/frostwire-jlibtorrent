package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.peer_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public class PeerConnection<T extends peer_connection> {

    private final T pc;

    public PeerConnection(T pc) {
        this.pc = pc;
    }

    public T getSwig() {
        return pc;
    }
}
