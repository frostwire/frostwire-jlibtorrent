package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.int_vector;

/**
 * @author gubatron
 * @author aldenml
 */
public enum Priority {

    /**
     * piece or file is not downloaded at all
     */
    IGNORE(0),

    /**
     * normal priority. Download order is dependent on availability
     */
    NORMAL(1),

    /**
     * higher than normal priority. Pieces are preferred over pieces with
     * the same availability, but not over pieces with lower availability
     */
    TWO(2),

    /**
     * pieces are as likely to be picked as partial pieces.
     */
    THREE(3),

    /**
     * pieces are preferred over partial pieces, but not over pieces with
     * lower availability
     */
    FOUR(4),

    /**
     * *currently the same as 4*
     */
    FIVE(5),

    /**
     * piece is as likely to be picked as any piece with availability 1
     */
    SIX(6),

    /**
     * maximum priority, availability is disregarded, the piece is
     * preferred over any other piece with lower priority
     */
    SEVEN(7);

    Priority(int swigValue) {
        this.swigValue = swigValue;
    }

    private final int swigValue;

    /**
     * @return the native value
     */
    public int swig() {
        return swigValue;
    }

    /**
     * @param swigValue the native value
     * @return the enum corresponding value
     */
    public static Priority fromSwig(int swigValue) {
        Priority[] enumValues = Priority.class.getEnumConstants();
        for (Priority ev : enumValues) {
            if (ev.swig() == swigValue) {
                return ev;
            }
        }
        throw new IllegalArgumentException("Invalid native value");
    }

    public static Priority[] array(Priority value, int size) {
        Priority[] arr = new Priority[size];

        for (int i = 0; i < size; i++) {
            arr[i] = value;
        }

        return arr;
    }

    static int_vector array2vector(Priority[] arr) {
        int_vector v = new int_vector();

        for (int i = 0; i < arr.length; i++) {
            Priority p = arr[i];
            v.push_back(p.swig());
        }

        return v;
    }

    static byte_vector array2byte_vector(Priority[] arr) {
        byte_vector v = new byte_vector();

        for (int i = 0; i < arr.length; i++) {
            Priority p = arr[i];
            v.push_back((byte) p.swig());
        }

        return v;
    }
}
