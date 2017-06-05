package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class FileStorageTest {

    @Test
    public void testFileName() throws IOException {
        byte[] data = Utils.resourceBytes("test5.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(data);
        String name = ti.files().fileName(0);
        assertEquals("frostwire-6.2.3.windows.fusion.exe", name);
    }
}
