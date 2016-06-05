package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha1_hash;

import java.util.Locale;

/**
 * @author gubatron
 * @author aldenml
 */
final class Hex {

    private Hex() {
    }

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    public static String convert(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] convert(String str) {
        int len = str.length();

        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid sha1 hex string");
        }

        str = str.toLowerCase(Locale.US);
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return data;
    }
}
