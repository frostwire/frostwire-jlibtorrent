package com.frostwire.jlibtorrent;

import org.junit.Assume;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

/**
 * frostwire/frostwire-jlibtorrent#337: SWIG proxies are only freed by {@code delete()}, and the
 * wrappers leaked the native temporaries of every call (decode buffers, nodes, error codes, result
 * vectors). Parsing a torrent leaked about 4 KB, more than the torrent_info it returned.
 * <p>
 * Each loop frees the object it gets back, so any growth left is leaked temporaries. It measures
 * the process's resident memory, so it runs only through {@code ./gradlew leakTest}, not with the
 * unit tests.
 */
public class NativeMemoryLeakTest {

    private static final int ROUNDS = 100_000;
    // RSS noise across a 100k batch reaches a few tens of MB either way; the leaks this guards
    // against grew 1.7-2.8 GB per batch (13.5 KB per call). 150 MB catches anything above ~1.5 KB
    // per call.
    private static final long ALLOWED_GROWTH_KB = 150 * 1024;

    @Test
    public void parsingTorrentsDoesNotLeakTemporaries() throws Exception {
        Assume.assumeTrue(Boolean.getBoolean("jlibtorrent.leakTest"));
        byte[] torrent = resource("/bittorrent-v2-test.torrent");
        assertFlat("TorrentInfo(byte[])", () -> new TorrentInfo(torrent).swig().delete());
    }

    @Test
    public void decodingAndEncodingEntriesDoesNotLeakTemporaries() throws Exception {
        Assume.assumeTrue(Boolean.getBoolean("jlibtorrent.leakTest"));
        byte[] data = resource("/bittorrent-v2-test.torrent");
        assertFlat("Entry.bdecode + bencode", () -> {
            Entry e = Entry.bdecode(data);
            e.bencode();
            e.swig().delete();
        });
    }

    private static void assertFlat(String what, Runnable op) throws Exception {
        // A full warm-up batch first: the JIT and the native allocator grow the process once, a few
        // tens of MB, then plateau. A leak keeps growing in every batch, so only the second batch is
        // measured.
        for (int i = 0; i < ROUNDS; i++) {
            op.run();
        }
        System.gc();
        long before = rssKb();
        for (int i = 0; i < ROUNDS; i++) {
            op.run();
        }
        System.gc();
        long growth = rssKb() - before;
        System.out.println(what + ": RSS grew " + growth / 1024 + " MB over " + ROUNDS + " calls");
        assertTrue(what + " leaked " + growth / 1024 + " MB over " + ROUNDS + " calls", growth < ALLOWED_GROWTH_KB);
    }

    private static long rssKb() throws IOException {
        Process p = new ProcessBuilder("ps", "-o", "rss=", "-p", String.valueOf(ProcessHandle.current().pid())).start();
        try (InputStream in = p.getInputStream()) {
            return Long.parseLong(new String(in.readAllBytes(), StandardCharsets.US_ASCII).trim());
        }
    }

    private static byte[] resource(String name) throws IOException {
        try (InputStream in = NativeMemoryLeakTest.class.getResourceAsStream(name)) {
            return in.readAllBytes();
        }
    }
}
