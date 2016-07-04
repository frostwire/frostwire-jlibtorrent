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
        Map<String, Object> m = LibTorrent.properties();
        for (Map.Entry<String, Object> e : m.entrySet()) {
            assertNotNull(e.getValue());
        }
    }
}
