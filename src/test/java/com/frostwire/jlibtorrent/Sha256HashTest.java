package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.sha1_hash_vector;
import com.frostwire.jlibtorrent.swig.sha256_hash;
import com.frostwire.jlibtorrent.swig.sha256_hash_vector;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link Sha256Hash} and the parts of {@link Sha1Hash} that {@code Sha1HashTest} leaves out.
 *
 * @author gubatron
 * @author aldenml
 */
public class Sha256HashTest {

    private static final String HEX_256 = "00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff";
    private static final String HEX_160 = "00112233445566778899aabbccddeeff00112233";

    @Test
    public void sha256Basics() {
        Sha256Hash zero = new Sha256Hash();
        assertTrue(zero.isAllZeros());
        assertEquals(256, zero.countLeadingZeroes());
        assertNotNull(zero.swig());

        Sha256Hash h = new Sha256Hash(HEX_256);
        assertFalse(h.isAllZeros());
        assertEquals(HEX_256, h.toHex());
        assertEquals(HEX_256, h.toString());
        assertEquals(11, h.countLeadingZeroes());

        Sha256Hash copy = h.clone();
        assertEquals(h, copy);
        assertEquals(h.hashCode(), copy.hashCode());
        assertEquals(0, h.compareTo(copy));

        Sha256Hash fromBytes = new Sha256Hash(Hex.decode(HEX_256));
        assertEquals(h, fromBytes);

        copy.clear();
        assertTrue(copy.isAllZeros());
        assertNotEquals(h, copy);
        assertTrue(h.compareTo(copy) > 0);
        assertTrue(copy.compareTo(h) < 0);
        assertNotEquals(h, "text");
        assertNotEquals(h, null);
    }

    @Test
    public void sha256Extremes() {
        assertTrue(Sha256Hash.min().isAllZeros());
        assertEquals("ff", Sha256Hash.max().toHex().substring(0, 2));
        assertTrue(Sha256Hash.max().compareTo(Sha256Hash.min()) > 0);
    }

    @Test
    public void sha256RejectsWrongLength() {
        try {
            new Sha256Hash(new byte[31]);
            fail("32 bytes are required");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void sha256Vectors() {
        sha256_hash_vector v = new sha256_hash_vector();
        v.add(new Sha256Hash(HEX_256).swig());
        v.add(sha256_hash.min());
        ArrayList<Sha256Hash> l = Sha256Hash.convert(v);
        assertEquals(2, l.size());
        assertEquals(HEX_256, l.get(0).toHex());
        assertTrue(l.get(1).isAllZeros());
    }

    @Test
    public void sha1Basics() {
        Sha1Hash zero = new Sha1Hash();
        assertTrue(zero.isAllZeros());
        assertEquals(160, zero.countLeadingZeroes());

        Sha1Hash h = new Sha1Hash(HEX_160);
        assertEquals(HEX_160, h.toHex());
        assertEquals(11, h.countLeadingZeroes());

        Sha1Hash copy = h.clone();
        assertEquals(h, copy);
        assertEquals(0, h.compareTo(copy));

        copy.clear();
        assertTrue(copy.isAllZeros());
        assertNotEquals(h, copy);
        assertTrue(h.compareTo(copy) > 0);
        assertNotEquals(h, "text");
        assertNotEquals(h, null);

        assertEquals("ff", Sha1Hash.max().toHex().substring(0, 2));
        assertTrue(Sha1Hash.min().isAllZeros());
    }

    @Test
    public void sha1Vectors() {
        sha1_hash_vector v = new sha1_hash_vector();
        v.add(new Sha1Hash(HEX_160).swig());
        v.add(sha1_hash.min());
        ArrayList<Sha1Hash> l = Sha1Hash.convert(v);
        assertEquals(2, l.size());
        assertEquals(HEX_160, l.get(0).toHex());
        assertTrue(l.get(1).isAllZeros());
    }
}
