package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link IntSeries} argument checks, its textual form and {@code last()} away from the wrap-around slot.
 *
 * @author gubatron
 * @author aldenml
 */
public class IntSeriesBoundsTest {

    @Test
    public void bufferMustExistAndHoldSomething() {
        try {
            new IntSeries((long[]) null);
            fail("null buffer");
        } catch (IllegalArgumentException expected) {
            assertEquals("buffer to hold data can't be null", expected.getMessage());
        }
        try {
            new IntSeries(new long[0]);
            fail("empty buffer");
        } catch (IllegalArgumentException expected) {
            assertEquals("buffer to hold data can't be of size 0", expected.getMessage());
        }
    }

    @Test
    public void lastIsTheMostRecentValue() {
        IntSeries s = new IntSeries(4);
        s.add(10);
        s.add(20);
        assertEquals(20, s.last());
        s.add(30);
        assertEquals(30, s.last());
        assertEquals(3, s.size());
    }

    @Test
    public void toStringDescribesTheRing() {
        IntSeries s = new IntSeries(3);
        assertTrue(s.toString(), s.toString().startsWith("{ head: -1, end: -1, size: 0, buffer: [ 0, 0, 0 ] }"));
        s.add(7);
        s.add(8);
        assertEquals("{ head: 0, end: 1, size: 2, buffer: [ 7, 8, 0 ] }", s.toString());
        s.add(9);
        s.add(10);
        assertEquals("{ head: 1, end: 0, size: 3, buffer: [ 10, 8, 9 ] }", s.toString());
    }

    @Test
    public void lastIsCorrectWhenTheNewestValueSitsAtIndexZero() {
        IntSeries one = new IntSeries(3);
        one.add(1);
        assertEquals(1, one.last());

        IntSeries s = new IntSeries(3);
        for (int v = 1; v <= 9; v++) {
            s.add(v);
            assertEquals(v, s.last());
        }
        assertEquals(3, s.size());
        assertEquals(7, s.get(0));
        assertEquals(9, s.get(2));
    }

    @Test
    public void lastOfAnEmptySeriesIsZero() {
        assertEquals(0, new IntSeries(3).last());
    }
}
