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

    /**
     * Sets the session-global limits of download rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public void setDownloadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.download_rate_limit.swigValue(), value);
    }

    /**
     * Sets the session-global limits of upload rate limit, in
     * bytes per second.
     * <p/>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public void setUploadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.upload_rate_limit.swigValue(), value);
    }

    /**
     * `active_downloads`` controls how many active
     * downloading torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveDownloads(int value) {
        sp.set_int(settings_pack.int_types.active_downloads.swigValue(), value);
    }

    /**
     * ``active_seeds`` controls how many active seeding
     * torrents the queuing mechanism allows.
     * <p/>
     * The target number of active torrents is ``min(active_downloads +
     * active_seeds, active_limit)``. ``active_downloads`` and
     * ``active_seeds`` are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p/>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p/>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p/>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @param value
     */
    public void setActiveSeeds(int value) {
        sp.set_int(settings_pack.int_types.active_seeds.swigValue(), value);
    }

    /**
     * Sets a global limit on the number of connections opened. The number of
     * connections is set to a hard minimum of at least two per torrent, so
     * if you set a too low connections limit, and open too many torrents,
     * the limit will not be met.
     *
     * @param value
     */
    public void setConnectionsLimit(int value) {
        sp.set_int(settings_pack.int_types.connections_limit.swigValue(), value);
    }

    /**
     * Sets the maximum number of peers in the list of known peers. These peers
     * are not necessarily connected, so this number should be much greater
     * than the maximum number of connected peers. Peers are evicted from the
     * cache when the list grows passed 90% of this limit, and once the size
     * hits the limit, peers are no longer added to the list. If this limit
     * is set to 0, there is no limit on how many peers we'll keep in the
     * peer list.
     *
     * @param value
     */
    public void setMaxPeerlistSize(int value) {
        sp.set_int(settings_pack.int_types.max_peerlist_size.swigValue(), value);
    }

    /**
     * Sets the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled. This prevents
     * fast downloads to slow medias to allocate more memory indefinitely.
     * This should be set to at least 16 kB to not completely disrupt normal
     * downloads. If it's set to 0, you will be starving the disk thread and
     * nothing will be written to disk. this is a per session setting.
     * <p/>
     * When this limit is reached, the peer connections will stop reading
     * data from their sockets, until the disk thread catches up. Setting
     * this too low will severly limit your download rate.
     *
     * @param value
     */
    public void setMaxQueuedDiskBytes(int value) {
        sp.set_int(settings_pack.int_types.max_queued_disk_bytes.swigValue(), value);
    }

    /**
     * Sets the upper limit of the send buffer low-watermark.
     * <p/>
     * if the send buffer has fewer bytes than this, we'll read another 16kB
     * block onto it. If set too small, upload rate capacity will suffer. If
     * set too high, memory will be wasted. The actual watermark may be lower
     * than this in case the upload rate is low, this is the upper limit.
     *
     * @param value
     */
    public void setSendBufferWatermark(int value) {
        sp.set_int(settings_pack.int_types.send_buffer_watermark.swigValue(), value);
    }

    /**
     * Sets the disk write and read  cache. It is specified in units of 16 KiB
     * blocks. Buffers that are part of a peer's send or receive buffer also
     * count against this limit. Send and receive buffers will never be
     * denied to be allocated, but they will cause the actual cached blocks
     * to be flushed or evicted. If this is set to -1, the cache size is
     * automatically set to the amount of physical RAM available in the
     * machine divided by 8. If the amount of physical RAM cannot be
     * determined, it's set to 1024 (= 16 MiB).
     * <p/>
     * Disk buffers are allocated using a pool allocator, the number of
     * blocks that are allocated at a time when the pool needs to grow can be
     * specified in ``cache_buffer_chunk_size``. This defaults to 16 blocks.
     * Lower numbers saves memory at the expense of more heap allocations. It
     * must be at least 1.
     *
     * @param value
     */
    public void setCacheSize(int value) {
        sp.set_int(settings_pack.int_types.cache_size.swigValue(), value);
    }

    /**
     * Controls if the uTP socket manager is allowed to increase the socket
     * buffer if a network interface with a large MTU is used (such as
     * loopback or ethernet jumbo frames). This defaults to true and might
     * improve uTP throughput. For RAM constrained systems, disabling this
     * typically saves around 30kB in user space and probably around 400kB in
     * kernel socket buffers (it adjusts the send and receive buffer size on
     * the kernel socket, both for IPv4 and IPv6).
     *
     * @param value
     */
    public void setUtpDynamicSockBuf(boolean value) {
        sp.set_bool(settings_pack.bool_types.utp_dynamic_sock_buf.swigValue(), value);
    }

    /**
     * Enables the disk cache to adjust the size
     * of a cache line generated by peers to depend on the upload rate
     * you are sending to that peer. The intention is to optimize the RAM
     * usage of the cache, to read ahead further for peers that you're
     * sending faster to.
     *
     * @param value
     */
    public void setGuidedReadCache(boolean value) {
        sp.set_bool(settings_pack.bool_types.guided_read_cache.swigValue(), value);
    }

    /**
     * Specifies the number of milliseconds between internal ticks. This is
     * the frequency with which bandwidth quota is distributed to peers. It
     * should not be more than one second (i.e. 1000 ms). Setting this to a
     * low value (around 100) means higher resolution bandwidth quota
     * distribution, setting it to a higher value saves CPU cycles.
     *
     * @param value
     */
    public void setTickInterval(int value) {
        sp.set_int(settings_pack.int_types.tick_interval.swigValue(), value);
    }

    /**
     * if a peer is uninteresting and uninterested for longer than this
     * number of seconds, it will be disconnected. default is 10 minutes
     *
     * @param value
     */
    public void setInactivityTimeout(int value) {
        sp.set_int(settings_pack.int_types.inactivity_timeout.swigValue(), value);
    }

    /**
     * Determines if seeding (and finished) torrents should attempt to make
     * outgoing connections or not. By default this is true. It may be set to
     * false in very specific applications where the cost of making outgoing
     * connections is high, and there are no or small benefits of doing so.
     * For instance, if no nodes are behind a firewall or a NAT, seeds don't
     * need to make outgoing connections.
     *
     * @param value
     */
    public void setSeedingOutgoingConnections(boolean value) {
        sp.set_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue(), value);
    }

    /**
     * defaults to false. When set to true, the client tries to hide its
     * identity to a certain degree. The peer-ID will no longer include the
     * client's fingerprint. The user-agent will be reset to an empty string.
     * It will also try to not leak other identifying information, such as
     * your local listen port, your IP etc.
     * <p/>
     * If you're using I2P, a VPN or a proxy, it might make sense to enable
     * anonymous mode.
     *
     * @param value
     */
    public void setAnonymousMode(boolean value) {
        sp.set_bool(settings_pack.bool_types.anonymous_mode.swigValue(), value);
    }
}
