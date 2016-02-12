package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;

import java.util.Comparator;

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
        return COMPARATOR.compare(a1, a2);
    }

    public static final Comparator<Address> COMPARATOR = new Comparator<Address>() {
        @Override
        public int compare(Address o1, Address o2) {
            return address.compare(o1.addr, o2.addr);
        }
    };
}
