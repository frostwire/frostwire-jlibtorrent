package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Address {

    public static final Comparator COMPARATOR = new Comparator();

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

    /**
     *
     */
    public static final class Comparator implements java.util.Comparator<Address> {

        private Comparator() {
        }

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
