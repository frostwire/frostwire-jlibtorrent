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

import com.frostwire.jlibtorrent.swig.metadata_failed_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when the metadata has been completely received and the info-hash
// failed to match it. i.e. the metadata that was received was corrupt. libtorrent will
// automatically retry to fetch it in this case. This is only relevant when running a
// torrent-less download, with the metadata extension provided by libtorrent.
public final class MetadataFailedAlert extends TorrentAlert<metadata_failed_alert> {

    public MetadataFailedAlert(metadata_failed_alert alert) {
        super(alert);
    }
}
