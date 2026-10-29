package com.frostwire.jlibtorrent;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * frostwire/frostwire-jlibtorrent#330: an odd-length hex string made {@link Hex#decode(String)}
 * read past the end of the input (found by TextParsersFuzzTest).
 */
public class HexTest {

    @Test
    public void oddLengthIsRejectedWithIllegalArgumentException() {
        for (String odd : new String[]{"a", "abc", "0123456789abcdef0"}) {
            assertThrows(odd, IllegalArgumentException.class, () -> Hex.decode(odd));
        }
    }

    @Test
    public void illegalDigitsAreRejectedWithIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Hex.decode("zz"));
        assertThrows(IllegalArgumentException.class, () -> Hex.decode("0g"));
    }

    @Test
    public void hexRoundTripsInEitherCase() {
        byte[] bytes = {0, 1, (byte) 0x7f, (byte) 0x80, (byte) 0xff};
        assertEquals("00017f80ff", Hex.encode(bytes));
        assertArrayEquals(bytes, Hex.decode("00017F80FF"));
        assertArrayEquals(new byte[0], Hex.decode(""));
    }

    @Test
    public void hashConstructorsRejectBadHexWithIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Sha1Hash("abc"));
        assertThrows(IllegalArgumentException.class, () -> new Sha256Hash("abc"));
        assertThrows(IllegalArgumentException.class, () -> new Sha1Hash("abcd"));
    }
}
