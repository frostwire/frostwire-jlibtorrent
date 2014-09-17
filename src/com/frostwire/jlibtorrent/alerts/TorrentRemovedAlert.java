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

import com.frostwire.jlibtorrent.swig.torrent_removed_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// The ``torrent_removed_alert`` is posted whenever a torrent is removed. Since
// the torrent handle in its baseclass will always be invalid (since the torrent
// is already removed) it has the info hash as a member, to identify it.
// It's posted when the ``status_notification`` bit is set in the alert_mask.
//
// Even though the ``handle`` member doesn't point to an existing torrent anymore,
// it is still useful for comparing to other handles, which may also no
// longer point to existing torrents, but to the same non-existing torrents.
//
// The ``torrent_handle`` acts as a ``weak_ptr``, even though its object no
// longer exists, it can still compare equal to another weak pointer which
// points to the same non-existent object.
public final class TorrentRemovedAlert extends TorrentAlert<torrent_removed_alert> {

    public TorrentRemovedAlert(torrent_removed_alert alert) {
        super(alert);
    }
}
