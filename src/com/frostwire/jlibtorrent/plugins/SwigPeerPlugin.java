package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.swig.swig_peer_plugin;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigPeerPlugin extends swig_peer_plugin {

    private final PeerPlugin p;

    public SwigPeerPlugin(PeerPlugin p) {
        this.p = p;
    }
}
