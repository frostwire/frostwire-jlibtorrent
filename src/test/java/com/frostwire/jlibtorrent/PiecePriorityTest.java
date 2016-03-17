package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * This test is for address the issue:
 * https://github.com/frostwire/frostwire-jlibtorrent/issues/77
 *
 * @author gubatron
 * @author aldenml
 */
public class PiecePriorityTest {

    @Test
    public void testScenario1() throws IOException {
        Session s = new Session();

        byte[] torrentBytes = Utils.getResourceBytes("./test1.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(torrentBytes);

        assertTrue(ti.numFiles() > 1);
    }
}
