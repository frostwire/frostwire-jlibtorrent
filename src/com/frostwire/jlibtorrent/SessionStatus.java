package com.frostwire.jlibtorrent;

/**
 * @author gubatron
 * @author aldenml
 */
@Deprecated
public final class SessionStatus {

    private final SessionStats stats;

    public SessionStatus(SessionStats stats) {
        this.stats = stats;
    }

    public long getDownloadRate() {
        return 0;
    }

    public long getUploadRate() {
        return 0;
    }

    public long getTotalDownload() {
        return stats.getTotalDownload();
    }

    public long getTotalUpload() {
        return stats.getTotalUpload();
    }
}
