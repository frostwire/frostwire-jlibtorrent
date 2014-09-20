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

import com.frostwire.jlibtorrent.swig.session_proxy;

/**
 * @author gubatron
 * @author aldenml
 */
// this is a holder for the internal session implementation object. Once the
// session destruction is explicitly initiated, this holder is used to
// synchronize the completion of the shutdown. The lifetime of this object
// may outlive session, causing the session destructor to not block. The
// session_proxy destructor will block however, until the underlying session
// is done shutting down.
public final class SessionProxy {

    private final session_proxy sp;

    public SessionProxy(session_proxy sp) {
        this.sp = sp;
    }

    public session_proxy getSwig() {
        return sp;
    }
}
