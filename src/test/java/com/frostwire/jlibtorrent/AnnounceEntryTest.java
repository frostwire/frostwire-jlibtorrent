package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * {@link AnnounceEntry}, {@link WebSeedEntry} and the endpoint views of a tracker.
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceEntryTest {

    @Test
    public void announceEntryProperties() {
        AnnounceEntry e = new AnnounceEntry("udp://tracker:80/announce");
        assertNotNull(e.swig());
        assertEquals("udp://tracker:80/announce", e.url());
        assertTrue(e.endpoints().isEmpty());
        assertEquals(0, e.tier());
        assertFalse(e.isVerified());
        assertEquals(0, e.source());

        e.url("udp://other:81/announce");
        assertEquals("udp://other:81/announce", e.url());
        e.trackerId("tid");
        assertEquals("tid", e.trackerId());
        e.tier((short) 3);
        assertEquals(3, e.tier());
        e.failLimit((short) 9);
        assertEquals(9, e.failLimit());

        AnnounceEntry wrapped = new AnnounceEntry(e.swig());
        assertEquals("udp://other:81/announce", wrapped.url());
    }

    @Test
    public void webSeedEntry() {
        WebSeedEntry w = new WebSeedEntry("http://seed/file");
        assertEquals("http://seed/file", w.url());
        assertEquals("http://seed/file", w.toString());
        assertEquals("", w.auth());
        assertTrue(w.extraHeaders().isEmpty());
        assertEquals(WebSeedEntry.Type.URL_SEED, w.type());
        assertEquals(w, new WebSeedEntry("http://seed/file"));
        assertEquals(w.hashCode(), new WebSeedEntry("http://seed/file").hashCode());
        assertNotEquals(w, new WebSeedEntry("http://seed/other"));
        assertNotEquals(w, "http://seed/file");
        assertNull(null);
        try {
            new WebSeedEntry(null);
            fail("null url");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void announceInfohashAndEndpointViews() {
        com.frostwire.jlibtorrent.swig.announce_endpoint native_ = new com.frostwire.jlibtorrent.swig.announce_endpoint();
        AnnounceEndpoint ep = new AnnounceEndpoint(native_);
        assertNotNull(ep.swig());
        assertNotNull(ep.localEndpoint());
        assertTrue(ep.enabled());

        AnnounceInfohash v1 = ep.infohashV1();
        assertEquals("", v1.message());
        assertEquals(0, v1.fails());
        assertFalse(v1.updating());
        assertTrue(v1.isWorking());
        assertNotNull(ep.infohashV2());
    }
}
