package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.entry;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * frostwire/frostwire-jlibtorrent#336: C++ strings reached Java through NewStringUTF and came back
 * through GetStringUTFChars, which speak JNI's modified UTF-8. Bytes that aren't valid were
 * undefined behavior (a binary key came back as ""), embedded NULs truncated, and characters outside
 * the BMP crossed into C++ as CESU-8 instead of UTF-8.
 */
public class StringBoundaryTest {

    private static final String EMOJI = "torrent 🚀 file";  // U+1F680, 4 bytes in UTF-8

    @Test
    public void charactersOutsideTheBmpReachCppAsRealUtf8() {
        Entry e = new Entry(new entry(EMOJI));
        assertArrayEquals("4:..." + "", "torrent 🚀 file".getBytes(StandardCharsets.UTF_8),
                stringPayload(e.bencode()));
        assertEquals(EMOJI, e.string());
    }

    @Test
    public void embeddedNulSurvivesBothDirections() {
        String withNul = "a\u0000b";
        Entry e = new Entry(new entry(withNul));
        assertEquals("3:a\u0000b", new String(e.bencode(), StandardCharsets.ISO_8859_1));
        assertEquals(withNul, e.string());
    }

    @Test
    public void bytesThatAreNotUtf8BecomeReplacementCharactersNotGarbage() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("d32:".getBytes(StandardCharsets.US_ASCII));
        for (int i = 0; i < 32; i++) {
            out.write(0x80 + i);
        }
        out.write("1:x1:".getBytes(StandardCharsets.US_ASCII));
        out.write(0xff);
        out.write("1:ye".getBytes(StandardCharsets.US_ASCII));
        Map<String, Entry> d = Entry.bdecode(out.toByteArray()).dictionary();
        assertEquals(2, d.size());
        for (String key : d.keySet()) {
            assertTrue("a binary key is no longer empty", !key.isEmpty());
            assertTrue(key, key.chars().allMatch(c -> c == 0xFFFD));
        }
        assertEquals(1, d.keySet().stream().filter(k -> k.length() == 32).count());
    }

    private static byte[] stringPayload(byte[] bencodedString) {
        String s = new String(bencodedString, StandardCharsets.ISO_8859_1);
        int colon = s.indexOf(':');
        byte[] payload = new byte[bencodedString.length - colon - 1];
        System.arraycopy(bencodedString, colon + 1, payload, 0, payload.length);
        return payload;
    }
}
