package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * @author gubatron
 * @author aldenml
 * @deprecated from now on use {@link SettingsPack}, for now this will wrap SettingsPack for you.
 */
@Deprecated
public final class SessionSettings {

    private final settings_pack s;

    public SessionSettings(settings_pack s) {
        this.s = s;
    }

    public settings_pack getSwig() {
        return s;
    }

    public int getDownloadRateLimit() {
        return s.get_int(settings_pack.int_types.download_rate_limit.swigValue());
    }

    public void setDownloadRateLimit(int value) {
        s.set_int(settings_pack.int_types.download_rate_limit.swigValue(), value);
    }

    public int getUploadRateLimit() {
        return s.get_int(settings_pack.int_types.upload_rate_limit.swigValue());
    }

    public void setUploadRateLimit(int value) {
        s.set_int(settings_pack.int_types.upload_rate_limit.swigValue(), value);
    }

    public int getMaxQueuedDiskBytes() {
        return s.get_int(settings_pack.int_types.max_queued_disk_bytes.swigValue());
    }

    public void setMaxQueuedDiskBytes(int value) {
        s.set_int(settings_pack.int_types.max_queued_disk_bytes.swigValue(), value);
    }

    public int getSendBufferWatermark() {
        return s.get_int(settings_pack.int_types.send_buffer_watermark.swigValue());
    }

    public void setSendBufferWatermark(int value) {
        s.set_int(settings_pack.int_types.send_buffer_watermark.swigValue(), value);
    }

    public int getActiveDownloads() {
        return s.get_int(settings_pack.int_types.active_downloads.swigValue());
    }

    public void setActiveDownloads(int value) {
        s.set_int(settings_pack.int_types.active_downloads.swigValue(), value);
    }

    public int getActiveSeeds() {
        return s.get_int(settings_pack.int_types.active_seeds.swigValue());
    }

    public void setActiveSeeds(int value) {
        s.set_int(settings_pack.int_types.active_seeds.swigValue(), value);
    }

    public int getConnectionsLimit() {
        return s.get_int(settings_pack.int_types.connections_limit.swigValue());
    }

    public void setConnectionsLimit(int value) {
        s.set_int(settings_pack.int_types.connections_limit.swigValue(), value);
    }

    public int getMaxPeerlistSize() {
        return s.get_int(settings_pack.int_types.max_peerlist_size.swigValue());
    }

    public void setMaxPeerlistSize(int value) {
        s.set_int(settings_pack.int_types.max_peerlist_size.swigValue(), value);
    }

    public int getCacheSize() {
        return s.get_int(settings_pack.int_types.cache_size.swigValue());
    }

    public void setCacheSize(int value) {
        s.set_int(settings_pack.int_types.cache_size.swigValue(), value);
    }

    public boolean getUtpDynamicSockBuf() {
        return s.get_bool(settings_pack.bool_types.utp_dynamic_sock_buf.swigValue());
    }

    public void setUtpDynamicSockBuf(boolean value) {
        s.set_bool(settings_pack.bool_types.utp_dynamic_sock_buf.swigValue(), value);
    }

    public boolean getGuidedReadCache() {
        return s.get_bool(settings_pack.bool_types.guided_read_cache.swigValue());
    }

    public void setGuidedReadCache(boolean value) {
        s.set_bool(settings_pack.bool_types.guided_read_cache.swigValue(), value);
    }

    public int getTickInterval() {
        return s.get_int(settings_pack.int_types.tick_interval.swigValue());
    }

    public void setTickInterval(int value) {
        s.set_int(settings_pack.int_types.tick_interval.swigValue(), value);
    }

    public int getInactivityTimeout() {
        return s.get_int(settings_pack.int_types.inactivity_timeout.swigValue());
    }

    public void setInactivityTimeout(int value) {
        s.set_int(settings_pack.int_types.inactivity_timeout.swigValue(), value);
    }

    public void optimizeHashingForSpeed(boolean value) {
        // TODO
    }

    public boolean getSeedingOutgoingConnections() {
        return s.get_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue());
    }

    public void setSeedingOutgoingConnections(boolean value) {
        s.set_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue(), value);
    }

    public boolean broadcastLSD() {
        return s.get_bool(settings_pack.bool_types.broadcast_lsd.swigValue());
    }

    public void broadcastLSD(boolean value) {
        s.set_bool(settings_pack.bool_types.broadcast_lsd.swigValue(), value);
    }

    public SettingsPack toPack() {
        SettingsPack p = new SettingsPack();

        p.setDownloadRateLimit(this.getDownloadRateLimit());
        p.setUploadRateLimit(this.getUploadRateLimit());
        p.setMaxQueuedDiskBytes(this.getMaxQueuedDiskBytes());
        p.setSendBufferWatermark(this.getSendBufferWatermark());
        p.setActiveDownloads(this.getActiveDownloads());
        p.setActiveSeeds(this.getActiveSeeds());
        p.setConnectionsLimit(this.getConnectionsLimit());
        p.setMaxPeerlistSize(this.getMaxPeerlistSize());
        p.setCacheSize(this.getCacheSize());
        p.setUtpDynamicSockBuf(this.getUtpDynamicSockBuf());
        p.setGuidedReadCache(this.getGuidedReadCache());
        p.setTickInterval(this.getTickInterval());
        p.setInactivityTimeout(this.getInactivityTimeout());
        p.setSeedingOutgoingConnections(this.getSeedingOutgoingConnections());
        p.broadcastLSD(this.broadcastLSD());

        return p;
    }
}
