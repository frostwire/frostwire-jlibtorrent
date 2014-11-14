package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Address {

    private final address addr;

    public Address(address addr) {
        this.addr = addr;
    }

    public address getSwig() {
        return addr;
    }

    @Override
    public String toString() {
        return addr.to_string();
    }
}
