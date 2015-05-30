package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.policy;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentPeer {

    private final policy.peer p;

    public TorrentPeer(policy.peer p) {
        this.p = p;
    }

    public policy.peer getSwig() {
        return p;
    }

    public long totalDownload() {
        return p.total_download();
    }

    public long totalUpload() {
        return p.total_upload();
    }
}
