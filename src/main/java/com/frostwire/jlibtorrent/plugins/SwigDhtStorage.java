package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.swig_dht_storage;

/**
 * @author gubatron
 * @author aldenml
 */
final class SwigDhtStorage extends swig_dht_storage {

    private static final Logger LOG = Logger.getLogger(SwigDhtStorage.class);

    private final DhtStorage s;

    public SwigDhtStorage(DhtStorage s) {
        this.s = s;
    }

    @Override
    public void tick() {
        try {
            s.tick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (tick)", e);
        }
    }
}
