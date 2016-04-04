package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * @author gubatron
 * @author aldenml
 */
public class PrintVersionsTest {

    @Test
    public void testComponentVersions() {
        Map<String, String> m = LibTorrent.componentVersions();
        for (Map.Entry<String, String> e : m.entrySet()) {
            assertNotNull(e.getValue());
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }
}
