package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.file_status_alert;
import com.frostwire.jlibtorrent.swig.int_vector;

/**
 * Posted in response to {@link com.frostwire.jlibtorrent.TorrentHandle#postFileStatus()}
 * (libtorrent 2.1): the files the torrent currently has open in its disk I/O backend.
 *
 * @author gubatron
 */
public final class FileStatusAlert extends TorrentAlert<file_status_alert> {

    FileStatusAlert(file_status_alert alert) {
        super(alert);
    }

    /**
     * @return the indices of the files the torrent has open
     */
    public int[] openFileIndices() {
        int_vector v = alert.get_open_file_indices();
        int size = (int) v.size();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = v.get(i);
        }
        return arr;
    }
}
