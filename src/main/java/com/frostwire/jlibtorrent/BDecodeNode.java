package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;

/**
 * @author gubatron
 * @author aldenml
 */
public final class BDecodeNode {

    private final bdecode_node n;

    /**
     * @param n the native object
     */
    public BDecodeNode(bdecode_node n) {
        this.n = n;
    }

    /**
     * @return the native object
     */
    public bdecode_node swig() {
        return n;
    }

    /**
     * A JSON style string representation
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return bdecode_node.to_string(n, false, 2);
    }
}
