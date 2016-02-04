package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Vectors {

    private Vectors() {
    }

    public static byte[] char_vector2bytes(char_vector v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = (byte) v.get(i);
        }

        return arr;
    }

    public static void byte_vector2bytes(byte_vector v, byte[] arr) {
        int size = (int) v.size();

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }
    }

    public static void char_vector2bytes(char_vector v, byte[] arr) {
        int size = (int) v.size();

        for (int i = 0; i < size; i++) {
            arr[i] = (byte) v.get(i);
        }
    }

    public static String char_vector2string(char_vector v, int size) {
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            sb.append(v.get(i));
        }

        return sb.toString();
    }

    public static char_vector string2char_vector(String s) {
        char_vector v = new char_vector();

        for (int i = 0; i < s.length(); i++) {
            v.add(s.charAt(i));
        }

        return v;
    }

    public static char_vector bytes2char_vector(byte[] arr) {
        char_vector v = new char_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add((char) arr[i]);
        }

        return v;
    }

    public static byte_vector bytes2byte_vector(byte[] arr) {
        byte_vector v = new byte_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add(arr[i]);
        }

        return v;
    }

    public static int_vector ints2int_vector(int[] arr) {
        int_vector v = new int_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add(arr[i]);
        }

        return v;
    }

    public static int[] int_vector2ints(int_vector v) {
        int size = (int) v.size();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static long[] int64_vector2longs(int64_vector v) {
        int size = (int) v.size();
        long[] arr = new long[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static unsigned_char_vector new_unsigned_char_vector(int size) {
        unsigned_char_vector v = new unsigned_char_vector();
        for (int i = 0; i < size; i++) {
            v.add((short) 0);
        }

        return v;
    }

    public static char_vector new_char_vector(int size) {
        char_vector v = new char_vector();
        for (int i = 0; i < size; i++) {
            v.add((char) 0);
        }

        return v;
    }

    public static byte_vector new_byte_vector(int size) {
        byte_vector v = new byte_vector();
        byte z = (byte) 0;
        for (int i = 0; i < size; i++) {
            v.add(z);
        }

        return v;
    }

    public static unsigned_char_vector priorities2unsigned_char_vector(Priority[] arr) {
        unsigned_char_vector v = new unsigned_char_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add((short) arr[i].getSwig());
        }

        return v;
    }
}
