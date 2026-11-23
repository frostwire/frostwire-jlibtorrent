package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static com.frostwire.jlibtorrent.swig.libtorrent.list_files_ex;
import static com.frostwire.jlibtorrent.swig.libtorrent.set_piece_hashes_ex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Creating a hybrid torrent from a folder with a subfolder (Stems/) and a file whose name starts with
 * the subfolder's name (Stems.zip) produced a torrent that did not load: "the v1 and v2 file metadata
 * does not match" (frostwire/frostwire#1550). Fixed upstream in libtorrent (arvidn/libtorrent#9183,
 * "fix file order when creating hybrid torrents"). Needs the native library.
 */
public class HybridTorrentSiblingNameTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void aFileNamedLikeASiblingFolderLoadsBack() throws IOException {
        File dir = folder.newFolder("album");
        File stems = new File(dir, "Stems");
        assertTrue(stems.mkdir());
        Utils.writeByteArrayToFile(new File(stems, "a.mp3"), new byte[]{1, 2, 3}, false);
        Utils.writeByteArrayToFile(new File(dir, "Stems.zip"), new byte[]{4, 5, 6}, false);

        add_files_listener all = new add_files_listener() {
            @Override
            public boolean pred(String p) {
                return true;
            }
        };
        create_file_entry_vector entries = list_files_ex(dir.getAbsolutePath(), all, new create_flags_t());
        assertEquals(2, entries.size());
        create_torrent ct = new create_torrent(entries); // hybrid torrent
        error_code ec = new error_code();
        set_piece_hashes_ex(ct, dir.getParent(), new set_piece_hashes_listener() {
            @Override
            public void progress(int i) {
            }
        }, ec);
        assertEquals(0, ec.value());

        byte[] bytes = Vectors.byte_vector2bytes(ct.generate().bencode());
        // before the fix: IllegalArgumentException "the v1 and v2 file metadata does not match"
        TorrentInfo ti = TorrentInfo.bdecode(bytes);
        assertTrue(ti.isValid());
        // hybrid torrents add pad files between the real ones: the two real files must be there
        assertTrue(ti.numFiles() >= 2);
    }
}
