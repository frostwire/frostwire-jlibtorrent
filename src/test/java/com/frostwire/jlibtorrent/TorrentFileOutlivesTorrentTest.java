package com.frostwire.jlibtorrent;

import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TorrentHandle.torrentFile() used to wrap a raw pointer into the session's metadata, which
 * libtorrent frees when the torrent is removed: reading the TorrentInfo afterwards crashed the JVM
 * with SIGSEGV (frostwire/frostwire#1531). It now keeps the metadata alive.
 * Needs the native library, like the other session tests.
 */
public class TorrentFileOutlivesTorrentTest {

    @Test
    public void theMetadataStaysReadableAfterTheTorrentIsRemoved() throws Exception {
        SessionManager sm = new SessionManager(false);
        try {
            sm.start();
        } catch (Throwable noNativeLibrary) {
            Assume.assumeNoException("needs the native library", noNativeLibrary);
        }
        try {
            byte[] torrentBytes = Utils.resourceBytes("test4.torrent");
            TorrentInfo added = TorrentInfo.bdecode(torrentBytes);
            File saveDir = Files.createTempDirectory("jlt-1531").toFile();
            sm.download(added, saveDir);

            TorrentHandle th = null;
            long deadline = System.currentTimeMillis() + 10000;
            while (th == null && System.currentTimeMillis() < deadline) {
                th = sm.find(added);
                if (th == null) {
                    Thread.sleep(50);
                }
            }
            assertNotNull("the torrent was added", th);

            TorrentInfo held = th.torrentFile();
            assertNotNull(held);
            String name = held.name();
            int files = held.numFiles();
            long total = held.totalSize();

            sm.remove(th);
            deadline = System.currentTimeMillis() + 10000;
            while (sm.find(added) != null && System.currentTimeMillis() < deadline) {
                Thread.sleep(50);
            }
            // give libtorrent time to free its torrent, then churn memory
            Thread.sleep(500);
            for (int i = 0; i < 50; i++) {
                byte[] garbage = new byte[1 << 20];
                garbage[0] = (byte) i;
                System.gc();
            }

            // before the fix this read freed memory
            assertEquals(name, held.name());
            assertEquals(files, held.numFiles());
            assertEquals(total, held.totalSize());
            assertTrue(held.numFiles() > 0);
        } finally {
            sm.stop();
        }
    }
}
