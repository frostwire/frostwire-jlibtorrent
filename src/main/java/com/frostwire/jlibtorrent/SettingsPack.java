package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.settings_pack;
import com.frostwire.jlibtorrent.swig.settings_pack.string_types;

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

    /**
     * Example, how to turn on the DHT using SettingsPack.
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     * pack.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), on);
     * s.applySettings(pack);
     * }</pre>
     */
    public SettingsPack() {
        this(new settings_pack());
    }

    /**
     * @return
     */
    public settings_pack swig() {
        return sp;
    }

    /**
     * @param name
     * @return
     */
    public boolean getBoolean(int name) {
        return sp.get_bool(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setBoolean(int name, boolean value) {
        sp.set_bool(name, value);
        return this;
    }

    /**
     * @param name
     * @return
     */
    public int getInteger(int name) {
        return sp.get_int(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setInteger(int name, int value) {
        sp.set_int(name, value);
        return this;
    }

    /**
     * @param name
     * @return
     */
    public String getString(int name) {
        return sp.get_str(name);
    }

    /**
     * @param name
     * @param value
     */
    public SettingsPack setString(int name, String value) {
        sp.set_str(name, value);
        return this;
    }

    public byte[] getBytes(int name) {
        byte_vector v = sp.get_bytes(name);
        return Vectors.byte_vector2bytes(v);
    }

    public void setBytes(int name, byte[] value) {
        byte_vector v = Vectors.bytes2byte_vector(value);
        sp.set_bytes(name, v);
    }

    public void clear() {
        sp.clear();
    }

    public void clear(int name) {
        sp.clear(name);
    }

    /**
     * Queries whether the specified configuration option has a value set in
     * this pack. ``name`` can be any enumeration value from string_types,
     * int_types or bool_types.
     *
     * @param name name id of the setting
     * @return true if present
     */
    public boolean hasValue(int name) {
        return sp.has_val(name);
    }

    /**
     * The fingerprint for the client.
     *
     * It will be used as the prefix to the peer-id. If this is 20 bytes (or longer)
     * it will be truncated to 20 bytes and used as the entire peer-id.
     */
    public byte[] getPeerFingerprint() {
        return getBytes(string_types.peer_fingerprint.swigValue());
    }

    /**
     * The fingerprint for the client.
     *
     * It will be used as the prefix to the peer-id. If this is 20 bytes (or longer)
     * it will be truncated to 20 bytes and used as the entire peer-id.
     */
    public void setPeerFingerprint(byte[] value) {
        setBytes(string_types.peer_fingerprint.swigValue(), value);
    }

    /**
     * @return the session-global download rate limit in bytes per second. (0 for unlimited)
     */
    public int downloadRateLimit() {
        return sp.get_int(settings_pack.int_types.download_rate_limit.swigValue());
    }

    /**
     * Sets the session-global limits of download rate limit, in
     * bytes per second.
     * <p>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public SettingsPack downloadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.download_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * @return the session-global upload rate limit in bytes per second. (0 for unlimited)
     */
    public int uploadRateLimit() {
        return sp.get_int(settings_pack.int_types.upload_rate_limit.swigValue());
    }

    /**
     * Sets the session-global limits of upload rate limit, in
     * bytes per second.
     * <p>
     * A value of 0 means unlimited.
     *
     * @param value
     */
    public SettingsPack uploadRateLimit(int value) {
        sp.set_int(settings_pack.int_types.upload_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_downloads} controls how many active
     * downloading torrents the queuing mechanism allows.
     * <p>
     * The target number of active torrents is {@code min(active_downloads +
     * active_seeds, active_limit)}. {@code active_downloads} and
     * {@code active_seeds} are upper limits on the number of downloading
     * torrents and seeding torrents respectively. Setting the value to -1
     * means unlimited.
     * <p>
     * For auto managed torrents, these are the limits they are subject to.
     * If there are too many torrents some of the auto managed ones will be
     * paused until some slots free up.
     * <p>
     * You can have more torrents *active*, even though they are not
     * announced to the DHT, lsd or their tracker. If some peer knows about
     * you for any reason and tries to connect, it will still be accepted,
     * unless the torrent is paused, which means it won't accept any
     * connections.
     * <p>
     * For example if there are 10 seeding torrents and 10 downloading
     * torrents, and ``active_downloads`` is 4 and ``active_seeds`` is 4,
     * there will be 4 seeds active and 4 downloading torrents. If the
     * settings are ``active_downloads`` = 2 and ``active_seeds`` = 4, then
     * there will be 2 downloading torrents and 4 seeding torrents active.
     * Torrents that are not auto managed are also counted against these
     * limits. If there are non-auto managed torrents that use up all the
     * slots, no auto managed torrent will be activated.
     *
     * @return
     */
    public int activeDownloads() {
        return sp.get_int(settings_pack.int_types.active_downloads.swigValue());
    }

    /**
     * @param value
     * @see #activeDownloads()
     */
    public SettingsPack activeDownloads(int value) {
        sp.set_int(settings_pack.int_types.active_downloads.swigValue(), value);
        return this;
    }

    /**
     * {@code active_seeds} controls how many active seeding
     * torrents the queuing mechanism allows.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeSeeds() {
        return sp.get_int(settings_pack.int_types.active_seeds.swigValue());
    }

    /**
     * @param value
     * @see #activeSeeds()
     */
    public SettingsPack activeSeeds(int value) {
        sp.set_int(settings_pack.int_types.active_seeds.swigValue(), value);
        return this;
    }

    /**
     * {@code active_checking} is the limit of number of simultaneous checking
     * torrents.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeChecking() {
        return sp.get_int(settings_pack.int_types.active_checking.swigValue());
    }

    /**
     * @param value
     * @see #activeChecking()
     */
    public SettingsPack activeChecking(int value) {
        sp.set_int(settings_pack.int_types.active_checking.swigValue(), value);
        return this;
    }

    /**
     * {@code active_dht_limit} is the max number of torrents to announce to
     * the DHT. By default this is set to 88, which is no more than one
     * DHT announce every 10 seconds.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeDhtLimit() {
        return sp.get_int(settings_pack.int_types.active_dht_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeDhtLimit()
     */
    public SettingsPack activeDhtLimit(int value) {
        sp.set_int(settings_pack.int_types.active_dht_limit.swigValue(), value);
        return this;
    }

    public int dhtUploadRate() {
        return sp.get_int(settings_pack.int_types.dht_upload_rate_limit.swigValue());
    }

    public SettingsPack dhtUploadRate(int value) {
        sp.set_int(settings_pack.int_types.dht_upload_rate_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_tracker_limit} is the max number of torrents to announce
     * to their trackers. By default this is 360, which is no more than
     * one announce every 5 seconds.
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeTrackerLimit() {
        return sp.get_int(settings_pack.int_types.active_tracker_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeTrackerLimit()
     */
    public SettingsPack activeTrackerLimit(int value) {
        sp.set_int(settings_pack.int_types.active_tracker_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_lsd_limit} is the max number of torrents to announce to
     * the local network over the local service discovery protocol. By
     * default this is 80, which is no more than one announce every 5
     * seconds (assuming the default announce interval of 5 minutes).
     *
     * @return
     * @see #activeDownloads()
     */
    public int activeLsdLimit() {
        return sp.get_int(settings_pack.int_types.active_lsd_limit.swigValue());
    }

    /**
     * @param value
     * @see #activeLsdLimit()
     */
    public SettingsPack activeLsdLimit(int value) {
        sp.set_int(settings_pack.int_types.active_lsd_limit.swigValue(), value);
        return this;
    }

    /**
     * {@code active_limit} is a hard limit on the number of active (auto
     * managed) torrents. This limit also applies to slow torrents.
     *
     * @return the value
     * @see #activeDownloads()
     */
    public int activeLimit() {
        return sp.get_int(settings_pack.int_types.active_limit.swigValue());
    }

    /**
     * {@code active_limit} is a hard limit on the number of active (auto
     * managed) torrents. This limit also applies to slow torrents.
     *
     * @param value the value
     * @see #activeLimit()
     */
    public SettingsPack activeLimit(int value) {
        sp.set_int(settings_pack.int_types.active_limit.swigValue(), value);
        return this;
    }

    /**
     * @return global limit on the number of connections opened.
     */
    public int connectionsLimit() {
        return sp.get_int(settings_pack.int_types.connections_limit.swigValue());
    }

    /**
     * Sets a global limit on the number of connections opened. The number of
     * connections is set to a hard minimum of at least two per torrent, so
     * if you set a too low connections limit, and open too many torrents,
     * the limit will not be met.
     *
     * @param value
     */
    public SettingsPack connectionsLimit(int value) {
        sp.set_int(settings_pack.int_types.connections_limit.swigValue(), value);
        return this;
    }

    /**
     * @return the maximum number of peers in the list of known peers. (0 for unlimited)
     */
    public int maxPeerlistSize() {
        return sp.get_int(settings_pack.int_types.max_peerlist_size.swigValue());
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
    public SettingsPack maxPeerlistSize(int value) {
        sp.set_int(settings_pack.int_types.max_peerlist_size.swigValue(), value);
        return this;
    }

    /**
     * @return the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled.
     */
    public int maxQueuedDiskBytes() {
        return sp.get_int(settings_pack.int_types.max_queued_disk_bytes.swigValue());
    }

    /**
     * Sets the maximum number of bytes a connection may have pending in the disk
     * write queue before its download rate is being throttled. This prevents
     * fast downloads to slow medias to allocate more memory indefinitely.
     * This should be set to at least 16 kB to not completely disrupt normal
     * downloads. If it's set to 0, you will be starving the disk thread and
     * nothing will be written to disk. this is a per session setting.
     * <p>
     * When this limit is reached, the peer connections will stop reading
     * data from their sockets, until the disk thread catches up. Setting
     * this too low will severely limit your download rate.
     *
     * @param value
     */
    public SettingsPack maxQueuedDiskBytes(int value) {
        sp.set_int(settings_pack.int_types.max_queued_disk_bytes.swigValue(), value);
        return this;
    }

    /**
     * @return the upper limit of the send buffer low-watermark.
     */
    public int sendBufferWatermark() {
        return sp.get_int(settings_pack.int_types.send_buffer_watermark.swigValue());
    }

    /**
     * Sets the upper limit of the send buffer low-watermark.
     * <p>
     * if the send buffer has fewer bytes than this, we'll read another 16kB
     * block onto it. If set too small, upload rate capacity will suffer. If
     * set too high, memory will be wasted. The actual watermark may be lower
     * than this in case the upload rate is low, this is the upper limit.
     *
     * @param value
     */
    public SettingsPack sendBufferWatermark(int value) {
        sp.set_int(settings_pack.int_types.send_buffer_watermark.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public int tickInterval() {
        return sp.get_int(settings_pack.int_types.tick_interval.swigValue());
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
    public SettingsPack tickInterval(int value) {
        sp.set_int(settings_pack.int_types.tick_interval.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public int inactivityTimeout() {
        return sp.get_int(settings_pack.int_types.inactivity_timeout.swigValue());
    }

    /**
     * if a peer is uninteresting and uninterested for longer than this
     * number of seconds, it will be disconnected. default is 10 minutes
     *
     * @param value
     */
    public SettingsPack inactivityTimeout(int value) {
        sp.set_int(settings_pack.int_types.inactivity_timeout.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public boolean seedingOutgoingConnections() {
        return sp.get_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue());
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
    public SettingsPack seedingOutgoingConnections(boolean value) {
        sp.set_bool(settings_pack.bool_types.seeding_outgoing_connections.swigValue(), value);
        return this;
    }

    /**
     * @return
     */
    public boolean anonymousMode() {
        return sp.get_bool(settings_pack.bool_types.anonymous_mode.swigValue());
    }

    /**
     * defaults to false. When set to true, the client tries to hide its
     * identity to a certain degree. The peer-ID will no longer include the
     * client's fingerprint. The user-agent will be reset to an empty string.
     * It will also try to not leak other identifying information, such as
     * your local listen port, your IP etc.
     * <p>
     * If you're using I2P, a VPN or a proxy, it might make sense to enable
     * anonymous mode.
     *
     * @param value
     */
    public SettingsPack anonymousMode(boolean value) {
        sp.set_bool(settings_pack.bool_types.anonymous_mode.swigValue(), value);
        return this;
    }

    /**
     * Starts the dht node and makes the trackerless service available to
     * torrents.
     *
     * @return true if enable
     */
    public boolean isEnableDht() {
        return sp.get_bool(settings_pack.bool_types.enable_dht.swigValue());
    }

    /**
     * Starts the dht node and makes the trackerless service available to
     * torrents.
     *
     * @param value true if enable
     */
    public void setEnableDht(boolean value) {
        sp.set_bool(settings_pack.bool_types.enable_dht.swigValue(), value);
    }

    /**
     * Starts and stops Local Service Discovery. This service will
     * broadcast the info-hashes of all the non-private torrents on the
     * local network to look for peers on the same swarm within multicast
     * reach.
     *
     * @return true if enable
     */
    public boolean isEnableLsd() {
        return sp.get_bool(settings_pack.bool_types.enable_lsd.swigValue());
    }

    /**
     * Starts and stops Local Service Discovery. This service will
     * broadcast the info-hashes of all the non-private torrents on the
     * local network to look for peers on the same swarm within multicast
     * reach.
     *
     * @param value true if enable
     */
    public void setEnableLsd(boolean value) {
        sp.set_bool(settings_pack.bool_types.enable_lsd.swigValue(), value);
    }

    /**
     * The maximum allowed size (in bytes) to be
     * received by the metadata extension, i.e. magnet links.
     */
    public int getMaxMetadataSize() {
        return sp.get_int(settings_pack.int_types.max_metadata_size.swigValue());
    }

    /**
     * The maximum allowed size (in bytes) to be
     * received by the metadata extension, i.e. magnet links.
     *
     * @param value true if enable
     */
    public void setMaxMetadataSize(int value) {
        sp.set_int(settings_pack.int_types.max_metadata_size.swigValue(), value);
    }

    /**
     * This is a comma-separated list of IP port-pairs. They will be added
     * to the DHT node (if it's enabled) as back-up nodes in case we don't
     * know of any.
     * <p>
     * Changing these after the DHT has been started may not have any
     * effect until the DHT is restarted.
     */
    public String getDhtBootstrapNodes() {
        return  sp.get_str(string_types.dht_bootstrap_nodes.swigValue());
    }

    /**
     * This is a comma-separated list of IP port-pairs. They will be added
     * to the DHT node (if it's enabled) as back-up nodes in case we don't
     * know of any.
     * <p>
     * Changing these after the DHT has been started may not have any
     * effect until the DHT is restarted.
     *
     * @param value the IP port-pairs list
     */
    public void setDhtBootstrapNodes(String value) {
        sp.set_str(string_types.dht_bootstrap_nodes.swigValue(), value);
    }

    /**
     * This is the STUN server used by WebTorrent to enable ICE NAT
     * traversal for WebRTC. It must have the format ``hostname:port``.
     *
     * TODO: Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0
     * See https://github.com/arvidn/libtorrent/blob/master/include/libtorrent/settings_pack.hpp#L370
     */
    public String getWebtorrentStunServer() {
        new RuntimeException("Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0");
        return "sp.get_str(string_types.webtorrent_stun_server.swigValue());";
    }

    /**
     * This is the STUN server used by WebTorrent to enable ICE NAT
     * traversal for WebRTC. It must have the format ``hostname:port``.
     *
     * TODO: Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0
     * See https://github.com/arvidn/libtorrent/blob/master/include/libtorrent/settings_pack.hpp#L370
     *
     * @param value the STUN server endpoint
     */
    public void setWebtorrentStunServer(String value) {
        new RuntimeException("Uncomment code below when settings_pack.hpp from libtorrent master is merged to RC_2_0");
        //sp.set_str(string_types.webtorrent_stun_server.swigValue(), value);
    }

    /**
     * @return
     */
    public String listenInterfaces() {
        return sp.get_str(settings_pack.string_types.listen_interfaces.swigValue());
    }

    /**
     * @param value
     * @return this
     */
    public SettingsPack listenInterfaces(String value) {
        sp.set_str(settings_pack.string_types.listen_interfaces.swigValue(), value);
        return this;
    }

    /**
     * @return the current value
     * @see #stopTrackerTimeout(int)
     */
    public int stopTrackerTimeout() {
        return sp.get_int(settings_pack.int_types.stop_tracker_timeout.swigValue());
    }

    /**
     * {@code stop_tracker_timeout} is the number of seconds to wait when
     * sending a stopped message before considering a tracker to have
     * timed out. This is usually shorter, to make the client quit faster.
     * If the value is set to 0, the connections to trackers with the
     * stopped event are suppressed.
     *
     * @param value the new value
     * @return this
     */
    public SettingsPack stopTrackerTimeout(int value) {
        sp.set_int(settings_pack.int_types.stop_tracker_timeout.swigValue(), value);
        return this;
    }

    /**
     * @return the current value
     * @see #alertQueueSize(int)
     */
    public int alertQueueSize() {
        return sp.get_int(settings_pack.int_types.alert_queue_size.swigValue());
    }

    /**
     * {@code alert_queue_size} is the maximum number of alerts queued up
     * internally. If alerts are not popped, the queue will eventually
     * fill up to this level.
     *
     * @param value the new value
     * @return this
     */
    public SettingsPack alertQueueSize(int value) {
        sp.set_int(settings_pack.int_types.alert_queue_size.swigValue(), value);
        return this;
    }


    /**
     * {@code validate_https_trackers} when set to true, the certificate of HTTPS trackers
     * and HTTPS web seeds will be validated against the system's certificate store (as defined by OpenSSL).
     * If the system does not have a certificate store, this option may have to be disabled
     * in order to get trackers and web seeds to work).
     */
    public SettingsPack validateHttpsTrackers(boolean value) {
        sp.set_bool(settings_pack.bool_types.validate_https_trackers.swigValue(), value);
        return this;
    }

    public boolean validateHttpsTrackers() {
        return sp.get_bool(settings_pack.bool_types.validate_https_trackers.swigValue());
    }

    /**
     * Returns whether the disk copy-on-write optimization is disabled.
     * <p>
     * When disabled, libtorrent will not use the operating system's copy-on-write
     * mechanism for memory-mapped files. Disabling copy-on-write can improve
     * disk I/O performance on certain filesystems (e.g., Btrfs, APFS, ZFS)
     * where copy-on-write introduces additional overhead for torrent workloads.
     * <p>
     * By default this is {@code true} (copy-on-write is disabled) in current
     * libtorrent builds, as CoW tends to hurt torrent workload performance.
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Check current setting
     * boolean cowDisabled = pack.diskDisableCopyOnWrite();
     *
     * // Disable copy-on-write for better performance on CoW filesystems
     * pack.diskDisableCopyOnWrite(true);
     *
     * // Apply settings to session
     * sessionManager.applySettings(pack);
     *
     * // Or use fluent API during session setup
     * SettingsPack settings = new SettingsPack()
     *     .diskDisableCopyOnWrite(true)
     *     .downloadRateLimit(0)
     *     .uploadRateLimit(0);
     * }</pre>
     *
     * @return {@code true} if copy-on-write is disabled, {@code false} otherwise
     * @see #diskDisableCopyOnWrite(boolean)
     */
    public boolean diskDisableCopyOnWrite() {
        return sp.get_bool(settings_pack.bool_types.disk_disable_copy_on_write.swigValue());
    }

    /**
     * Sets whether to disable the disk copy-on-write optimization.
     * <p>
     * When set to {@code true}, libtorrent will not use the operating system's
     * copy-on-write mechanism for memory-mapped files. This can improve disk
     * I/O performance on filesystems that use copy-on-write by default (e.g.,
     * Btrfs, APFS, ZFS), where the CoW behavior introduces extra overhead
     * for random-write workloads typical of BitTorrent downloads.
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>On Btrfs/ZFS/APFS filesystems where CoW causes fragmentation</li>
     *   <li>When downloading to SSDs where CoW write amplification is a concern</li>
     *   <li>When experiencing slower-than-expected disk I/O throughput</li>
     * </ul>
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack()
     *     .diskDisableCopyOnWrite(true)
     *     .downloadRateLimit(0);
     *
     * sessionManager.applySettings(pack);
     * }</pre>
     *
     * @param value {@code true} to disable copy-on-write, {@code false} to leave it enabled
     * @return this SettingsPack for fluent chaining
     * @see #diskDisableCopyOnWrite()
     */
    public SettingsPack diskDisableCopyOnWrite(boolean value) {
        sp.set_bool(settings_pack.bool_types.disk_disable_copy_on_write.swigValue(), value);
        return this;
    }

    /**
     * Returns the override address for NAT-PMP (NAT Port Mapping Protocol)
     * gateway requests.
     * <p>
     * By default libtorrent auto-detects the local gateway by inspecting the
     * routing table. In certain topologies — VPN tunnels, multi-homed hosts,
     * containers, or networks where the default route does not own the public
     * IP — this auto-detection picks the wrong interface and port mappings
     * fail silently. This getter returns the currently configured gateway
     * override; an empty string means "let libtorrent auto-detect".
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>VPN client networks where the tun/tap interface is the default
     *       route but the VPN provider does not support NAT-PMP</li>
     *   <li>Multi-WAN or policy-routing setups where the gateway owning the
     *       public IP is not the default route</li>
     *   <li>Docker / LXC containers where the container gateway differs from
     *       the host gateway</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code ""} (empty string — auto-detect)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Check current override (will be empty on fresh pack)
     * String current = pack.natpmpGateway();
     * if (current.isEmpty()) {
     *     System.out.println("Auto-detect enabled");
     * }
     *
     * // Force NAT-PMP through a known gateway on a VPN subnet
     * pack.natpmpGateway("10.8.0.1");
     *
     * // Apply and start session
     * SessionManager session = new SessionManager();
     * session.start();
     * session.applySettings(pack);
     * }</pre>
     *
     * @return the configured NAT-PMP gateway address, or empty string when
     *         auto-detect is in effect
     * @see #natpmpGateway(String)
     * @see SessionManager
     * @since 2.0.12.9
     */
    public String natpmpGateway() {
        return sp.get_str(settings_pack.string_types.natpmp_gateway.swigValue());
    }

    /**
     * Overrides the gateway address used for NAT-PMP (NAT Port Mapping
     * Protocol) requests.
     * <p>
     * Setting a non-empty value forces libtorrent to send NAT-PMP
     * {@code map-request} packets to the supplied IPv4 address instead of
     * querying the operating-system routing table. This is the only reliable
     * way to make port-mapping work when the default route does not own the
     * public IP (e.g., VPN split-tunnel, multi-homed server, container
     * bridge). If the string is empty (the default) libtorrent falls back
     * to automatic gateway discovery.
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>When {@code portmap_log} alerts show "no router" but you know a
     *       NAT-PMP-enabled gateway exists on a secondary interface</li>
     *   <li>After switching from Wi-Fi to wired Ethernet and the gateway
     *       changes subnet (e.g. 192.168.1.1 → 192.168.50.1)</li>
     *   <li>On seed-boxes with multiple upstream ISPs where only one router
     *       supports NAT-PMP / UPnP</li>
     * </ul>
     * <p>
     * <b>Constraints &amp; edge cases:</b>
     * <ul>
     *   <li>The value must be a valid dotted IPv4 address. Hostnames are
     *       <b>not</b> resolved by libtorrent.</li>
     *   <li>Changing this setting at runtime invalidates existing port maps;
     *       libtorrent will re-map through the new gateway on the next
     *       refresh cycle.</li>
     *   <li>Has no effect when UPnP is used instead of NAT-PMP.</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code ""} (empty string — auto-detect)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * // Detect auto-detect failure from portmap alerts, then override
     * SessionManager session = new SessionManager();
     * session.start();
     *
     * SettingsPack pack = new SettingsPack();
     * pack.setEnableDht(true)
     *     .portRangeStart(6881)
     *     .portRangeEnd(6889);
     *
     * // After observing "no router" portmap_log alerts, pin the gateway
     * pack.natpmpGateway("192.168.50.1");
     *
     * session.applySettings(pack);
     * }</pre>
     *
     * @param value the NAT-PMP gateway IPv4 address, or empty string to
     *              restore auto-detect
     * @return this SettingsPack for fluent chaining
     * @see #natpmpGateway()
     * @see SessionManager
     * @since 2.0.12.9
     */
    public SettingsPack natpmpGateway(String value) {
        sp.set_str(settings_pack.string_types.natpmp_gateway.swigValue(), value);
        return this;
    }

    /**
     * Returns whether libtorrent allows more than one simultaneous connection
     * from peers that share the same peer ID (PID).
     * <p>
     * The BitTorrent protocol nominally assigns a unique peer ID to every
     * client instance, but in practice multiple peers behind the same NAT may
     * expose the same PID because they run the same client version and port,
     * or because a buggy tracker hands out identical IDs. By default
     * (value {@code false}) libtorrent keeps only the first connection and
     * drops duplicates as a anti-DDoS / anti-loop measure.
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>Swarms with many peers behind carrier-grade NAT (CGNAT) where
     *       distinct users legitimately share the same public IP and PID</li>
     *   <li>Private trackers that intentionally assign identical peer IDs
     *       to sibling instances for accounting</li>
     *   <li>Testing / debugging scenarios where you run multiple local
     *       clients with the same {@code peer_id} prefix</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code false} (duplicate peer IDs are rejected)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Check whether duplicates are currently allowed
     * boolean allowed = pack.allowMultipleConnectionsPerPid();
     * System.out.println("Duplicates allowed: " + allowed); // false
     *
     * // Only enable for a specific private swarm known to use CGNAT
     * if (swarmIsBehindCGNAT()) {
     *     pack.allowMultipleConnectionsPerPid(true);
     * }
     *
     * session.applySettings(pack);
     * }</pre>
     *
     * @return {@code true} if multiple connections per peer ID are allowed,
     *         {@code false} if duplicates are dropped
     * @see #allowMultipleConnectionsPerPid(boolean)
     * @see SessionManager
     * @since 2.0.12.9
     */
    public boolean allowMultipleConnectionsPerPid() {
        return sp.get_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue());
    }

    /**
     * Controls whether libtorrent accepts multiple simultaneous connections
     * from peers that present the same peer ID.
     * <p>
     * Disabling the duplicate-PID guard ({@code false}, the default) is the
     * safest choice for public swarms because it prevents:
     * <ul>
     *   <li>Connection loops created by mis-configured port-forwarding</li>
     *   <li>Accidental (or intentional) connection-flooding from a single
     *       hostile client re-using one peer ID</li>
     *   <li>Wasted upload bandwidth to the same remote client instance</li>
     * </ul>
     * <p>
     * Enabling it ({@code true}) relaxes that safety check and is
     * appropriate only when you <b>know</b> the swarm contains multiple
     * legitimate peers sharing a PID — typically because of NAT or tracker
     * behaviour, not malice.
     * <p>
     * <b>When to use:</b>
     * <ul>
     *   <li>Enterprise or campus networks with symmetric NAT where many
     *       identical client versions appear behind one public IP</li>
     *   <li>Swarms served by a tracker that assigns peer IDs based on
     *       ASN rather than per-user nonce</li>
     *   <li>Integration tests that spawn many local libtorrent instances
     *       with the same {@code fingerprint}</li>
     * </ul>
     * <p>
     * <b>Constraints &amp; security warning:</b>
     * <ul>
     *   <li>Do <b>not</b> enable on public, untrusted swarms — a single
     *       attacker can exhaust your connection limit by reconnecting
     *       under the same peer ID.</li>
     *   <li>This setting interacts with {@code connectionsLimit()}; if the
     *       limit is low, duplicate-PID connections may crowd out unique
     *       peers.</li>
     *   <li>Changes take effect immediately for new incoming connections;
     *       existing duplicates are not retroactively killed.</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code false}
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Default: safe mode — reject duplicate peer IDs
     * pack.allowMultipleConnectionsPerPid(false);
     *
     * // For a controlled, private swarm behind the same corporate NAT:
     * if (torrent.isPrivate() && network.isCorporateNAT()) {
     *     pack.allowMultipleConnectionsPerPid(true)
     *         .connectionsLimit(500);  // raise limit to absorb extra peers
     * }
     *
     * session.applySettings(pack);
     * }</pre>
     *
     * @param value {@code true} to allow multiple connections per peer ID,
     *              {@code false} to reject duplicates
     * @return this SettingsPack for fluent chaining
     * @see #allowMultipleConnectionsPerPid()
     * @see #connectionsLimit(int)
     * @see SessionManager
     * @since 2.0.12.9
     */
    public SettingsPack allowMultipleConnectionsPerPid(boolean value) {
        sp.set_bool(settings_pack.bool_types.allow_multiple_connections_per_pid.swigValue(), value);
        return this;
    }

    /**
     * Returns the lease duration, in seconds, requested for NAT-PMP port
     * mappings.
     * <p>
     * When libtorrent asks a NAT-PMP-enabled router to open a port, it
     * proposes a lifetime for that mapping. The router may honour the request
     * or substitute its own maximum. A longer lease means fewer renewal
     * packets and lower background traffic; a shorter lease means the mapping
     * vanishes quickly if the client crashes, keeping the router's NAT table
     * small and friendly to other devices.
     * <p>
     * <b>When to use shorter leases:</b>
     * <ul>
     *   <li>Short interactive sessions (e.g., a mobile app that seeds only
     *       while the user is watching)</li>
     *   <li>Routers with tiny NAT tables that evict long-lived mappings</li>
     *   <li>Development / CI environments where the session is torn down
     *       within minutes</li>
     * </ul>
     * <b>When to use longer leases:</b>
     * <ul>
     *   <li>24/7 seed-boxes or dedicated home servers</li>
     *   <li>Connections over satellite or high-latency links where every
     *       extra packet is expensive</li>
     *   <li>Routers known to rate-limit NAT-PMP renewals</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code 3600} seconds (1 hour)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * SettingsPack pack = new SettingsPack();
     *
     * // Default is 3600s — fine for a typical desktop session
     * int current = pack.natpmpLeaseDuration();
     * System.out.println("Current lease: " + current + "s");
     *
     * // For a short-lived mobile session, reduce to 5 minutes
     * pack.natpmpLeaseDuration(300);
     *
     * // For a long-running seeder on a stable home network, extend to 2h
     * if (sessionType == SEED_BOX) {
     *     pack.natpmpLeaseDuration(7200);
     * }
     *
     * session.applySettings(pack);
     * }</pre>
     *
     * @return the NAT-PMP lease duration in seconds
     * @see #natpmpLeaseDuration(int)
     * @see #natpmpGateway(String)
     * @see SessionManager
     * @since 2.0.12.9
     */
    public int natpmpLeaseDuration() {
        return sp.get_int(settings_pack.int_types.natpmp_lease_duration.swigValue());
    }

    /**
     * Sets the requested lease duration for NAT-PMP port mappings in seconds.
     * <p>
     * Every mapping created via NAT-PMP (and by extension PCP) carries a
     * lifetime. libtorrent renews the mapping automatically at roughly 50 %
     * of the lease elapsed time, so the chosen value directly controls
     * background chatter: a 3600-second lease generates a renewal every ~30
     * minutes, while a 300-second lease renews every ~2.5 minutes.
     * <p>
     * Choose the value based on your session's expected lifetime and the
     * router's behaviour. Many consumer routers silently cap leases at 7200 s
     * (2 h) regardless of what the client asks, so values above that may
     * not produce any benefit. Conversely, values below 60 s can trigger
     * aggressive renewal loops or be rejected outright by picky firmware.
     * <p>
     * <b>Constraints &amp; edge cases:</b>
     * <ul>
     *   <li>Values &lt; 60 are allowed but may be rounded up by the router or
     *       cause excessive renewal traffic.</li>
     *   <li>Zero is technically valid in the NAT-PMP spec but is interpreted
     *       by many routers as "delete this mapping immediately"; avoid it
     *       unless you explicitly want to un-map.</li>
     *   <li>Changing this setting does not alter the lifetime of already
     *       active mappings — only new requests use the updated value.</li>
     *   <li>Has no effect on UPnP-IGD mappings, which usually follow the
     *       router's own lease policy.</li>
     * </ul>
     * <p>
     * <b>Default:</b> {@code 3600} seconds (1 hour)
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     * // Dynamic adjustment based on session type
     * SettingsPack pack = new SettingsPack()
     *     .setEnableDht(true)
     *     .portRangeStart(50000)
     *     .portRangeEnd(50050);
     *
     * switch (sessionProfile) {
     *     case MOBILE_TEMPORARY:
     *         // 5 min — router table stays small, user won't notice renewals
     *         pack.natpmpLeaseDuration(300);
     *         break;
     *     case DESKTOP_STANDARD:
     *         // 1 hour — default, balanced
     *         pack.natpmpLeaseDuration(3600);
     *         break;
     *     case SEED_BOX:
     *         // 2 hours — minimise packet count on a stable server
     *         pack.natpmpLeaseDuration(7200);
     *         break;
     * }
     *
     * session.applySettings(pack);
     * }</pre>
     *
     * @param value the NAT-PMP lease duration in seconds
     * @return this SettingsPack for fluent chaining
     * @see #natpmpLeaseDuration()
     * @see #natpmpGateway(String)
     * @see SessionManager
     * @since 2.0.12.9
     */
    public SettingsPack natpmpLeaseDuration(int value) {
        sp.set_int(settings_pack.int_types.natpmp_lease_duration.swigValue(), value);
        return this;
    }
}
