package com.frostwire.jlibtorrent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertNull;

/**
 * fetchMagnet freed its error_code after parsing the magnet, then called clear() on it before
 * adding the torrent: every fetch of a torrent the session didn't have yet passed a null pointer to
 * native code and killed the JVM (FrostWire desktop, double-clicking a search result).
 */
public class FetchMagnetUnknownTorrentTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void fetchingAMagnetTheSessionDoesNotHaveTimesOutWithoutCrashing() throws Exception {
        SettingsPack settings = new SettingsPack();
        settings.setEnableDht(false);
        settings.setEnableLsd(false);
        settings.listenInterfaces("127.0.0.1:0");
        SessionManager session = new SessionManager(false);
        session.start(new SessionParams(settings));
        try {
            String uri = "magnet:?xt=urn:btih:0123456789abcdef0123456789abcdef01234567";
            // no peers and no DHT: the metadata never arrives, so the fetch times out
            assertNull(session.fetchMagnet(uri, 1, tmp.newFolder("fetch")));
        } finally {
            session.stop();
        }
    }
}
