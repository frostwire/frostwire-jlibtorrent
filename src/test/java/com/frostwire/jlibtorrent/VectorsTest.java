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
        v.push_back((byte) 'A');
        v.push_back((byte) 'B');
        v.push_back((byte) 'C');
        assertEquals(Vectors.byte_vector2ascii(v), "ABC");

        v.clear();
        v.push_back((byte) 'A');
        v.push_back((byte) 'B');
        v.push_back((byte) 0);
        assertEquals(Vectors.byte_vector2ascii(v), "AB");

        v.clear();
        v.push_back((byte) 0);
        v.push_back((byte) 0);
        v.push_back((byte) 0);
        assertEquals(Vectors.byte_vector2ascii(v), "");

        v.clear();
        v.push_back((byte) 'A');
        v.push_back((byte) 0);
        v.push_back((byte) 'B');
        assertEquals(Vectors.byte_vector2ascii(v), "A");
    }
}
