package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
        // create a file with 1 megabyte
        byte[] data = new byte[1024 * 1024];
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) (i % 256);
        }
        Utils.writeByteArrayToFile(f, data, false);

        file_storage fs = new file_storage();
        add_files_listener l1 = new add_files_listener() {
            @Override
            public boolean pred(String p) {
                assertEquals(f.getAbsolutePath(), p);
                return true;
            }
        };

        add_files_ex(fs, f.getAbsolutePath(), l1, new create_flags_t());
        create_torrent ct = new create_torrent(fs); // hybrid torrent
        //int autoPieceSize = 0;
        //create_torrent ct = new create_torrent(fs, autoPieceSize, create_torrent.v1_only); // v1 only torrent
        //create_torrent ct = new create_torrent(fs, autoPieceSize, create_torrent.v2_only); // v2 only torrent (shows truncated sha256 for the sha1)
        set_piece_hashes_listener piece_hash_listener = new set_piece_hashes_listener() {
            @Override
            public void progress(int i) {
                assertTrue(i >= 0);
            }
        };
        error_code ec = new error_code();

        set_piece_hashes_ex(ct, f.getParent(), piece_hash_listener, ec);
        assertEquals(ec.value(), 0);
        entry e = ct.generate();
        byte_vector buffer = e.bencode();

        byte[] bytes = Vectors.byte_vector2bytes(buffer);
        TorrentInfo ti = TorrentInfo.bdecode(bytes);
        assertTrue(ti.isValid());
        assertEquals(1, ti.numFiles());

        Sha1Hash sha1Hash = ti.infoHashV1();
        Sha256Hash sha2Hash = ti.infoHashV2();

        System.out.println("infohash v2 (SHA2 256): " + sha2Hash);
        System.out.println("infohash v1 (SHA1 160): " + sha1Hash);
        FileStorage files = ti.files();
        System.out.println("Files in torrent: " + files.numFiles());
        for (int i = 0; i < files.numFiles(); i++) {
            String path = files.filePath(i);
            long size = files.fileSize(i);
            Sha1Hash hash = files.hash(i);
            System.out.println("File " + i + ": " + path + " (" + size + " bytes) - hash: " + hash + ")");
        }
        //now write the buffer to a file
        File torrentFile = new File("test.torrent");
        Utils.writeByteArrayToFile(torrentFile, bytes, false);
        System.out.println("Torrent file created at: " + torrentFile.getAbsolutePath());

        // Now load the torrent from the .torrent file and check the info hashes
        byte[] torrentFileDataArray = Utils.readFileToByteArray(torrentFile);
        TorrentInfo ti2 = TorrentInfo.bdecode(torrentFileDataArray);

        assertEquals(sha1Hash, ti2.infoHashV1());
        System.out.println("Loaded torrent info hash V1: " + ti2.infoHashV1().toHex());

        if (ti2.infoHashV2() != null) {
            assertEquals(sha2Hash, ti2.infoHashV2());
            System.out.println("Loaded torrent info hash V2: " + ti2.infoHashV2().toHex());
        }
    }

    @Test
    public void testFromDir() throws IOException {
        File dir = folder.newFolder();
        File f1 = new File(dir, "test.txt");
        Utils.writeByteArrayToFile(f1, new byte[]{0}, false);
        File f2 = new File(dir, "test1.txt");
        Utils.writeByteArrayToFile(f2, new byte[]{0}, false);

        file_storage fs = new file_storage();
        add_files_listener l1 = new add_files_listener() {
            @Override
            public boolean pred(String p) {
                return true;
            }
        };
        add_files_ex(fs, dir.getAbsolutePath(), l1, new create_flags_t());
        create_torrent ct = new create_torrent(fs);
        set_piece_hashes_listener l2 = new set_piece_hashes_listener() {
            @Override
            public void progress(int i) {
                assertTrue(i >= 0);
            }
        };
        error_code ec = new error_code();
        set_piece_hashes_ex(ct, dir.getParent(), l2, ec);
        assertEquals(ec.value(), 0);
        entry e = ct.generate();
        byte_vector buffer = e.bencode();
        TorrentInfo ti = TorrentInfo.bdecode(Vectors.byte_vector2bytes(buffer));
        assertEquals(4, ti.numFiles());
    }

    @Test
    public void testUsingBuilder() throws IOException {
        File dir = folder.newFolder();
        File f1 = new File(dir, "test.txt");
        Utils.writeByteArrayToFile(f1, new byte[]{0}, false);
        File f2 = new File(dir, "test1.txt");
        Utils.writeByteArrayToFile(f2, new byte[]{0}, false);

        TorrentBuilder b = new TorrentBuilder();
        TorrentBuilder.Result r = b.path(dir)
                .comment("comment")
                .creator("creator")
                .addUrlSeed("http://urlseed/")
                .addNode(new Pair<>("1.1.1.1", 1))
                .addTracker("udp://tracker/")
                .setPrivate(true)
                .addSimilarTorrent(Sha1Hash.min())
                .addCollection("collection")
                .generate();

        TorrentInfo ti = TorrentInfo.bdecode(r.entry().bencode());
        assertEquals("comment", ti.comment());
        assertEquals("creator", ti.creator());

        ArrayList<WebSeedEntry> seeds = ti.webSeeds();
        for (WebSeedEntry e : seeds) {
            if (e.type() == WebSeedEntry.Type.URL_SEED) {
                assertEquals("http://urlseed/", e.url());
            }
            if (e.type() == WebSeedEntry.Type.HTTP_SEED) {
                assertEquals("http://httpseed/", e.url());
            }
        }

        assertEquals("1.1.1.1", ti.nodes().get(0).first);
        assertEquals("udp://tracker/", ti.trackers().get(0).url());
        assertEquals(true, ti.isPrivate());
        assertTrue(ti.similarTorrents().get(0).isAllZeros());
        assertEquals("collection", ti.collections().get(0));
        assertEquals(4, ti.numFiles());
    }

    @Test
    public void testBuilderListener() throws IOException {
        File dir = folder.newFolder();
        File f1 = new File(dir, "test.txt");
        Utils.writeByteArrayToFile(f1, new byte[]{0, 0}, false);
        File f2 = new File(dir, "test1.txt");
        Utils.writeByteArrayToFile(f2, new byte[]{0, 0}, false);

        final AtomicBoolean b1 = new AtomicBoolean();
        final AtomicBoolean b2 = new AtomicBoolean();

        TorrentBuilder b = new TorrentBuilder();
        TorrentBuilder.Result r = b.path(dir)
                .listener(new TorrentBuilder.Listener() {
                    @Override
                    public boolean accept(String filename) {
                        b1.set(true);
                        return true;
                    }

                    @Override
                    public void progress(int piece, int total) {
                        b2.set(true);
                    }
                })
                .generate();

        TorrentInfo ti = TorrentInfo.bdecode(r.entry().bencode());
        long totalBytes = 0;
        for (int i=0; i < ti.numFiles(); i++) {
            FileStorage files = ti.files();
            String path = files.filePath(i);
            long size = files.fileSize(i);
            totalBytes += size;
            //System.out.println(path + " (" + size + " bytes)");
        }

        // 4 files
        // test.txt, ./pad/16382, test1.txt, ./pad/16382
        assertEquals(4, ti.numFiles());
        assertTrue(b1.get());
        assertTrue(b2.get());

        //System.out.println("Expected total size: " + totalBytes + " bytes");
        assertEquals(totalBytes, ti.totalSize());
    }

    /*
    @Test
    public void testBuilderMerkle() throws IOException {
        File dir = folder.newFolder();
        File f1 = new File(dir, "test.txt");
        Utils.writeByteArrayToFile(f1, new byte[]{0, 0, 0}, false);
        File f2 = new File(dir, "test1.txt");
        Utils.writeByteArrayToFile(f2, new byte[]{0, 0, 0}, false);

        TorrentBuilder b = new TorrentBuilder();
        TorrentBuilder.Result r = b.path(dir)
                .flags(b.flags() | TorrentBuilder.Flags.MERKLE.swig())
                .generate();

        TorrentInfo ti = TorrentInfo.bdecode(r.entry().bencode());
        assertEquals(2, ti.numFiles());

        ArrayList<Sha1Hash> tree = r.merkleTree();
        assertTrue(tree.size() >= 0);
        ti.merkleTree(tree);
        assertEquals(tree.get(0), ti.merkleTree().get(0));
    }*/

    /*
    @Test
    public void testMerkleFlag() throws IOException {
        TorrentBuilder b = new TorrentBuilder();

        assertFalse(b.merkle());
        b.merkle(false);
        assertFalse(b.merkle());
        b.merkle(true);
        assertTrue(b.merkle());
        b.merkle(true);
        assertTrue(b.merkle());
        b.merkle(false);
        assertFalse(b.merkle());
        b.merkle(false);
        assertFalse(b.merkle());
    }*/
}
