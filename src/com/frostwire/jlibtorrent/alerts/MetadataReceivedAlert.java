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

package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.metadata_received_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when the metadata has been completely received and the torrent
// can start downloading. It is not generated on torrents that are started with metadata, but
// only those that needs to download it from peers (when utilizing the libtorrent extension).
//
// There are no additional data members in this alert.
//
// Typically, when receiving this alert, you would want to save the torrent file in order
// to load it back up again when the session is restarted. Here's an example snippet of
// code to do that::
//
//	torrent_handle h = alert->handle();
//	if (h.is_valid()) {
//		boost::intrusive_ptr<torrent_info const> ti = h.torrent_file();
//		create_torrent ct(*ti);
//		entry te = ct.generate();
//		std::vector<char> buffer;
//		bencode(std::back_inserter(buffer), te);
//		FILE* f = fopen((to_hex(ti->info_hash().to_string()) + ".torrent").c_str(), "wb+");
//		if (f) {
//			fwrite(&buffer[0], 1, buffer.size(), f);
//			fclose(f);
//		}
//	}
//
public final class MetadataReceivedAlert extends TorrentAlert<metadata_received_alert> {

    public MetadataReceivedAlert(metadata_received_alert alert) {
        super(alert);
    }
}
