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

import com.frostwire.jlibtorrent.swig.torrent_added_alert;

/**
 * The TorrentAddedAlert is posted once every time a torrent is successfully
 * added. It doesn't contain any members of its own, but inherits the torrent handle
 * from its base class.
 * It's posted when the ``status_notification`` bit is set in the alert_mask.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TorrentAddedAlert extends TorrentAlert<torrent_added_alert> {

    public TorrentAddedAlert(torrent_added_alert alert) {
        super(alert);
    }
}
