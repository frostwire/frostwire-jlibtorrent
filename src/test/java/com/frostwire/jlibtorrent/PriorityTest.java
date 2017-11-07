package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.download_priority_vector;
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
        download_priority_vector vec = Priority.array2vector(arr);
        assertEquals(arr.length, vec.size());
    }
}
