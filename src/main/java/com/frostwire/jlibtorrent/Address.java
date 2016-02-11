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

    public static final class Comparator implements java.util.Comparator<Address> {

        @Override
        public int compare(Address o1, Address o2) {
            address a1 = o1.addr;
            address a2 = o2.addr;
            if (a1.op_lt(a2)) {
                return -1;
            }
            if (a2.op_lt(a1)) {
                return 1;
            }

            return 0;
        }
    }
}
