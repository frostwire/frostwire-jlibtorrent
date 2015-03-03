package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.settings_pack;

/**
 * The ``settings_pack`` struct, contains the names of all settings as
 * enum values. These values are passed in to the ``set_str()``,
 * ``set_int()``, ``set_bool()`` functions, to specify the setting to
 * change.
 *
 * @author gubatron
 * @author aldenml
 */
public final class SettingsPack {

    private final settings_pack sp;

    public SettingsPack(settings_pack sp) {
        this.sp = sp;
    }

    public SettingsPack() {
        this(new settings_pack());
    }

    public settings_pack getSwig() {
        return sp;
    }

    public boolean getBoolean(int name) {
        return sp.get_bool(name);
    }

    public void setBoolean(int name, boolean value) {
        sp.set_bool(name, value);
    }

    public int getInteger(int name) {
        return sp.get_int(name);
    }

    public void setInteger(int name, int value) {
        sp.set_int(name, value);
    }

    public String getString(int name) {
        return sp.get_str(name);
    }

    public void setString(int name, String value) {
        sp.set_str(name, value);
    }

    public void setDownloadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.download_rate_limit.swigValue(), value);
    }

    public void setUploadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.upload_rate_limit.swigValue(), value);
    }

    public void setActiveDownloads(int value) {
        sp.set_int(settings_pack.int_types.active_downloads.swigValue(), value);
    }

    public void setActiveSeeds(int value) {
        sp.set_int(settings_pack.int_types.active_seeds.swigValue(), value);
    }

    public void setConnectionsLimit(int value) {
        sp.set_int(settings_pack.int_types.connections_limit.swigValue(), value);
    }

    public void setMaxPeerlistSize(int value) {
        sp.set_int(settings_pack.int_types.max_peerlist_size.swigValue(), value);
    }

    public void setMaxQueuedDiskBytes(int value) {
        sp.set_int(settings_pack.int_types.max_queued_disk_bytes.swigValue(), value);
    }

    public void setSendBufferWatermark(int value) {
        sp.set_int(settings_pack.int_types.send_buffer_watermark.swigValue(), value);
    }

    public void setCacheSize(int value) {
        sp.set_int(settings_pack.int_types.cache_size.swigValue(), value);
    }

    public void setUtpDynamicSockBuf(boolean value) {
        sp.set_bool(settings_pack.bool_types.utp_dynamic_sock_buf.swigValue(), value);
    }

    public void setGuidedReadCache(boolean value) {
        sp.set_bool(settings_pack.bool_types.guided_read_cache.swigValue(), value);
    }

    public void setTickInterval(int value) {
        sp.set_int(settings_pack.int_types.tick_interval.swigValue(), value);
    }

    public void setInactivityTimeout(int value) {
        sp.set_int(settings_pack.int_types.inactivity_timeout.swigValue(), value);
    }

    public void setSeedingOutgoingConnections(boolean value) {
        sp.set_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue(), value);
    }
}
