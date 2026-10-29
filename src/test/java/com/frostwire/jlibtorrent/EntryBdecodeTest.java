package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * frostwire/frostwire-jlibtorrent#332: {@link Entry#bdecode(byte[])} threw an undocumented bare
 * RuntimeException (SWIG's translation of libtorrent's system_error) for malformed data, found by
 * EntryBdecodeFuzzTest on the empty input.
 */
public class EntryBdecodeTest {

    private static byte[] ascii(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

    @Test
    public void malformedDataIsRejectedWithIllegalArgumentException() {
        for (String bad : new String[]{"", "d", "i12", "5:abc", "l1:a", "x"}) {
            IllegalArgumentException e = assertThrows(bad, IllegalArgumentException.class,
                    () -> Entry.bdecode(ascii(bad)));
            assertTrue(e.getMessage(), e.getMessage().startsWith("Can't decode data: "));
        }
    }

    @Test
    public void nestingBeyondTheDepthLimitIsRejected() {
        String deep = "l".repeat(1000) + "e".repeat(1000);
        assertThrows(IllegalArgumentException.class, () -> Entry.bdecode(ascii(deep)));
    }

    @Test
    public void bytesAfterTheFirstCompleteValueAreIgnored() {
        assertEquals("l1:ae", new String(Entry.bdecode(ascii("l1:ae1:b")).bencode(), StandardCharsets.US_ASCII));
    }

    @Test
    public void validDataStillDecodes() {
        Entry e = Entry.bdecode(ascii("d3:fooi42e4:listl1:aee"));
        assertEquals(42, e.dictionary().get("foo").integer());
        assertEquals("a", e.dictionary().get("list").list().get(0).string());
        assertEquals("d3:fooi42e4:listl1:aee", new String(e.bencode(), StandardCharsets.US_ASCII));
    }
}
