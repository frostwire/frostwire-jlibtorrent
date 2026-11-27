package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author gubatron
 * @author aldenml
 */
public class SessionParamsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void settingsRoundTripThroughTheNativeParams() {
        SettingsPack sp = new SettingsPack();
        sp.downloadRateLimit(111_000);
        SessionParams p = new SessionParams(sp);
        assertNotNull(p.swig());
        assertEquals(111_000, p.getSettings().downloadRateLimit());

        SettingsPack other = new SettingsPack();
        other.uploadRateLimit(222_000);
        p.setSettings(other);
        assertEquals(222_000, p.getSettings().uploadRateLimit());
    }

    @Test
    public void diskIoConstructorsCanBeSelected() {
        SessionParams p = new SessionParams();
        p.setPosixDiskIO();
        p.setPreadDiskIO();
        p.setDefaultDiskIO();
    }

    @Test
    public void savedStateIsReadBackFromBytesAndFromAFile() throws IOException {
        SettingsPack sp = new SettingsPack();
        sp.connectionsLimit(321);
        SessionParams original = new SessionParams(sp);

        byte[] state = Vectors.byte_vector2bytes(
                com.frostwire.jlibtorrent.swig.session_params.write_session_params_buf(original.swig()));
        assertTrue(state.length > 0);

        SessionParams fromBytes = new SessionParams(state);
        assertEquals(321, fromBytes.getSettings().connectionsLimit());

        File f = folder.newFile("session.dat");
        Utils.writeByteArrayToFile(f, state, false);
        SessionParams fromFile = new SessionParams(f);
        assertEquals(321, fromFile.getSettings().connectionsLimit());
    }

    @Test
    public void garbageIsRejected() throws IOException {
        try {
            new SessionParams(new byte[]{'x', 'y', 'z'});
            fail("garbage is not a session state");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("Can't decode data"));
        }
    }

    @Test
    public void aMissingFileIsRejected() {
        File missing = new File(folder.getRoot(), "nope.dat");
        try {
            new SessionParams(missing);
            fail("a missing file can't be decoded");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("nope.dat"));
        }
    }
}
