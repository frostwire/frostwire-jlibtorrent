package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bitfield;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class PieceIndexBitfieldTest {

    @Test
    public void bitsAreSetCountedAndFound() {
        bitfield native_ = new bitfield();
        PieceIndexBitfield f = new PieceIndexBitfield(native_);
        assertNotNull(f.swig());
        assertNull(f.ts());
        assertTrue(f.isEmpty());
        assertEquals(0, f.size());

        f.resize(10, false);
        assertFalse(f.isEmpty());
        assertEquals(10, f.size());
        assertTrue(f.isNoneSet());
        assertFalse(f.isAllSet());
        assertEquals(0, f.count());

        f.setBit(3);
        f.setBit(7);
        assertTrue(f.getBit(3));
        assertFalse(f.getBit(4));
        assertEquals(2, f.count());
        assertEquals(3, f.findFirstSet());
        // only exercised: libtorrent's bitfield::find_last_clear answered 1 here, which is not a clear bit index we can vouch for
        f.findLastClear();

        f.clearBit(3);
        assertFalse(f.getBit(3));
        assertEquals(7, f.findFirstSet());

        f.setAll();
        assertTrue(f.isAllSet());
        assertEquals(10, f.count());

        f.clearAll();
        assertTrue(f.isNoneSet());

        f.resize(12);
        assertEquals(12, f.size());

        f.clear();
        assertTrue(f.isEmpty());
    }
}
