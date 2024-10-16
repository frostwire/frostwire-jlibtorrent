package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created on 12/21/16.
 *
 * @author aldenml
 * @author gubatron
 */
public class IntSeriesTest {
    @Test
    public void addAndGetTest() {
        IntSeries series = new IntSeries(3);

        assertEquals("addAndGetTest 1", 0, series.size());

        debugAddElement(series, 1);
        assertEquals("addAndGetTest 2.a", 1, series.size());
        assertEquals("addAndGetTest 2.b", 1, series.get(0));

        debugAddElement(series, 2);
        assertEquals("addAndGetTest 3.a", 2, series.size());
        assertEquals("addAndGetTest 3.b", 1, series.get(0));
        assertEquals("addAndGetTest 3.c", 2, series.get(1));

        debugAddElement(series, 3);
        assertEquals("addAndGetTest 4.a", 3, series.size());
        assertEquals("addAndGetTest 4.b", 1, series.get(0));
        assertEquals("addAndGetTest 4.c", 2, series.get(1));
        assertEquals("addAndGetTest 4.d", 3, series.get(2));

        debugAddElement(series, 4);

        assertEquals("addAndGetTest 5.a", 2, series.get(0));
        assertEquals("addAndGetTest 5.b", 3, series.get(1));
        assertEquals("addAndGetTest 5.c", 4, series.get(2));

        debugAddElement(series, 5);

        assertEquals("addAndGetTest 6.a", 3, series.get(0));
        assertEquals("addAndGetTest 6.b", 4, series.get(1));
        assertEquals("addAndGetTest 6.c", 5, series.get(2));

        debugAddElement(series, 6);

        assertEquals("addAndGetTest 7.a", 4, series.get(0));
        assertEquals("addAndGetTest 7.b", 5, series.get(1));
        assertEquals("addAndGetTest 7.c", 6, series.get(2));

        debugAddElement(series, 7);

        assertEquals("addAndGetTest 8.a", 5, series.get(0));
        assertEquals("addAndGetTest 8.b", 6, series.get(1));
        assertEquals("addAndGetTest 8.c", 7, series.get(2));
    }

    private static void printArray(String name, int[] arr) {
        System.out.print(name + " [ ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i != arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }

    private static void debugAddElement(IntSeries circular, int elem) {
        //System.out.println(">>> Adding " + elem);
        circular.add(elem);
        //printArray("internal buffer", circular.getBufferCopy());
        //printArray("           tail", circular.tail(3));
        //System.out.println();
    }
}
