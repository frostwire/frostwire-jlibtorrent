package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.int_int_pair;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Pair<T1, T2> {

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     */
    public final T1 first;

    /**
     *
     */
    public final T2 second;

    /**
     * @return
     */
    public int_int_pair to_int_int_pair() {
        if (!Integer.class.equals(first.getClass()) || !Integer.class.equals(second.getClass())) {
            throw new IllegalArgumentException("Incompatible types");
        }

        return new int_int_pair((Integer) first, (Integer) second);
    }
}
