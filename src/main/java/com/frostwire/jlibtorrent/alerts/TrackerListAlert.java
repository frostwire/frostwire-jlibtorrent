package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.AnnounceEntry;
import com.frostwire.jlibtorrent.swig.announce_entry;
import com.frostwire.jlibtorrent.swig.announce_entry_vector;
import com.frostwire.jlibtorrent.swig.tracker_list_alert;

import java.util.ArrayList;

public final class TrackerListAlert extends TorrentAlert<tracker_list_alert> {

    TrackerListAlert(tracker_list_alert alert) {
        super(alert);
    }

    /**
     * list of trackers and their status for the torrent.
     */
    public ArrayList<AnnounceEntry> getTrackers() {
        announce_entry_vector v = alert.getTrackers();
        int size = v.size();
        ArrayList<AnnounceEntry> trackers = new ArrayList<>(size);

        for (announce_entry e : v) {
            trackers.add(new AnnounceEntry(e));
        }

        return trackers;
    }
}