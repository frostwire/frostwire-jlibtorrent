package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bitfield;

/**
 * The bitfiled type stores any number of bits as a bitfield
 * in a heap allocated or borrowed array.
 *
 * @author gubatron
 * @author aldenml
 */
public final class Bitfield {

    private final bitfield bf;

    public Bitfield(bitfield bf) {
        this.bf = bf;
    }

    public bitfield getSwig() {
        return bf;
    }
}
