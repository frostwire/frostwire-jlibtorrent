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

import com.frostwire.jlibtorrent.alerts.*;
import com.frostwire.jlibtorrent.swig.block_finished_alert;
import com.frostwire.jlibtorrent.swig.metadata_received_alert;
import com.frostwire.jlibtorrent.swig.torrent_finished_alert;

/**
 * @author gubatron
 * @author aldenml
 */
public class TorrentAlertAdapter implements AlertListener {

    protected final TorrentHandle th;

    public TorrentAlertAdapter(TorrentHandle th) {
        this.th = th;
    }

    @Override
    public final boolean accept(Alert<?> alert) {
        if (!(alert instanceof TorrentAlert<?>)) {
            return false;
        }

        TorrentAlert<?> ta = (TorrentAlert<?>) alert;

        return ta.getSwig().getHandle().op_eq(th.getSwig());
    }

    @Override
    public void onAlert(Alert<?> alert) {
        int type = alert.getType();
        if (type == torrent_finished_alert.alert_type) {
            onFinished((TorrentFinishedAlert) alert);
        } else if (type == block_finished_alert.alert_type) {
            onBlockFinished((BlockFinishedAlert) alert);
        } else if (type == metadata_received_alert.alert_type) {
            onMetadataReceivedAlert((MetadataReceivedAlert) alert);
        }
    }

    public void onFinished(TorrentFinishedAlert alert) {
    }

    public void onBlockFinished(BlockFinishedAlert alert) {
    }

    public void onMetadataReceivedAlert(MetadataReceivedAlert alert) {
    }
}
