package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.bdecode_node;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.torrent_info;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author gubatron
 * @author aldenml
 */
public class BDecodeReadTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = Utils.getResourceBytes("test5.torrent");

        byte_vector buffer = Vectors.bytes2byte_vector(data);
        bdecode_node e = new bdecode_node();
        error_code ec = new error_code();
        int ret = bdecode_node.bdecode(buffer, e, ec);

        assertEquals("failed to decode torrent: " + ec.message(), ret, 0);

        ec.clear();
        torrent_info ti = new torrent_info(e, ec);

        System.out.println(new Sha1Hash(ti.info_hash()));
        System.out.println(bdecode_node.to_string(e, false, 0));
    }
}
