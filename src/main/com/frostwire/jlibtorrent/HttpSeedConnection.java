package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.http_seed_connection;

/**
 * @author gubatron
 * @author aldenml
 */
public final class HttpSeedConnection extends PeerConnection<http_seed_connection> {

    public HttpSeedConnection(http_seed_connection pc) {
        super(pc);
    }
}
