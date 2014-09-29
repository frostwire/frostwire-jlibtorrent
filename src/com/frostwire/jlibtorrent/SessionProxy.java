package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_proxy;

/**
 * This is a holder for the internal session implementation object. Once the
 * session destruction is explicitly initiated, this holder is used to
 * synchronize the completion of the shutdown. The lifetime of this object
 * may outlive session, causing the session destructor to not block. The
 * session_proxy destructor will block however, until the underlying session
 * is done shutting down.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SessionProxy {

    private final session_proxy sp;

    public SessionProxy(session_proxy sp) {
        this.sp = sp;
    }

    public session_proxy getSwig() {
        return sp;
    }
}
