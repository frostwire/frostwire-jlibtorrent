package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Vectors {

    private Vectors() {
    }

    public static byte[] byte_vector2bytes(byte_vector v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static void byte_vector2bytes(byte_vector v, byte[] arr) {
        int size = (int) v.size();

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }
    }

    public static byte_vector bytes2byte_vector(byte[] arr) {
        byte_vector v = new byte_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add(arr[i]);
        }

        return v;
    }

    public static void bytes2byte_vector(byte[] arr, byte_vector v) {
        for (int i = 0; i < arr.length; i++) {
            v.set(i, arr[i]);
        }
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

    public static byte[] byte_span2bytes(byte_const_span v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte_vector new_byte_vector(int size) {
        byte_vector v = new byte_vector();
        byte z = (byte) 0;
        for (int i = 0; i < size; i++) {
            v.add(z);
        }

        return v;
    }

    public static List<String> string_vector2list(string_vector v) {
        int size = (int) v.size();
        ArrayList<String> l = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            l.add(v.get(i));
        }

        return l;
    }

    public static String byte_vector2string(byte_vector v, String encoding) {
        byte[] arr = Vectors.byte_vector2bytes(v);

        int n = 0;
        for (; n < arr.length && arr[n] != 0; ) {
            n++;
        }

        if (n == 0) {
            return "";
        }

        try {
            return new String(arr, 0, n, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String byte_vector2ascii(byte_vector v) {
        return byte_vector2string(v, "US-ASCII");
    }

    public static String byte_vector2utf8(byte_vector v) {
        return byte_vector2string(v, "UTF-8");
    }

    public static byte_vector string2byte_vector(String s, String encoding) {
        try {
            byte[] arr = s.getBytes(encoding);
            return bytes2byte_vector(arr);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte_vector ascii2byte_vector(String s) {
        return string2byte_vector(s, "US-ASCII");
    }

    public static byte_array_32 bytes2byte_array_32(byte[] arr) {
        byte_array_32 v = new byte_array_32();

        for (int i = 0; i < 32; i++) {
            v.set(i, arr[i]);
        }

        return v;
    }

    public static byte[] byte_array2bytes(byte_array_32 v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte[] byte_array2bytes(byte_array_64 v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }

        return arr;
    }

    public static byte_array_64 bytes2byte_array_64(byte[] arr) {
        byte_array_64 v = new byte_array_64();

        for (int i = 0; i < 64; i++) {
            v.set(i, arr[i]);
        }

        return v;
    }
}
