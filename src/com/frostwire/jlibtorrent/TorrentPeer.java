package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.torrent_peer;

/**
 * @author gubatron
 * @author aldenml
 */
public final class TorrentPeer {

    private final torrent_peer p;

    public TorrentPeer(torrent_peer p) {
        this.p = p;
    }

    public torrent_peer getSwig() {
        return p;
    }

    public long totalDownload() {
        return p.total_download().longValue();
    }

    public long totalUpload() {
        return p.total_upload().longValue();
    }
}
