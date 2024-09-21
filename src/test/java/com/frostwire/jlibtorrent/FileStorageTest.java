package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testPaths() throws IOException {
        byte[] data = Utils.resourceBytes("Shinobi_Ninja_FrostWire_Mixtape__MP3_128K_Oct_02_2015.torrent");
        TorrentInfo ti = TorrentInfo.bdecode(data);
        FileStorage fs = ti.files();
        assertTrue(fs.numFiles() > 0);
        ArrayList<String> paths = fs.paths();
        //System.out.printf("File Storage numFiles:%d vs Paths size:%d\n", fs.numFiles(), paths.size());
        assertEquals(fs.numFiles(), paths.size());
        for (String path : paths) {
            assertTrue(path.length() > 0);
            //System.out.printf("FileStorageTest::testPaths() -> '%s' size: %d bytes\n", path, path.length());
        }
    }
}
