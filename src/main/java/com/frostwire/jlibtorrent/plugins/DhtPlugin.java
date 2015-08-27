package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public interface DhtPlugin {

    boolean onMessage(udp_endpoint source, bdecode_node request, entry response);
}
