package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.int_vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author aldenml
 * @author gubatron
 */
public class PriorityTest {

    @Test
    public void testArray2vector() {
        Priority[] arr = Priority.array(Priority.FOUR, 10);
        int_vector vec = Priority.array2vector(arr);
        assertEquals(arr.length, vec.size());
    }
}
