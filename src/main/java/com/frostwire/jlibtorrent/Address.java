package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;

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

    @Override
    public String toString() {
        return addr.to_string();
    }

    public static int compare(Address a1, Address a2) {
        return address.compare(a1.addr, a2.addr);
    }
}
