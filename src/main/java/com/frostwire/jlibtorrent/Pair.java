package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair;

/**
 * Utility function to mirror the C++ std::pair class
 *
 * @author gubatron
 * @author aldenml
 */
public final class Pair<T1, T2> {

    /**
     * @param first  first element
     * @param second second element
     */
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * the first element
     */
    public final T1 first;

    /**
     * the second element
     */
    public final T2 second;

    /**
     * @return a native object
     */
    string_string_pair to_string_string_pair() {
        if (!String.class.equals(first.getClass()) || !String.class.equals(second.getClass())) {
            throw new IllegalArgumentException("Incompatible types");
        }

        return new string_string_pair((String) first, (String) second);
    }

    /**
     * @return a native object
     */
    string_int_pair to_string_int_pair() {
        if (!String.class.equals(first.getClass()) || !Integer.class.equals(second.getClass())) {
            throw new IllegalArgumentException("Incompatible types");
        }

        return new string_int_pair((String) first, (Integer) second);
    }
}
