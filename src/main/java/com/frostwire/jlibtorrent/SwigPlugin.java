package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.nio.charset.StandardCharsets;

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
        byte_vector v = query.to_bytes();
        byte[] arr = Vectors.byte_vector2bytes(v);
        String s = new String(arr, StandardCharsets.US_ASCII);
        return p.onDhtRequest(s, new UdpEndpoint(source),
                new BDecodeNode(message), new Entry(response));
    }
}
