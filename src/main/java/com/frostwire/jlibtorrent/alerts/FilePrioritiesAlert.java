package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Priority;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.file_priorities_alert;

/**
 * Posted in response to {@link com.frostwire.jlibtorrent.TorrentHandle#postFilePriorities()}
 * (libtorrent 2.1): the priority of every file in the torrent, read without blocking on the
 * session the way {@code TorrentHandle.filePriorities()} does.
 *
 * @author gubatron
 */
public final class FilePrioritiesAlert extends TorrentAlert<file_priorities_alert> {

    FilePrioritiesAlert(file_priorities_alert alert) {
        super(alert);
    }

    /**
     * @return the priority of each file, indexed by file index
     */
    public Priority[] priorities() {
        byte_vector v = alert.get_priorities();
        int size = (int) v.size();
        Priority[] arr = new Priority[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Priority.fromSwig(v.get(i));
        }
        return arr;
    }
}
