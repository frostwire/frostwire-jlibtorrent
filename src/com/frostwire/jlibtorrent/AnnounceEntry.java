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

import com.frostwire.jlibtorrent.swig.announce_entry;

/**
 * This class holds information about one bittorrent tracker, as it
 * relates to a specific torrent.
 *
 * @author gubatron
 * @author aldenml
 */
public class AnnounceEntry {

    private final announce_entry e;

    public AnnounceEntry(announce_entry e) {
        this.e = e;
    }

    public AnnounceEntry(String url) {
        this(new announce_entry(url));
    }

    public announce_entry getSwig() {
        return e;
    }

    /**
     * Tracker URL as it appeared in the torrent file.
     *
     * @return
     */
    public String getUrl() {
        return e.getUrl();
    }
}
