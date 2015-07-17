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
        return stats.totalDownloadRate();
    }

    public long getUploadRate() {
        return stats.totalUploadRate();
    }

    public long getTotalDownload() {
        return stats.getTotalDownload();
    }

    public long getTotalUpload() {
        return stats.getTotalUpload();
    }
}
