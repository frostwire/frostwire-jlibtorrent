package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.DhtSettings;
import com.frostwire.jlibtorrent.Sha1Hash;

/**
 * @author gubatron
 * @author aldenml
 */
public interface DhtStorageConstructor {

    DhtStorage create(Sha1Hash id, DhtSettings settings);
}
