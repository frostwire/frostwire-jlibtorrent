package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static com.frostwire.jlibtorrent.swig.libtorrent.add_files_ex;
import static com.frostwire.jlibtorrent.swig.libtorrent.set_piece_hashes_ex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author gubatron
 * @author aldenml
 */
public class CreateTorrentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testFromFile() throws IOException {
        final File f = folder.newFile("test.txt");
        Utils.writeByteArrayToFile(f, new byte[]{0}, false);

        file_storage fs = new file_storage();
        add_files_listener l1 = new add_files_listener() {
            @Override
            public boolean pred(String p) {
                assertEquals(f.getAbsolutePath(), p);
                return true;
            }
        };
        add_files_ex(fs, f.getAbsolutePath(), l1, 0L);
        create_torrent ct = new create_torrent(fs);
        set_piece_hashes_listener l2 = new set_piece_hashes_listener() {
            @Override
            public void progress(int i) {
                assertTrue(i >= 0);
                assertTrue(i <= 100);
            }
        };
        error_code ec = new error_code();
        set_piece_hashes_ex(ct, f.getParent(), l2, ec);
        assertEquals(ec.value(), 0);
        entry e = ct.generate();
        byte_vector buffer = e.bencode();
        TorrentInfo ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(buffer));
        assertEquals(ti.numFiles(), 1);
    }
}
