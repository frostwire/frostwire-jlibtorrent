package com.frostwire.jlibtorrent;

import org.junit.Test;

import com.frostwire.jlibtorrent.swig.settings_pack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SettingsPack} including the new
 * {@link SettingsPack#diskDisableCopyOnWrite()} setting.
 *
 * @author gubatron
 * @author aldenml
 */
public class SettingsPackTest {

    /**
     * Verify that diskDisableCopyOnWrite has a deterministic default value.
     * In libtorrent 2.0.12 (commit 5720b90a1), the default was changed to {@code false}
     * (CoW enabled by default).
     */
    @Test
    public void testDiskDisableCopyOnWriteDefaultValue() {
        SettingsPack pack = new SettingsPack();
        assertFalse("diskDisableCopyOnWrite should default to false (CoW enabled)",
                pack.diskDisableCopyOnWrite());
    }

    /**
     * Verify that diskDisableCopyOnWrite can be set to true and read back.
     */
    @Test
    public void testDiskDisableCopyOnWriteSetTrue() {
        SettingsPack pack = new SettingsPack();
        pack.diskDisableCopyOnWrite(true);
        assertTrue("diskDisableCopyOnWrite should be true after setting to true",
                pack.diskDisableCopyOnWrite());
    }

    /**
     * Verify that diskDisableCopyOnWrite can be toggled between true and false.
     */
    @Test
    public void testDiskDisableCopyOnWriteToggle() {
        SettingsPack pack = new SettingsPack();

        // Set to false (enable CoW)
        pack.diskDisableCopyOnWrite(false);
        assertFalse("diskDisableCopyOnWrite should be false after setting to false",
                pack.diskDisableCopyOnWrite());

        // Toggle back to true (disable CoW)
        pack.diskDisableCopyOnWrite(true);
        assertTrue("diskDisableCopyOnWrite should be true after toggling back",
                pack.diskDisableCopyOnWrite());
    }

    /**
     * Verify that diskDisableCopyOnWrite setter returns 'this' for fluent chaining.
     */
    @Test
    public void testDiskDisableCopyOnWriteFluentApi() {
        SettingsPack pack = new SettingsPack();
        SettingsPack result = pack.diskDisableCopyOnWrite(true);
        assertSame("diskDisableCopyOnWrite setter should return same SettingsPack instance",
                pack, result);
    }

    /**
     * Verify that multiple settings can be chained together using the fluent API,
     * including the new diskDisableCopyOnWrite setting.
     */
    @Test
    public void testFluentApiChaining() {
        SettingsPack pack = new SettingsPack()
                .diskDisableCopyOnWrite(true)
                .downloadRateLimit(1024 * 1024)
                .uploadRateLimit(512 * 1024);

        assertTrue("diskDisableCopyOnWrite should be true",
                pack.diskDisableCopyOnWrite());
        assertEquals("downloadRateLimit should be 1MB/s",
                1024 * 1024, pack.downloadRateLimit());
        assertEquals("uploadRateLimit should be 512KB/s",
                512 * 1024, pack.uploadRateLimit());
    }

    // =================== natpmpGateway ===================

    @Test
    public void testNatpmpGatewayDefaultValue() {
        SettingsPack pack = new SettingsPack();
        assertEquals("natpmpGateway should default to empty string (auto-detect)",
                "", pack.natpmpGateway());
        assertEquals("raw SWIG should also report empty default",
                "", pack.swig().get_str(settings_pack.string_types.natpmp_gateway.swigValue()));
    }

    @Test
    public void testNatpmpGatewayBoundaryValues() {
        SettingsPack pack = new SettingsPack();

        // Empty string (auto-detect)
        pack.natpmpGateway("");
        assertEquals("empty string should round-trip", "", pack.natpmpGateway());
        assertEquals("", pack.swig().get_str(settings_pack.string_types.natpmp_gateway.swigValue()));

        // Typical IPv4 address
        pack.natpmpGateway("192.168.1.1");
        assertEquals("IPv4 address should round-trip", "192.168.1.1", pack.natpmpGateway());
        assertEquals("192.168.1.1", pack.swig().get_str(settings_pack.string_types.natpmp_gateway.swigValue()));

        // Very long string boundary
        String longGateway = "A".repeat(300);
        pack.natpmpGateway(longGateway);
        assertEquals("long string should round-trip", longGateway, pack.natpmpGateway());
    }

    @Test
    public void testNatpmpGatewaySwigRoundTrip() {
        SettingsPack pack = new SettingsPack();

        // Set via Java wrapper, read back via raw SWIG
        pack.natpmpGateway("10.0.0.1");
        assertEquals("10.0.0.1", pack.swig().get_str(settings_pack.string_types.natpmp_gateway.swigValue()));

        // Set via raw SWIG, read back via Java wrapper
        pack.swig().set_str(settings_pack.string_types.natpmp_gateway.swigValue(), "172.16.0.1");
        assertEquals("172.16.0.1", pack.natpmpGateway());
    }

    @Test
    public void testNatpmpGatewayFluentApi() {
        SettingsPack pack = new SettingsPack()
                .natpmpGateway("192.168.0.1")
                .downloadRateLimit(2048);
        assertEquals("192.168.0.1", pack.natpmpGateway());
        assertEquals(2048, pack.downloadRateLimit());
        assertSame("natpmpGateway setter should return same SettingsPack instance",
                pack, pack.natpmpGateway("10.0.0.1"));
    }

    // =================== allowMultipleConnectionsPerPid ===================

    @Test
    public void testAllowMultipleConnectionsPerPidDefaultValue() {
        SettingsPack pack = new SettingsPack();
        assertFalse("allowMultipleConnectionsPerPid should default to false",
                pack.allowMultipleConnectionsPerPid());
        assertFalse("raw SWIG should also report false default",
                pack.swig().get_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue()));
    }

    @Test
    public void testAllowMultipleConnectionsPerPidSwigRoundTrip() {
        SettingsPack pack = new SettingsPack();

        // Set true via wrapper, verify both wrapper and raw SWIG
        pack.allowMultipleConnectionsPerPid(true);
        assertTrue("should be true after setting via wrapper", pack.allowMultipleConnectionsPerPid());
        assertTrue("raw SWIG should reflect true",
                pack.swig().get_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue()));

        // Set false via wrapper, verify both wrapper and raw SWIG
        pack.allowMultipleConnectionsPerPid(false);
        assertFalse("should be false after setting via wrapper", pack.allowMultipleConnectionsPerPid());
        assertFalse("raw SWIG should reflect false",
                pack.swig().get_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue()));

        // Set true via raw SWIG, verify wrapper reads it back
        pack.swig().set_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue(), true);
        assertTrue("wrapper should reflect true set via raw SWIG", pack.allowMultipleConnectionsPerPid());
    }

    @Test
    public void testAllowMultipleConnectionsPerPidFluentApi() {
        SettingsPack pack = new SettingsPack()
                .allowMultipleConnectionsPerPid(true)
                .uploadRateLimit(1024);
        assertTrue(pack.allowMultipleConnectionsPerPid());
        assertEquals(1024, pack.uploadRateLimit());
        assertSame("allowMultipleConnectionsPerPid setter should return same SettingsPack instance",
                pack, pack.allowMultipleConnectionsPerPid(false));
    }

    // =================== natpmpLeaseDuration ===================

    @Test
    public void testNatpmpLeaseDurationDefaultValue() {
        SettingsPack pack = new SettingsPack();
        assertEquals("natpmpLeaseDuration should default to 3600 (1 hour)",
                3600, pack.natpmpLeaseDuration());
        assertEquals("raw SWIG should also report 3600 default",
                3600, pack.swig().get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));
    }

    @Test
    public void testNatpmpLeaseDurationBoundaryValues() {
        SettingsPack pack = new SettingsPack();

        // Zero lease duration
        pack.natpmpLeaseDuration(0);
        assertEquals("0 should round-trip", 0, pack.natpmpLeaseDuration());
        assertEquals(0, pack.swig().get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));

        // Negative value (should be stored as-is)
        pack.natpmpLeaseDuration(-1);
        assertEquals("-1 should round-trip", -1, pack.natpmpLeaseDuration());
        assertEquals(-1, pack.swig().get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));

        // Large positive value
        pack.natpmpLeaseDuration(Integer.MAX_VALUE);
        assertEquals("Integer.MAX_VALUE should round-trip",
                Integer.MAX_VALUE, pack.natpmpLeaseDuration());
        assertEquals(Integer.MAX_VALUE,
                pack.swig().get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));

        // Typical 2-hour lease
        pack.natpmpLeaseDuration(7200);
        assertEquals("7200 should round-trip", 7200, pack.natpmpLeaseDuration());
    }

    @Test
    public void testNatpmpLeaseDurationSwigRoundTrip() {
        SettingsPack pack = new SettingsPack();

        // Set via wrapper, read via raw SWIG
        pack.natpmpLeaseDuration(1800);
        assertEquals(1800, pack.swig().get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));

        // Set via raw SWIG, read via wrapper
        pack.swig().set_int(settings_pack.int_types.natpmp_lease_duration.swigValue(), 3600);
        assertEquals(3600, pack.natpmpLeaseDuration());
    }

    @Test
    public void testNatpmpLeaseDurationFluentApi() {
        SettingsPack pack = new SettingsPack()
                .natpmpLeaseDuration(7200)
                .activeDownloads(5);
        assertEquals(7200, pack.natpmpLeaseDuration());
        assertEquals(5, pack.activeDownloads());
        assertSame("natpmpLeaseDuration setter should return same SettingsPack instance",
                pack, pack.natpmpLeaseDuration(3600));
    }

    // =================== SWIG enum ordinals ===================

    @Test
    public void testSwigEnumOrdinals() {
        // Verify the generated SWIG enum ordinals match the expected libtorrent values.
        // These values must stay in sync with the C++ enum definitions; if SWIG is
        // regenerated and the ordinals shift, this test will fail.
        assertEquals("natpmp_gateway ordinal", 12,
                settings_pack.string_types.natpmp_gateway.swigValue());
        assertEquals("allow_multiple_connections_per_pid ordinal", 32854,
                settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue());
        assertEquals("natpmp_lease_duration ordinal", 16544,
                settings_pack.int_types.natpmp_lease_duration.swigValue());

        // Verify swigToEnum round-trips correctly
        assertSame(settings_pack.string_types.natpmp_gateway,
                settings_pack.string_types.swigToEnum(12));
        assertSame(settings_pack.bool_types.allow_multiple_connections_per_pid,
                settings_pack.bool_types.swigToEnum(32854));
        assertSame(settings_pack.int_types.natpmp_lease_duration,
                settings_pack.int_types.swigToEnum(16544));

        // Verify that a raw settings_pack queried with these ordinals returns the
        // correct default values (this proves the JNI layer maps them to the right
        // C++ settings).
        settings_pack sp = new settings_pack();
        assertEquals("", sp.get_str(settings_pack.string_types.natpmp_gateway.swigValue()));
        assertFalse(sp.get_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue()));
        assertEquals(3600, sp.get_int(settings_pack.int_types.natpmp_lease_duration.swigValue()));
    }

    /**
     * Verify that the underlying swig object is accessible.
     */
    @Test
    public void testSwigAccessor() {
        SettingsPack pack = new SettingsPack();
        assertNotNull("swig() should return non-null settings_pack",
                pack.swig());
    }
}
