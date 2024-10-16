/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 *
 * Licensed under the MIT License.
 */

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author aldenml
 * @author gubatron
 */
public class VectorsTest {

    @Test
    public void testByteVector2String() {
        byte_vector v = new byte_vector();
        v.add((byte) 'A');
        v.add((byte) 'B');
        v.add((byte) 'C');
        assertEquals(Vectors.byte_vector2ascii(v), "ABC");

        v.clear();
        v.add((byte) 'A');
        v.add((byte) 'B');
        v.add((byte) 0);
        assertEquals(Vectors.byte_vector2ascii(v), "AB");

        v.clear();
        v.add((byte) 0);
        v.add((byte) 0);
        v.add((byte) 0);
        assertEquals(Vectors.byte_vector2ascii(v), "");

        v.clear();
        v.add((byte) 'A');
        v.add((byte) 0);
        v.add((byte) 'B');
        assertEquals(Vectors.byte_vector2ascii(v), "A");

        v.clear();
        v.add((byte) 194);
        v.add((byte) 181);
        assertEquals(Vectors.byte_vector2utf8(v), "µ");
    }
}
