package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author gubatron
 * @author aldenml
 */
public class BDecodeNodeTest {

    private static BDecodeNode decode(String s) {
        return BDecodeNode.bdecode(s.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void dictionaryLookups() {
        BDecodeNode n = decode("d1:ali1ei2ee1:dd1:x1:ye1:s3:str1:ni7ee");
        assertNotNull(n.swig());
        assertNotNull(n.buffer());
        assertTrue(n.toString(), n.toString().contains("str"));

        assertTrue(n.hasList("a"));
        assertFalse(n.hasList("d"));
        assertFalse(n.hasList("missing"));
        assertEquals(2, n.getList("a").swig().list_size());
        assertNull(n.getList("missing"));

        assertTrue(n.hasDict("d"));
        assertFalse(n.hasDict("a"));
        assertEquals("y", n.getDict("d").getString("x"));
        assertNull(n.getDict("missing"));

        assertTrue(n.hasString("s"));
        assertEquals("str", n.getString("s"));
    }

    @Test
    public void hasStringIsFalseForMissingOrNonStringKeys() {
        BDecodeNode n = decode("d1:ni7e1:s3:stre");
        assertFalse(n.hasString("missing"));
        assertFalse(n.hasString("n"));
        assertTrue(n.hasString("s"));
    }

    @Test
    public void nodesWithoutAValueAnswerNegatively() {
        BDecodeNode none = new BDecodeNode(new com.frostwire.jlibtorrent.swig.bdecode_node());
        assertNull(none.buffer());
        assertFalse(none.hasList("a"));
        assertFalse(none.hasDict("a"));
        assertFalse(none.hasString("a"));
        assertFalse(none.hasInt("a"));
        assertNull(none.getString("a"));
        assertEquals(0, none.getInt("a"));
    }

    @Test
    public void malformedDataIsRejected() {
        try {
            decode("d1:a");
            fail("truncated dictionary");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("Can't decode data"));
        }
    }
}
