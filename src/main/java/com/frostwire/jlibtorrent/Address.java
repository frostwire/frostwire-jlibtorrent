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

    public address swig() {
        return addr;
    }

    @Override
    public String toString() {
        return addr.to_string();
    }

    public static int compare(Address a1, Address a2) {
        return address.compare(a1.addr, a2.addr);
    }
}
