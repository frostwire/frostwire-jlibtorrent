package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.file_progress_alert;
import com.frostwire.jlibtorrent.swig.int64_vector;

public class FileProgressAlert extends TorrentAlert<file_progress_alert> {
    public FileProgressAlert(file_progress_alert fileProgressAlert) {
        super(fileProgressAlert);
    }
    /**
     * the list of the files in the torrent
     */
    public long[] getFiles() {
        int64_vector files = alert.get_files();
        return Vectors.int64_vector2longs(files);
    }
}
