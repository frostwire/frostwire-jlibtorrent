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
