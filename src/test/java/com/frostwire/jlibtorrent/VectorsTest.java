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

    /** frostwire/frostwire-jlibtorrent#338: the bulk copies keep every byte, at every size. */
    @Test
    public void bulkCopiesRoundTripEveryByteValue() {
        byte[][] cases = {new byte[0], new byte[]{-128}, new byte[256], new byte[1 << 20]};
        for (int i = 0; i < 256; i++) {
            cases[2][i] = (byte) i;
        }
        new java.util.Random(7).nextBytes(cases[3]);
        for (byte[] original : cases) {
            com.frostwire.jlibtorrent.swig.byte_vector v = Vectors.bytes2byte_vector(original);
            try {
                org.junit.Assert.assertEquals(original.length, v.size());
                org.junit.Assert.assertArrayEquals(original, Vectors.byte_vector2bytes(v));
            } finally {
                v.delete();
            }
        }
    }

    @Test
    public void copyIntoAShorterArrayStopsAtItsEnd() {
        com.frostwire.jlibtorrent.swig.byte_vector v = Vectors.bytes2byte_vector(new byte[]{1, 2, 3, 4});
        try {
            byte[] two = new byte[2];
            Vectors.byte_vector2bytes(v, two);
            org.junit.Assert.assertArrayEquals(new byte[]{1, 2}, two);
        } finally {
            v.delete();
        }
    }
}
