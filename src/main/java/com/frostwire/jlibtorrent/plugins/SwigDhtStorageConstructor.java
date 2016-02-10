package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.DhtSettings;
import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.dht_settings;
import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.swig_dht_storage;
import com.frostwire.jlibtorrent.swig.swig_dht_storage_constructor;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigDhtStorageConstructor extends swig_dht_storage_constructor {

    private static final Logger LOG = Logger.getLogger(SwigDhtStorageConstructor.class);

    private final DhtStorageConstructor sc;

    private SwigDhtStorage s;

    public SwigDhtStorageConstructor(DhtStorageConstructor sc) {
        this.sc = sc;
    }

    @Override
    public swig_dht_storage create(sha1_hash id, dht_settings settings) {
        DhtStorage p = sc.create(new Sha1Hash(id), new DhtSettings(settings));
        s = new SwigDhtStorage(p);
        return s;
    }
}
