package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.swig_dht_storage;

/**
 * @author gubatron
 * @author aldenml
 */
final class SwigDhtStorage extends swig_dht_storage {

    private static final Logger LOG = Logger.getLogger(SwigDhtStorage.class);

    private final DhtStorage p;

    public SwigDhtStorage(DhtStorage p) {
        this.p = p;
    }

    @Override
    public void tick() {
        try {
            p.tick();
        } catch (Throwable e) {
            LOG.error("Error in plugin (tick)", e);
        }
    }
}
