package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.torrent_info;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class BDecodeReadTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = Utils.resourceBytes("test5.torrent");

        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node e = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, e, ec);

        assertEquals("failed to decode torrent: " + ec.message(), ret, 0);

        ec.clear();
        torrent_info ti = new torrent_info(e, ec);
        buffer.clear(); // prevents GC

        assertEquals("failed to create torrent info: " + ec.message(), ret, 0);
    }

    @Test
    public void testReadNone() throws IOException {
        byte[] data = Utils.resourceBytes("test5.torrent");

        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node e = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, e, ec);

        assertEquals("failed to decode torrent: " + ec.message(), ret, 0);

        bdecode_node n1 = e.dict_find_s("info");
        assertEquals(n1.type(), bdecode_node.type_t.dict_t);
        // { 'length': , 'name': , 'piece length': , 'pieces': }
        assertEquals(n1.dict_size(), 4);

        bdecode_node n2 = e.dict_find_list_s("any-key");
        assertEquals(n2.type(), bdecode_node.type_t.none_t);
        // if (n2.type() != bdecode_node.type_t.none_t)
        //    assertEquals(n2.list_size(), <size>);

        ec.clear();
        buffer.clear(); // prevents GC
    }
}
