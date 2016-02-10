package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.plugins.DhtStorageConstructor;
import com.frostwire.jlibtorrent.plugins.SwigDhtStorageConstructor;
import com.frostwire.jlibtorrent.swig.session_handle;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionHandle {

    protected final session_handle s;

    private SwigDhtStorageConstructor dhtSC;

    public SessionHandle(session_handle s) {
        this.s = s;
    }

    public session_handle getSwig() {
        return s;
    }

    public void setDhtStorage(DhtStorageConstructor sc) {
        dhtSC = new SwigDhtStorageConstructor(sc);
        s.set_swig_dht_storage(dhtSC);
    }
}
