package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_array_32;
import com.frostwire.jlibtorrent.swig.byte_array_64;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.int64_vector;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.string_vector;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author gubatron
 * @author aldenml
 */
public class VectorsConversionTest {

    @Test
    public void bytesRoundTripThroughAByteVector() {
        byte[] data = {1, 2, 3, (byte) 0xff, 0};
        byte_vector v = Vectors.bytes2byte_vector(data);
        assertEquals(5, v.size());
        assertArrayEquals(data, Vectors.byte_vector2bytes(v));

        byte[] into = new byte[5];
        Vectors.byte_vector2bytes(v, into);
        assertArrayEquals(data, into);

        assertArrayEquals(data, Vectors.byte_vector2bytesAndDelete(v));
    }

    @Test
    public void bytesCanBeCopiedElementWiseIntoAnExistingVector() {
        byte_vector v = Vectors.new_byte_vector(3);
        assertEquals(3, v.size());
        Vectors.bytes2byte_vector(new byte[]{7, 8, 9}, v);
        assertArrayEquals(new byte[]{7, 8, 9}, Vectors.byte_vector2bytes(v));
    }

    @Test
    public void numbersAndStringsConvertToJavaArrays() {
        int_vector iv = new int_vector();
        iv.add(5);
        iv.add(-6);
        assertArrayEquals(new int[]{5, -6}, Vectors.int_vector2ints(iv));

        int64_vector lv = new int64_vector();
        lv.add(1L << 40);
        lv.add(-2L);
        assertArrayEquals(new long[]{1L << 40, -2L}, Vectors.int64_vector2longs(lv));

        string_vector sv = new string_vector();
        sv.add("a");
        sv.add("b");
        assertEquals(Arrays.asList("a", "b"), Vectors.string_vector2list(sv));
    }

    @Test
    public void stringsStopAtTheFirstNulByte() {
        byte_vector v = Vectors.bytes2byte_vector(new byte[]{'h', 'i', 0, 'x'});
        assertEquals("hi", Vectors.byte_vector2ascii(v));
        assertEquals("hi", Vectors.byte_vector2utf8(v));
        assertEquals("", Vectors.byte_vector2utf8(Vectors.bytes2byte_vector(new byte[]{0, 'x'})));
        assertEquals("", Vectors.byte_vector2utf8(new byte_vector()));
    }

    @Test
    public void stringsEncodeToByteVectors() {
        assertArrayEquals(new byte[]{'a', 'b'}, Vectors.byte_vector2bytes(Vectors.ascii2byte_vector("ab")));
        assertArrayEquals(new byte[]{(byte) 0xc3, (byte) 0xa9},
                Vectors.byte_vector2bytes(Vectors.string2byte_vector("é", "UTF-8")));
    }

    @Test
    public void unknownEncodingsAreWrapped() {
        try {
            Vectors.string2byte_vector("x", "no-such-charset");
            fail("unknown charset");
        } catch (RuntimeException e) {
            // UnsupportedEncodingException wrapped
        }
        try {
            Vectors.byte_vector2string(Vectors.bytes2byte_vector(new byte[]{'x'}), "no-such-charset");
            fail("unknown charset");
        } catch (RuntimeException e) {
            // UnsupportedEncodingException wrapped
        }
    }

    @Test
    public void fixedSizeArraysRoundTrip() {
        byte[] b32 = new byte[32];
        byte[] b64 = new byte[64];
        for (int i = 0; i < 64; i++) {
            b64[i] = (byte) (i + 1);
            if (i < 32) {
                b32[i] = (byte) (100 + i);
            }
        }
        byte_array_32 a32 = Vectors.bytes2byte_array_32(b32);
        assertArrayEquals(b32, Vectors.byte_array2bytes(a32));
        byte_array_64 a64 = Vectors.bytes2byte_array_64(b64);
        assertArrayEquals(b64, Vectors.byte_array2bytes(a64));
    }
}
