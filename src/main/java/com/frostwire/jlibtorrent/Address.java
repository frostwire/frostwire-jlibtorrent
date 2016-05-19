package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.error_code;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Address implements Comparable<Address> {

    private final address addr;

    /**
     * @param addr
     */
    public Address(address addr) {
        this.addr = addr;
    }

    /**
     * Create an address from an IPv4 address string in dotted decimal form,
     * or from an IPv6 address in hexadecimal notation.
     *
     * @param ip
     */
    public Address(String ip) {
        error_code ec = new error_code();
        this.addr = address.from_string(ip, ec);
        if (ec.value() != 0) {
            throw new IllegalArgumentException(ec.message());
        }
    }

    /**
     *
     */
    public Address() {
        this(new address());
    }

    /**
     * @return
     */
    public address swig() {
        return addr;
    }

    /**
     * Get whether the address is an IP version 4 address.
     *
     * @return
     */
    public boolean isV4() {
        return addr.is_v4();
    }

    /**
     * Get whether the address is an IP version 6 address.
     *
     * @return
     */
    public boolean isV6() {
        return addr.is_v6();
    }

    /**
     * Determine whether the address is a loopback address.
     *
     * @return
     */
    public boolean isLoopback() {
        return addr.is_loopback();
    }

    /**
     * Determine whether the address is unspecified.
     *
     * @return
     */
    public boolean isUnspecified() {
        return addr.is_unspecified();
    }

    /**
     * Determine whether the address is a multicast address.
     *
     * @return
     */
    public boolean isMulticast() {
        return addr.is_multicast();
    }

    /**
     * Compare addresses for ordering.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Address o) {
        return compare(this, o);
    }

    /**
     * Get the address as a string in dotted decimal format.
     *
     * @return
     */
    @Override
    public String toString() {
        return toString(addr);
    }

    /**
     * Compare addresses for ordering.
     *
     * @param a1
     * @param a2
     * @return
     */
    public static int compare(Address a1, Address a2) {
        return address.compare(a1.addr, a2.addr);
    }

    static String toString(address a) {
        error_code ec = new error_code();
        String s = a.to_string(ec);
        if (ec.value() != 0) {
            s = "<invalid address>";
        }
        return s;
    }
}
