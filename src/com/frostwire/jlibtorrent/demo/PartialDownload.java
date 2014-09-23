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

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.entry_vector;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

/**
 * @author gubatron
 * @author aldenml
 */
public final class PartialDownload {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        byte[] data = Files.readAllBytes(torrentFile.toPath());
        entry e = entry.bdecode(Vectors.bytes2char_vector(data));

        entry_vector files = e.find_key("info").find_key("files").list_v();

        System.out.println("Files inside the torrent");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).find_key("path").list_v().get(0).to_string());
        }

        // Download only the first file
        byte[] priorities = new byte[(int) files.size()];
        priorities[0] = 7;
        for (int i = 1; i < files.size(); i++) {
            priorities[i] = 0;
        }

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(torrentFile, priorities, torrentFile.getParentFile());

        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new TorrentAlertAdapter(th) {
            @Override
            public void onBlockFinished(BlockFinishedAlert alert) {
                int p = (int) (th.getStatus().getProgress() * 100);
                System.out.println("Progress: " + p);
            }

            @Override
            public void onTorrentFinished(TorrentFinishedAlert alert) {
                System.out.print("Torrent finished");
                signal.countDown();
            }
        });

        signal.await();
    }
}
