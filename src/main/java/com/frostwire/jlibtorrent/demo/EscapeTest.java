package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.Utils;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EscapeTest {

    public static void main(String[] args) throws Throwable {
        byte[] d1 = new byte[]{1, 2, 3, 4, 5};
        System.out.println(Utils.toHex(d1));

        byte[] d2 = new byte[]{Byte.valueOf("01", 16), Byte.valueOf("02", 16)};
        System.out.println(Utils.toHex(d2));
    }
}
