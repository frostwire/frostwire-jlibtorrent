/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.char_vector;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.unsigned_char_vector;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LibTorrent {

    static {
        System.loadLibrary("jlibtorrent");
    }

    private LibTorrent() {
    }

    public static String version() {
        return libtorrent.LIBTORRENT_VERSION;
    }

    public static long time2millis(int time) {
        return ((long) time) * 1000;
    }

    public static byte[] vector2bytes(char_vector v) {
        int size = (int) v.size();
        byte[] arr = new byte[size];

        for (int i = 0; i < size; i++) {
            arr[i] = (byte) v.get(i);
        }

        return arr;
    }

    public static char_vector bytes2char_vector(byte[] arr) {
        char_vector v = new char_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add((char) arr[i]);
        }

        return v;
    }

    public static unsigned_char_vector bytes2unsigned_char_vector(byte[] arr) {
        unsigned_char_vector v = new unsigned_char_vector();

        for (int i = 0; i < arr.length; i++) {
            v.add((short) arr[i]);
        }

        return v;
    }
}
