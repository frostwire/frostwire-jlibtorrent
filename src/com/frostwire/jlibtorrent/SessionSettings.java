package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.session_settings;
import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * @author gubatron
 * @author aldenml
 */
public final class SessionSettings {

    private final session_settings s;

    public SessionSettings(session_settings s) {
        this.s = s;
    }

    public session_settings getSwig() {
        return s;
    }

    public int getDownloadRateLimit() {
        return s.get_int(settings_pack.int_types.download_rate_limit.swigValue());
    }

    public int getUploadRateLimit() {
        return s.get_int(settings_pack.int_types.upload_rate_limit.swigValue());
    }

    public int getMaxQueuedDiskBytes() {
        return s.get_int(settings_pack.int_types.max_queued_disk_bytes.swigValue());
    }

    public int getSendBufferWatermark() {
        return s.get_int(settings_pack.int_types.send_buffer_watermark.swigValue());
    }

    public int getActiveDownloads() {
        return s.get_int(settings_pack.int_types.active_downloads.swigValue());
    }

    public int getActiveSeeds() {
        return s.get_int(settings_pack.int_types.active_seeds.swigValue());
    }

    public int getConnectionsLimit() {
        return s.get_int(settings_pack.int_types.connections_limit.swigValue());
    }

    public int getMaxPeerlistSize() {
        return s.get_int(settings_pack.int_types.max_peerlist_size.swigValue());
    }
}
