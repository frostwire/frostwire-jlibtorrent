package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created on 12/21/16.
 *
 * @author aldenml
 * @author gubatron
 */
public class CircularArrayTest {
    @Test
    public void addAndGetTest() {
        IntSeries circular = new IntSeries(3);

        assertEquals("addAndGetTest 1", 0, circular.size());

        debugAddElement(circular, 1);
        assertEquals("addAndGetTest 2.a", 1, circular.size());
        assertEquals("addAndGetTest 2.b", 1, circular.get(0));

        debugAddElement(circular, 2);
        assertEquals("addAndGetTest 3.a", 2, circular.size());
        assertEquals("addAndGetTest 3.b", 1, circular.get(0));
        assertEquals("addAndGetTest 3.c", 2, circular.get(1));

        debugAddElement(circular, 3);
        assertEquals("addAndGetTest 4.a", 3, circular.size());
        assertEquals("addAndGetTest 4.b", 1, circular.get(0));
        assertEquals("addAndGetTest 4.c", 2, circular.get(1));
        assertEquals("addAndGetTest 4.d", 3, circular.get(2));

        debugAddElement(circular, 4);

        assertEquals("addAndGetTest 5.a", 2, circular.get(0));
        assertEquals("addAndGetTest 5.b", 3, circular.get(1));
        assertEquals("addAndGetTest 5.c", 4, circular.get(2));

        debugAddElement(circular, 5);

        assertEquals("addAndGetTest 6.a", 3, circular.get(0));
        assertEquals("addAndGetTest 6.b", 4, circular.get(1));
        assertEquals("addAndGetTest 6.c", 5, circular.get(2));

        debugAddElement(circular, 6);

        assertEquals("addAndGetTest 7.a", 4, circular.get(0));
        assertEquals("addAndGetTest 7.b", 5, circular.get(1));
        assertEquals("addAndGetTest 7.c", 6, circular.get(2));

        debugAddElement(circular, 7);

        assertEquals("addAndGetTest 8.a", 5, circular.get(0));
        assertEquals("addAndGetTest 8.b", 6, circular.get(1));
        assertEquals("addAndGetTest 8.c", 7, circular.get(2));
    }

    @Test
    public void tailTest() {
        IntSeries circular = new IntSeries(3);
        circular.add(1);
        int[] tail = circular.tail(3);
        // [ 1 ]
        //printArray("tailTest 1's tail ->", tail);
        assertEquals("tailTest 1.a", 1, tail.length);
        assertEquals("tailTest 1.b", 1, tail[0]);
        circular.add(2);
        circular.add(3);
        tail = circular.tail(1);
        // [ 3 ]
        //printArray("tailTest 2's tail ->", tail);
        assertEquals("tailTest 2.a", 1, tail.length);
        assertEquals("tailTest 2.b", 3, tail[0]);
        tail = circular.tail(2);
        // [ 2, 3 ]
        //printArray("tailTest 3's tail ->", tail);
        assertEquals("tailTest 3.a", 2, tail.length);
        assertEquals("tailTest 3.b", 2, tail[0]);
        assertEquals("tailTest 3.c", 3, tail[1]);
        circular.add(4);
        tail = circular.tail(3);
        // [ 2, 3, 4 ]
        //printArray("tailTest 4's tail ->", tail);
        assertEquals("tailTest 4.a", 3, tail.length);
        assertEquals("tailTest 4.b", 2, tail[0]);
        assertEquals("tailTest 4.c", 3, tail[1]);
        assertEquals("tailTest 4.d", 4, tail[2]);
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
