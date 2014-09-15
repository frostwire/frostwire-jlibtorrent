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

package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.torrent_finished_alert;

import java.io.File;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class Main {

    public static void main(String[] args) throws Throwable {

        // comment this line for a real application
        args = new String[]{"/Users/aldenml/Downloads/Kellee_Maize_The_5th_Element_FrostClick_FrostWire_MP3_April_14_2014.torrent"};

        File torrentFile = new File(args[0]);

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final Session s = new Session();

        final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<alert> alerts = s.waitForAlerts(1000);
                    for (alert alert : alerts) {
                        //System.out.println("alert: " + alert.what() + " - " + alert.message());

                        if (alert instanceof torrent_finished_alert) {
                            System.out.print("Torrent finished");
                            System.exit(0);
                        }

                        int p = (int) (th.getStatus().progress * 100);
                        System.out.println("Progress: " + p);
                    }
                }
            }
        });

        t.start();
        t.join();
    }
}
