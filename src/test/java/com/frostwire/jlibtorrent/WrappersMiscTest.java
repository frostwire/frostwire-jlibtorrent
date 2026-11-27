package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.info_hash_t;
import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.string_string_pair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Small wrappers that need no session: version information, pairs, info hashes, move flags,
 * metrics and network enumeration.
 *
 * @author gubatron
 * @author aldenml
 */
public class WrappersMiscTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void versionInformation() {
        assertTrue(LibTorrent.versionNum() >= 20100);
        assertTrue(LibTorrent.version(), LibTorrent.version().startsWith("2.1"));
        assertFalse(LibTorrent.boostVersion().isEmpty());
        assertTrue(LibTorrent.boostVersionNum() > 0);
        assertFalse(LibTorrent.opensslVersion().isEmpty());
        assertTrue(LibTorrent.opensslVersionNum() != 0);
        assertFalse(LibTorrent.jlibtorrentVersion().isEmpty());
        assertFalse(LibTorrent.revision().isEmpty());
        // only reported, the answer depends on the CPU
        LibTorrent.hasArmNeonSupport();
        LibTorrent.hasWebTorrentSupport();
    }

    @Test
    public void sessionMetricsAreNamedAndTyped() {
        List<StatsMetric> metrics = LibTorrent.sessionStatsMetrics();
        assertFalse(metrics.isEmpty());
        boolean counter = false;
        boolean gauge = false;
        for (StatsMetric m : metrics) {
            assertFalse(m.name.isEmpty());
            assertTrue(m.toString().startsWith(m.name + ":" + m.valueIndex + ":"));
            counter |= m.type == StatsMetric.TYPE_COUNTER && m.toString().endsWith(":counter");
            gauge |= m.type == StatsMetric.TYPE_GAUGE && m.toString().endsWith(":gauge");
            assertEquals(m.valueIndex, LibTorrent.findMetricIdx(m.name));
        }
        assertTrue(counter);
        assertTrue(gauge);
        assertEquals(LibTorrent.findMetricIdx(StatsMetric.NET_SENT_BYTES_COUNTER_NAME), StatsMetric.NET_SENT_BYTES_COUNTER_INDEX);
        assertEquals(-1, LibTorrent.findMetricIdx("no.such.metric"));
    }

    @Test
    public void pairsConvertToNativePairs() {
        Pair<String, Integer> si = new Pair<>("host", 6881);
        string_int_pair a = si.to_string_int_pair();
        assertEquals("host", a.getFirst());
        assertEquals(6881, a.getSecond());

        Pair<String, String> ss = new Pair<>("k", "v");
        string_string_pair b = ss.to_string_string_pair();
        assertEquals("k", b.getFirst());
        assertEquals("v", b.getSecond());
    }

    @Test
    public void infoHashes() {
        InfoHash empty = new InfoHash();
        assertFalse(empty.hasV1());
        assertFalse(empty.hasV2());
        assertNotNull(empty.swig());

        info_hash_t native_ = new info_hash_t(new Sha1Hash("00112233445566778899aabbccddeeff00112233").swig());
        InfoHash ih = new InfoHash(native_);
        assertTrue(ih.hasV1());
        assertFalse(ih.hasV2());
        assertEquals("00112233445566778899aabbccddeeff00112233", ih.getV1().toHex());
        assertEquals("00112233445566778899aabbccddeeff00112233", ih.getBest().toHex());
        assertTrue(ih.getV2().isAllZeros());
    }

    @Test
    public void moveFlagsMapBackToTheirNativeValue() {
        for (MoveFlags f : MoveFlags.values()) {
            assertEquals(f, MoveFlags.fromSwig(f.swig()));
        }
    }

    @Test
    public void operationsHaveNativeNames() {
        for (Operation op : Operation.values()) {
            if (op != Operation.UNKNOWN) {
                assertFalse(op.name(), op.nativeName().isEmpty());
            }
            assertEquals(op, Operation.fromSwig(op.swig()));
        }
    }

    @Test
    public void networkInterfacesAndRoutesOfThisMachine() {
        try (LoopbackSession s = LoopbackSession.start()) {
            List<EnumNet.IpInterface> interfaces = EnumNet.enumInterfaces(s.sm);
            assertFalse(interfaces.isEmpty());
            boolean loopback = false;
            for (EnumNet.IpInterface i : interfaces) {
                assertNotNull(i.swig());
                assertNotNull(i.netmask());
                assertNotNull(i.friendlyName());
                assertNotNull(i.description());
                i.preferred();
                assertTrue(i.toString().contains("address: " + i.interfaceAddress()));
                assertFalse(i.name().isEmpty());
                loopback |= i.interfaceAddress().isLoopback();
            }
            assertTrue(loopback);

            List<EnumNet.IpRoute> routes = EnumNet.enumRoutes(s.sm);
            for (EnumNet.IpRoute r : routes) {
                assertNotNull(r.swig());
                assertNotNull(r.destination());
                assertNotNull(r.netmask());
                assertNotNull(r.gateway());
                assertNotNull(r.name());
                assertTrue(r.mtu() >= 0);
                assertTrue(r.toString().contains("destination: " + r.destination()));
            }
            if (!routes.isEmpty()) {
                com.frostwire.jlibtorrent.swig.ip_route_vector v = com.frostwire.jlibtorrent.swig.libtorrent.enum_routes(s.sm.swig());
                assertNotNull(EnumNet.getGateway(s.sm, interfaces.get(0), v));
            }
        }
        // a manager that isn't running has nothing to enumerate
        SessionManager stopped = new SessionManager();
        assertTrue(EnumNet.enumInterfaces(stopped).isEmpty());
        assertTrue(EnumNet.enumRoutes(stopped).isEmpty());
    }

    @Test
    public void filesReadsWholeFilesAndRejectsBadOnes() throws IOException {
        File f = folder.newFile("data.bin");
        Utils.writeByteArrayToFile(f, new byte[]{1, 2, 3, 4}, false);
        assertEquals(4, Files.bytes(f).length);

        File empty = folder.newFile("empty.bin");
        assertEquals(0, Files.bytes(empty).length);

        try {
            Files.bytes(folder.getRoot());
            org.junit.Assert.fail("a directory can't be read as a file");
        } catch (IOException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("is a directory"));
        }
        try {
            Files.bytes(new File(folder.getRoot(), "missing.bin"));
            org.junit.Assert.fail("missing file");
        } catch (java.io.FileNotFoundException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("does not exist"));
        }
        // the file shrank after its length was read
        File shrinking = folder.newFile("shrinking.bin");
        Utils.writeByteArrayToFile(shrinking, new byte[]{1, 2, 3}, false);
        java.io.InputStream in = new java.io.ByteArrayInputStream(new byte[]{1});
        try {
            java.lang.reflect.Method m = Files.class.getDeclaredMethod("toByteArray", java.io.InputStream.class, int.class);
            m.setAccessible(true);
            m.invoke(null, in, 3);
            org.junit.Assert.fail("short read");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertTrue(e.getCause().getMessage(), e.getCause().getMessage().contains("Unexpected readed size"));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
        try {
            java.lang.reflect.Method m = Files.class.getDeclaredMethod("toByteArray", java.io.InputStream.class, int.class);
            m.setAccessible(true);
            m.invoke(null, in, -1);
            org.junit.Assert.fail("negative size");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
        try {
            java.lang.reflect.Method m = Files.class.getDeclaredMethod("toByteArray", java.io.InputStream.class, long.class);
            m.setAccessible(true);
            m.invoke(null, in, Integer.MAX_VALUE + 1L);
            org.junit.Assert.fail("too large");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
        Files.closeQuietly((java.io.Closeable) null);
        Files.closeQuietly(new java.io.Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("close failed");
            }
        });
        new RandomAccessFile(f, "r").close();
    }
}
