package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.oversized_file_alert;

/**
 * This alert may be posted when the initial checking of resume data and files
 * on disk (just existence, not piece hashes) completes. If a file belonging
 * to the torrent is found on disk but is larger than the file in the
 * torrent, that's when this alert is posted.
 * <p>
 * The client may want to call truncate_files() in that case, or perhaps
 * interpret it as a sign that some other file is in the way, that shouldn't
 * be overwritten.
 */
public final class OversizedFileAlert extends TorrentAlert<oversized_file_alert> {

    OversizedFileAlert(oversized_file_alert alert) {
        super(alert);
    }
}