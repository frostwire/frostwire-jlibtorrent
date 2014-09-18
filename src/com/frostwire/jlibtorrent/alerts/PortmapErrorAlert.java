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

import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.portmap_error_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when a NAT router was successfully found but some
// part of the port mapping request failed. It contains a text message that
// may help the user figure out what is wrong. This alert is not generated in
// case it appears the client is not running on a NAT:ed network or if it
// appears there is no NAT router that can be remote controlled to add port
// mappings.
public final class PortmapErrorAlert extends AbstractAlert<portmap_error_alert> {

    public PortmapErrorAlert(portmap_error_alert alert) {
        super(alert);
    }

    // refers to the mapping index of the port map that failed, i.e.
    // the index returned from add_mapping().
    public int getMapping() {
        return alert.getMapping();
    }

    // 0 for NAT-PMP and 1 for UPnP.
    public int getMapType() {
        return alert.getMap_type();
    }

    // tells you what failed.
    public error_code getError() {
        return alert.getError();
    }
}
