package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
public interface Plugin {

    boolean onDhtRequest(String query, UdpEndpoint source, BDecodeNode message, Entry response);
}
