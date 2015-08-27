package com.frostwire.jlibtorrent.plugins;

import com.frostwire.jlibtorrent.Logger;
import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.dht_extension_handler_listener;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.udp_endpoint;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SwigDhtPlugin extends dht_extension_handler_listener {

    private static final Logger LOG = Logger.getLogger(SwigDhtPlugin.class);

    private final DhtPlugin p;

    public SwigDhtPlugin(DhtPlugin p) {
        this.p = p;
    }

    @Override
    public boolean on_message(udp_endpoint source, bdecode_node request, entry response) {
        try {
            p.onMessage(source, request, response);
        } catch (Throwable e) {
            LOG.error("Error in plugin (on_piece_pass)", e);
        }

        return false;
    }
}
