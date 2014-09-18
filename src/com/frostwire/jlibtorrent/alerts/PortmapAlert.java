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

import com.frostwire.jlibtorrent.swig.portmap_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated when a NAT router was successfully found and
// a port was successfully mapped on it. On a NAT:ed network with a NAT-PMP
// capable router, this is typically generated once when mapping the TCP
// port and, if DHT is enabled, when the UDP port is mapped.
public final class PortmapAlert extends AbstractAlert<portmap_alert> {

    public PortmapAlert(portmap_alert alert) {
        super(alert);
    }

    // refers to the mapping index of the port map that failed, i.e.
    // the index returned from add_mapping().
    public int getMapping() {
        return alert.getMapping();
    }

    // the external port allocated for the mapping.
    public int getExternalPort() {
        return alert.getExternal_port();
    }

    // 0 for NAT-PMP and 1 for UPnP.
    public int getMapType() {
        return alert.getMap_type();
    }
}
