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
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.MetadataReceivedAlert;
import com.frostwire.jlibtorrent.swig.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @author gubatron
 * @author aldenml
 */
public final class GetMagnet {

    public static void main(String[] args) throws Exception {

        String uri = "magnet:?xt=urn:btih:86d0502ead28e495c9e67665340f72aa72fe304e&dn=Frostwire.5.3.6.+%5BWindows%5D&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80&tr=udp%3A%2F%2Ftracker.istole.it%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";
        final File downloadDir = new File("/Users/aldenml/Downloads/frostwire_installer.torrent");

        System.out.println("Using libtorrent version: " + LibTorrent.version());

        final Session s = new Session();

        add_torrent_params p = add_torrent_params.create_instance_no_storage();
        error_code ec = new error_code();
        libtorrent.parse_magnet_uri(uri, p, ec);
        torrent_handle th = s.getSwig().add_torrent(p);
        th.auto_managed(false);

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    List<Alert<?>> alerts = s.waitForAlerts(1000);
//                    for (Alert<?> alert : alerts) {
//                        //System.out.println("alert: " + alert.what() + " - " + alert.message());
//
//                        if (alert instanceof MetadataReceivedAlert) {
//
//                            metadata_received_alert a = ((MetadataReceivedAlert) alert).getSwig();
//                            torrent_handle th = a.getHandle();
//                            torrent_info ti = th.torrent_file();
//                            create_torrent ct = new create_torrent(ti);
//                            byte[] data = LibTorrent.vector2bytes(ct.generate().bencode());
//                            try {
//                                Files.write(downloadDir.toPath(), data);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            System.out.print("Torrent metadata received");
//                            System.exit(0);
//                        }
//
//                        System.out.println("Downloading magnet...");
//                    }
//                }
//            }
//        });
//
//        t.start();
//        t.join();
    }
}
