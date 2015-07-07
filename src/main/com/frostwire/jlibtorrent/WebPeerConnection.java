package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.web_peer_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public final class WebPeerConnection extends PeerConnection<web_peer_connection> {

    public WebPeerConnection(web_peer_connection pc) {
        super(pc);
    }
}
