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

import com.frostwire.jlibtorrent.swig.tracker_announce_alert;

/**
 * This alert is generated each time a tracker announce is sent (or attempted to be sent).
 * <p/>
 * There are no extra data members in this alert. The url can be found in the base class
 * however.
 *
 * @author gubatron
 * @author aldenml
 */
public final class TrackerAnnounceAlert extends TrackerAlert<tracker_announce_alert> {

    public TrackerAnnounceAlert(tracker_announce_alert alert) {
        super(alert);
    }

    // specifies what event was sent to the tracker. It is defined as:
    //
    // 0. None
    // 1. Completed
    // 2. Started
    // 3. Stopped
    public int getEvent() {
        return alert.getEvent();
    }
}
