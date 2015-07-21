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
        return stats.downloadRate();
    }

    public long getUploadRate() {
        return stats.uploadRate();
    }

    public long getTotalDownload() {
        return stats.download();
    }

    public long getTotalUpload() {
        return stats.upload();
    }
}
