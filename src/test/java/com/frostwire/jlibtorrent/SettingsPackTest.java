package com.frostwire.jlibtorrent;

import org.junit.Test;

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
     * In current libtorrent builds, the default is {@code true} (CoW disabled).
     */
    @Test
    public void testDiskDisableCopyOnWriteDefaultValue() {
        SettingsPack pack = new SettingsPack();
        assertTrue("diskDisableCopyOnWrite should default to true",
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
    public void testNatpmpGatewayRoundTrip() {
        SettingsPack pack = new SettingsPack();
        pack.natpmpGateway("192.168.1.1");
        assertEquals("natpmpGateway should round-trip",
                "192.168.1.1", pack.natpmpGateway());

        pack.natpmpGateway("");
        assertEquals("natpmpGateway should be empty after clearing",
                "", pack.natpmpGateway());
    }

    @Test
    public void testNatpmpGatewayFluentApi() {
        SettingsPack pack = new SettingsPack();
        SettingsPack result = pack.natpmpGateway("10.0.0.1");
        assertSame("natpmpGateway setter should return same SettingsPack instance",
                pack, result);
    }

    // =================== allowMultipleConnectionsPerPid ===================

    @Test
    public void testAllowMultipleConnectionsPerPidRoundTrip() {
        SettingsPack pack = new SettingsPack();
        pack.allowMultipleConnectionsPerPid(true);
        assertTrue("allowMultipleConnectionsPerPid should be true after setting",
                pack.allowMultipleConnectionsPerPid());

        pack.allowMultipleConnectionsPerPid(false);
        assertFalse("allowMultipleConnectionsPerPid should be false after setting",
                pack.allowMultipleConnectionsPerPid());
    }

    @Test
    public void testAllowMultipleConnectionsPerPidFluentApi() {
        SettingsPack pack = new SettingsPack();
        SettingsPack result = pack.allowMultipleConnectionsPerPid(true);
        assertSame("allowMultipleConnectionsPerPid setter should return same SettingsPack instance",
                pack, result);
    }

    // =================== natpmpLeaseDuration ===================

    @Test
    public void testNatpmpLeaseDurationRoundTrip() {
        SettingsPack pack = new SettingsPack();
        pack.natpmpLeaseDuration(7200);
        assertEquals("natpmpLeaseDuration should round-trip",
                7200, pack.natpmpLeaseDuration());

        pack.natpmpLeaseDuration(1800);
        assertEquals("natpmpLeaseDuration should update correctly",
                1800, pack.natpmpLeaseDuration());
    }

    @Test
    public void testNatpmpLeaseDurationFluentApi() {
        SettingsPack pack = new SettingsPack();
        SettingsPack result = pack.natpmpLeaseDuration(3600);
        assertSame("natpmpLeaseDuration setter should return same SettingsPack instance",
                pack, result);
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
