package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Address {

    private final address addr;

    public Address(address addr) {
        this.addr = addr;
    }

    public Address(String ip) {
        error_code ec = new error_code();
        this.addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
    }

    public address swig() {
        return addr;
    }

    public boolean isV4() {
        return addr.is_v4();
    }

    public boolean isV6() {
        return addr.is_v6();
    }

    public address_v4 toV4() {
        return addr.to_v4();
    }

    public address_v6 toV6() {
        return addr.to_v6();
    }

    public boolean isLoopback() {
        return addr.is_loopback();
    }

    public boolean isUnspecified() {
        return addr.is_unspecified();
    }

    public boolean isMulticast() {
        return addr.is_multicast();
    }

    @Override
    public String toString() {
        return toString(addr);
    }

    public static int compare(Address a1, Address a2) {
        return address.compare(a1.addr, a2.addr);
    }

    static String toString(address a) {
        error_code ec = new error_code();
        String s = a.to_string(ec);
        if (ec.value() != 0) {
            s = "invalid";
        }
        return s;
    }
}
