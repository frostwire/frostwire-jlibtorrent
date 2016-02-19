package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class Sha1HashTest {

    @Test
    public void testHashCode() {
        byte[] arr = new byte[20];
        Arrays.fill(arr, (byte) 2);
        Sha1Hash h = new Sha1Hash(arr);
        assertEquals(Arrays.hashCode(arr), h.hashCode());
    }
}
