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

import com.frostwire.jlibtorrent.swig.portmap_log_alert;

/**
 * @author gubatron
 * @author aldenml
 */
// This alert is generated to log informational events related to either
// UPnP or NAT-PMP. They contain a log line and the type (0 = NAT-PMP
// and 1 = UPnP). Displaying these messages to an end user is only useful
// for debugging the UPnP or NAT-PMP implementation.
public final class PortmapLogAlert extends AbstractAlert<portmap_log_alert> {

    public PortmapLogAlert(portmap_log_alert alert) {
        super(alert);
    }

    public int getMapType() {
        return alert.getMap_type();
    }

    public String getMessage() {
        return alert.getMsg();
    }
}
