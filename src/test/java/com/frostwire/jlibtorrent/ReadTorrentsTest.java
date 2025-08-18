package com.frostwire.jlibtorrent;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ReadTorrentsTest {
    @Test
    public void readHybridTorrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-hybrid-test.torrent");
        TorrentInfo tiFromBytes = new TorrentInfo(torrentFileBytes);
        System.out.println("readHybridTorrent: info-hash V1 from bytes: " + tiFromBytes.infoHashV1());
        assertEquals("d8dd32ac93357c368556af3ac1d95c9d76bd0dff", tiFromBytes.infoHashV1().toHex());
        assertNotNull("readHybridTorrent: info-hash V2 from bytes should not be null", tiFromBytes.infoHashV2());
        System.out.println("readHybridTorrent: info-hash V2 from bytes: " + tiFromBytes.infoHashV2());
        assertEquals("d8dd32ac93357c368556af3ac1d95c9d76bd0dff6fa9833ecdac3d53134efabb", tiFromBytes.infoHashV2().toHex());

        FileStorage file_storage = tiFromBytes.files();
        System.out.println("readHybridTorrent: file storage is valid?: " + file_storage.isValid());
        if (file_storage.isValid()) {
            for (int i = 0; i < tiFromBytes.numFiles(); i++) {
                String file_name = file_storage.fileName(i);
                String file_path = file_storage.filePath(i);
                long file_size = file_storage.fileSize(i);
                Sha1Hash hash = file_storage.hash(i);
                System.out.println("readHybridTorrent: file[" + i + "]: name: " + file_name + ", path: " + file_path + ", size: " + file_size + ", hash: " + hash);
            }
        }
    }

    @Test
    public void readV2Torrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("bittorrent-v2-test.torrent");
        TorrentInfo tiFromBytes = new TorrentInfo(torrentFileBytes);
        System.out.println("readV2Torrent: info-hash V1 from bytes: " + tiFromBytes.infoHashV1());
        assertEquals("caf1e1c30e81cb361b9ee167c4aa64228a7fa4fa", tiFromBytes.infoHashV1().toHex());
        assertNotNull("readV2Torrent: info-hash V2 from bytes should not be null", tiFromBytes.infoHashV2());
        System.out.println("readV2Torrent: info-hash V2 from bytes: " + tiFromBytes.infoHashV2());
        assertEquals("caf1e1c30e81cb361b9ee167c4aa64228a7fa4fa9f6105232b28ad099f3a302e", tiFromBytes.infoHashV2().toHex());
        System.out.println("readV2Torrent: number of files: " + tiFromBytes.numFiles());

        FileStorage file_storage = tiFromBytes.files();
        System.out.println("readV2Torrent: file storage is valid?: " + file_storage.isValid());
        if (file_storage.isValid()) {
            for (int i = 0; i < tiFromBytes.numFiles(); i++) {
                String file_name = file_storage.fileName(i);
                String file_path = file_storage.filePath(i);
                long file_size = file_storage.fileSize(i);
                Sha1Hash hash = file_storage.hash(i);
                System.out.println("readV2Torrent: file[" + i + "]: name: " + file_name + ", path: " + file_path + ", size: " + file_size + ", hash: " + hash);
            }
        }
    }

    @Test
    public void readV1Torrent() throws IOException {
        byte[] torrentFileBytes = Utils.resourceBytes("test1.torrent");
        TorrentInfo tiFromBytes = new TorrentInfo(torrentFileBytes);
        System.out.println("readV1Torrent: info-hash V1 from bytes: " + tiFromBytes.infoHashV1());
        assertEquals("5e81e226f98e5ddc48a4392e673e6cf9f667f5b0", tiFromBytes.infoHashV1().toHex());
        assertNull("readV1Torrent: info-hash V2 from bytes should be null", tiFromBytes.infoHashV2());

        FileStorage file_storage = tiFromBytes.files();
        System.out.println("readV1Torrent: file storage is valid?: " + file_storage.isValid());
        if (file_storage.isValid()) {
            for (int i = 0; i < tiFromBytes.numFiles(); i++) {
                String file_name = file_storage.fileName(i);
                String file_path = file_storage.filePath(i);
                long file_size = file_storage.fileSize(i);
                Sha1Hash hash = file_storage.hash(i);
                System.out.println("readV1Torrent: file[" + i + "]: name: " + file_name + ", path: " + file_path + ", size: " + file_size + ", hash: " + hash);
            }
        }
    }
}
