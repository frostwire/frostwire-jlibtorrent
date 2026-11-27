package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Building a {@link FileStorage} by hand.
 *
 * @author gubatron
 * @author aldenml
 */
public class FileStorageWrapperTest {

    private static FileStorage storage() {
        FileStorage fs = new FileStorage(new com.frostwire.jlibtorrent.swig.file_storage());
        fs.name("root");
        fs.reserve(4);
        fs.pieceLength(16384);
        fs.addFile("root/a.txt", 20000);
        fs.addFile("root/dir/b.txt", 30000, FileStorage.FLAG_EXECUTABLE);
        fs.numPieces(4);
        return fs;
    }

    @Test
    public void accessorsOfAHandBuiltStorage() {
        FileStorage fs = storage();
        assertNotNull(fs.swig());
        assertNull(fs.ti());
        assertTrue(fs.isValid());
        assertEquals("root", fs.name());
        assertEquals(2, fs.numFiles());
        assertEquals(50000, fs.totalSize());
        assertEquals(16384, fs.pieceLength());
        assertEquals(4, fs.numPieces());
        assertEquals(16384, fs.pieceSize(0));
        assertEquals(50000 - 3 * 16384, fs.pieceSize(3));

        assertEquals("root" + File.separator + "a.txt", fs.filePath(0).replace('/', File.separatorChar));
        assertEquals("a.txt", fs.fileName(0));
        assertEquals("b.txt", fs.fileName(1));
        assertEquals(20000, fs.fileSize(0));
        assertEquals(30000, fs.fileSize(1));
        assertEquals(0, fs.fileOffset(0));
        assertEquals(20000, fs.fileOffset(1));
        assertFalse(fs.padFileAt(0));
        assertFalse(fs.fileAbsolutePath(0));
        assertEquals(0, fs.fileIndexAtOffset(10));
        assertEquals(1, fs.fileIndexAtOffset(20001));
        assertEquals("/save" + File.separator + fs.filePath(1), fs.filePath(1, "/save"));
        assertEquals(2, fs.paths().size());
        assertTrue(fs.paths().get(1).contains("dir"));
    }

    @Test
    public void flagsAndMappings() {
        FileStorage fs = storage();
        assertEquals(FileStorage.FLAG_EXECUTABLE.to_int(), fs.fileFlags(1).to_int() & FileStorage.FLAG_EXECUTABLE.to_int());
        assertEquals(0, fs.fileFlags(0).to_int() & FileStorage.FLAG_EXECUTABLE.to_int());
        assertNotNull(FileStorage.FLAG_PAD_FILE);
        assertNotNull(FileStorage.FLAG_HIDDEN);
        assertNotNull(FileStorage.FLAG_SYMLINK);

        List<FileSlice> slices = fs.mapBlock(1, 0, 16384);
        // piece 1 covers bytes 16384..32768: the tail of a.txt and the head of b.txt
        assertEquals(2, slices.size());
        assertEquals(0, slices.get(0).fileIndex());
        assertEquals(16384, slices.get(0).offset());
        assertEquals(20000 - 16384, slices.get(0).size());
        assertEquals(1, slices.get(1).fileIndex());
        assertEquals(0, slices.get(1).offset());
        assertEquals(16384 - (20000 - 16384), slices.get(1).size());
        assertTrue(slices.get(1).toString(), slices.get(1).toString().startsWith("FileSlice(fileIndex: 1"));

        PeerRequest r = fs.mapFile(1, 5, 10);
        assertEquals(1, r.piece());
        assertEquals(20000 - 16384 + 5, r.start());
        assertEquals(10, r.length());
        assertTrue(r.toString(), r.toString().length() > 0);
    }

    @Test
    public void everyAddFileOverloadAddsAFile() {
        FileStorage fs = new FileStorage(new com.frostwire.jlibtorrent.swig.file_storage());
        fs.name("o");
        fs.addFile("o/1", 1);
        fs.addFile("o/2", 2, FileStorage.FLAG_HIDDEN);
        fs.addFile("o/3", 3, FileStorage.FLAG_HIDDEN, 1_600_000_000);
        fs.addFile("o/4", 4, FileStorage.FLAG_SYMLINK, 1_600_000_000, "o/1");
        assertEquals(4, fs.numFiles());
        assertEquals(10, fs.totalSize());
    }
}
