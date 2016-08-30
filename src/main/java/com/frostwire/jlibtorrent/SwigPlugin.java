package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
class SwigPlugin extends swig_plugin {

    private final Plugin p;

    public SwigPlugin(Plugin p) {
        this.p = p;
    }

    @Override
    public boolean on_dht_request(string_view query, udp_endpoint source,
                                  bdecode_node message, entry response) {
        return p.onDhtRequest(query.to_string(), new UdpEndpoint(source),
                new BDecodeNode(message), new Entry(response));
    }
}
