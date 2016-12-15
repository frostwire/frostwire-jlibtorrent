package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.BDecodeNode;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.Utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author gubatron
 * @author aldenml
 */
public final class ReadBEncoded {

    public static void main(String[] args) throws Throwable {
        args = new String[]{"/Users/aldenml/Downloads/303dde355f99c9b903efaeba57e23194a7a6713f.resume"};

        byte[] data = Utils.readFileToByteArray(new File(args[0]));

        BDecodeNode n = BDecodeNode.bdecode(data);

        System.out.println(n);
    }
}
