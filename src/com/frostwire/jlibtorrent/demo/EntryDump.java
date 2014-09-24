package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Utils;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.entry;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class EntryDump {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/test.torrent"};

        File file = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        byte[] data = Utils.readFileToByteArray(file);

        entry e = entry.bdecode(Vectors.bytes2char_vector(data));

        System.out.println(e.to_string());
    }
}
