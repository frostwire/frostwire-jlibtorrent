/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.jlibtorrent.demo;

import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.nio.file.Files;

/**
 * @author gubatron
 * @author aldenml
 */
public final class LazyRead {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        byte[] data = Files.readAllBytes(torrentFile.toPath());

        char_vector buffer = LibTorrent.bytes2char_vector(data);
        lazy_entry e = new lazy_entry();
        error_code ec = new error_code();
        int ret = lazy_entry.bdecode(buffer, e, ec);

        if (ret != 0) {
            System.out.println("failed to decode torrent: " + ec.message());
            return;
        }

        torrent_info ti = new torrent_info(e);

        System.out.println(new Sha1Hash(ti.info_hash()));
        System.out.println(libtorrent.print_entry(e));
    }
}
