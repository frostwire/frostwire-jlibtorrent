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
     * @param addr the native object
     */
    public Address(address addr) {
        this.addr = addr;
    }

    /**
     * Create an address from an IPv4 address string in dotted decimal form,
     * or from an IPv6 address in hexadecimal notation.
     *
     * @param ip the ip string representation
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
     * @return native object
     */
    public address swig() {
        return addr;
    }

    /**
     * Get whether the address is an IP version 4 address.
     *
     * @return if it's an IPv4 address
     */
    public boolean isV4() {
        return addr.is_v4();
    }

    /**
     * Get whether the address is an IP version 6 address.
     *
     * @return if it's an IPv6 address
     */
    public boolean isV6() {
        return addr.is_v6();
    }

    /**
     * Determine whether the address is a loopback address.
     *
     * @return if it's a loopback address
     */
    public boolean isLoopback() {
        return addr.is_loopback();
    }

    /**
     * Determine whether the address is unspecified.
     *
     * @return if it's an unspecified address
     */
    public boolean isUnspecified() {
        return addr.is_unspecified();
    }

    /**
     * Determine whether the address is a multicast address.
     *
     * @return if it's an multicast address
     */
    public boolean isMulticast() {
        return addr.is_multicast();
    }

    /**
     * Compare addresses for ordering.
     *
     * @param o the other address
     * @return -1, 0 or 1
     */
    @Override
    public int compareTo(Address o) {
        return address.compare(this.addr, o.addr);
    }

    /**
     * Get the address as a string in dotted decimal format.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return toString(addr);
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
